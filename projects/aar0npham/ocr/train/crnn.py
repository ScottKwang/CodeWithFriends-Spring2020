import os
import random
import string
import sys
import time

import numpy as np
import torch
import torch.backends.cudnn as cudnn
import torch.nn.functional as F
import torch.nn.init as init
import torch.optim as optim
import yaml
from torch.utils.data import DataLoader

from ocr.model import CRNNet
from ocr.tools import (AlignCollate, AttnLabelConverter, Averager, CTCLabelConverter, LMDBDataset, RandomSequentialSampler, ResizeNormalize,
                       load_data)

# load device for either `cuda` or `cpu`
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
with open(os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), 'config.yml'), 'r') as f:
    CONFIG = yaml.safe_load(f)
# setup some global Variable, torch.autograd.Variable is deprecated
if CONFIG['rgb']:
    CONFIG['input_channel'] = 3
else:
    CONFIG['input_channel'] = 1
IMAGE = torch.FloatTensor((CONFIG['batch_size'], CONFIG['input_channel'], CONFIG['height'], CONFIG['height'])).to(device)
TEXT = torch.IntTensor(CONFIG['batch_size'] * 5).to(device)
LENGTH = torch.IntTensor(CONFIG['batch_size']).to(device)
DASHED = '-' * 80

# randomize seeds
random.seed(CONFIG['seeds'])
np.random.seed(CONFIG['seeds'])
torch.manual_seed(CONFIG['seeds'])
torch.cuda.manual_seed(CONFIG['seeds'])
cudnn.benchmark = True
cudnn.deterministic = True

if not os.path.exists(CONFIG['log_dir']):
    os.makedirs(CONFIG['log_dir'])
# init logs file for easier debugging
with open(os.path.join(CONFIG['log_dir'], 'log_dataset.txt'), 'a') as dataset_log:
    dataset_log.write(DASHED + '\n')
    print(DASHED)
    # init dataset/loader
    train_dataset = LMDBDataset(CONFIG, root=CONFIG['train_root'])
    print(f'dataset_root:{CONFIG["train_root"]}\nbatch_size:{CONFIG["batch_size"]}\n')
    dataset_log.write(f'dataset_root:{CONFIG["train_root"]}\nbatch_size:{CONFIG["batch_size"]}\n')
    dataset_log.close()
if not CONFIG['random_sample']:
    sampler = RandomSequentialSampler(train_dataset, CONFIG['batch_size'])
else:
    sampler = None
collate_fn = AlignCollate(height=CONFIG['height'], width=CONFIG['width'], keep_ratio=CONFIG['keep_ratio'])
train_loader = DataLoader(train_dataset,
                          batch_size=CONFIG['batch_size'],
                          shuffle=True,
                          sampler=sampler,
                          num_workers=int(CONFIG['workers']),
                          collate_fn=collate_fn,
                          pin_memory=True)
val_dataset = LMDBDataset(CONFIG, root=CONFIG['val_root'], transform=ResizeNormalize((100, 32)))

# setup fine tune
with open(os.path.join(CONFIG['log_dir'], 'log_model.txt'), 'a') as f:
    CONFIG['num_classes'] = len(CONFIG['character']) + 1
    if CONFIG['prediction'] == 'CTC':
        converter = CTCLabelConverter(CONFIG['character'])
    else:
        converter = AttnLabelConverter(CONFIG['character'])
    CONFIG['num_classes'] = len(converter.character)

    # init model here
    model = CRNNet(CONFIG)
    model_log = f'model input params:\nheight:{CONFIG["height"]}\nwidth:{CONFIG["width"]}\nfidicial points:{CONFIG["num_fiducial"]}\ninput channel:{CONFIG["input_channel"]}\noutput channel:{CONFIG["output_channel"]}\nhidden size:{CONFIG["hidden_size"]}\nnum class:{CONFIG["num_classes"]}\nbatch_max_len:{CONFIG["batch_max_len"]}\nmodel structures as follow:{CONFIG["transform"]}-{CONFIG["backbone"]}-{CONFIG["sequence"]}-{CONFIG["prediction"]}'
    print(model_log)
    f.write(model_log)
    f.close()

# init weights, skips for loc_fc2
for name, params in model.named_parameters():
    if 'loc_fc2' in name:
        print(f'skips {name} since fc2 is already initialized')
        continue
    try:
        if 'bias' in name:
            init.constant_(params, 0.)
        elif 'weight' in name:
            init.kaiming_normal_(params)  # torch way of saying he_norm
    except Exception as e:  # for batchnorm
        print(e)
        if 'weight' in name:
            params.data.fill_(1)
        continue

# if you have multiple gpu go ahead I only have 1060 =(
if torch.cuda.device_count() > 1:
    model = torch.nn.DataParallel(model).to(device)

# check whether there is pretrained model for continue-training
if CONFIG['saved_model_path'] != '':
    print(f'loading pretrained models from {CONFIG["saved_model_path"]}')
    if CONFIG['fine_tune']:
        model.load_state_dict(torch.load(CONFIG['saved_model_path']), strict=False)
    else:
        model.load_state_dict(torch.load(CONFIG['saved_model_path']))
print(f'Model:{model}')

# get loss
if CONFIG['prediction'] == 'CTC':
    loss_fn = torch.nn.CTCLoss(zero_infinity=True).to(device)
else:
    loss_fn = torch.nn.CrossEntropyLoss(ignore_index=0).to(device)  # [GO] idx at 0
avg_loss = Averager()

filtered_params, num_params = [], []
for p in filter(lambda p: p.requires_grad, model.parameters()):
    filtered_params.append(p)
    num_params.append(np.prod(p.size()))
print(f'trainable params: {sum(num_params)}')

# get optimizer
if CONFIG['adam']:
    optimizer = optim.Adam(filtered_params, lr=CONFIG['lr'], betas=(CONFIG['beta1'], 0.999))
else:
    optimizer = optim.Adadelta(filtered_params, lr=CONFIG['lr'], rho=CONFIG['rho'], eps=CONFIG['eps'])
print(f'optimizer: {optimizer}')

with open(os.path.join(CONFIG['log_dir'], 'log_config.txt'), 'a+') as log:
    options = '------------------Options------------------\n'
    for k, v in CONFIG.items():
        options += f'{str(k)}: {str(v)}\n'
    options += '-------------------------------------------\n'
    print(options)
    log.write(options)
    log.close()


def evaluation(net, val_dataset, loss_fn, config=CONFIG):
    # evaluation_fn
    """
    args:
        - net: CRNNet model
        - val_dataset: dataset used for validation
        - loss_fn: loss function, either torch.nn.CTCLoss or torch.nn.CrossEntropyLoss
        - config: list parsed from config.yml
    returns:
        - val_loss: loss from validation
        - accuracy: accuracy of the prediction
        - preds_: prediction list
        - confidence_: confidence score list
    """
    print('\nstart validation...\n')
    # some variable here
    num_correct = 0
    # FIXME: added evaluation case for ICDAR2019-STROIE normalized edit_distance
    # norm_ed = 0
    len_data = 0
    infer_ = 0  # track infer time
    avg_loss = Averager()

    # disable gradient when validating
    for p in net.parameters():
        p.requires_grad = False
    # evaluation mode intensifies =))
    net.eval()
    val_loader = DataLoader(val_dataset, shuffle=True, batch_size=CONFIG['batch_size'], num_workers=int(CONFIG['workers']))
    val_iter = iter(val_loader)
    max_iter = min(config['max_iter'], len(val_loader))

    for i, (img_tensor, label) in enumerate(val_loader):
        batch_size = img_tensor.size(0)
        len_data += batch_size
        img = img_tensor.to(device)
        # max length prediction
        preds_size = torch.IntTensor([config['batch_max_len'] * batch_size]).to(device)  # length of prediction
        preds_text = torch.LongTensor(batch_size, config['batch_max_len'] + 1).fill_(0).to(device)

        loss_text, len_loss = converter.encode(label, batch_max_len=config['batch_max_len'])

        start_ = time.time()
        if config['prediction'] == 'CTC':
            preds = net(img, preds_text)
            forward_ = time.time() - start_
            print(f'time took to predict with {config["prediction"]}: {forward_:5.2f}')
            # we need evaluation loss when using CTC
            ctc_preds = torch.IntTensor([preds.size(1)] * batch_size)  # ctc preds_size
            # then permute preds to ctc format : label,input_len, label_len
            cost = loss_fn(preds.log_softmax(2).permute(1, 0, 2), loss_text, ctc_preds, len_loss)

            # greedy decoding the convert idx to character
            _, preds_idx = preds.max(2)
            preds_idx = preds_idx.view(-1)
            preds_ = converter.decode(preds_idx.data, ctc_preds.data)

        else:
            # for attention decoder
            preds = net(img, preds_text, trainning=False)
            forward_ = time.time() - start_
            print(f'time took to predict with {config["prediction"]}: {forward_:5.2f}')
            preds = preds[:, :loss_text.shape[1] - 1, :]
            target = loss_text[:, 1:]  # prune [GO] symbol
            # FIXME: fixes MemoryError when training at cost -> DONE
            cost = loss_fn(
                preds.contiguous().view(-1, preds.shape[-1]),
                target.contiguous().view(
                    -1))  # tensor.contiguous() allows to use previous loaded tensor in memmory (if available then return the previous tensor)
            _, preds_idx = preds.max(2)
            preds_ = converter.decode(preds_idx, preds_size)
            label = converter.decode(loss_text[:, 1:], len_loss)

        infer_ += forward_
        avg_loss.add(cost)

        # returns accuracy and confidence score
        probs = F.softmax(preds, dim=2)
        max_probs, _ = probs.max(dim=2)
        confidence_ = []
        for gt, pred, max_prob in zip(label, preds_, max_probs):
            if config['prediction'] == 'Attention':
                gt = gt[:gt.find('[s]')]  # prune EOS token
                pred_EOS = pred.find('[s]')
                pred = pred[:pred_EOS]
                max_prob = max_prob[:pred_EOS]
            if pred == gt:
                num_correct += 1
            try:
                score = max_prob.cumprod(dim=0)[-1]
            except:
                socre = 0  # empty pred case when already removed EOS token
            confidence_.append(score)
    accuracy = num_correct / float(len_data) * 100
    valid_loss = avg_loss.val()
    return valid_loss, accuracy, preds_, confidence_, label, infer_, len_data


def train_batch(net, train_loader, loss_fn, optimizer, IMAGE=IMAGE, TEXT=TEXT, LENGTH=LENGTH):
    iters = iter(train_loader)
    img, text = iters.next()
    load_data(IMAGE, img)
    IMAGE = IMAGE.to(device)
    t, l = converter.encode(text, batch_max_len=CONFIG['batch_max_len'])
    load_data(TEXT, t)
    load_data(LENGTH, l)
    batch_size = IMAGE.size(0)

    if CONFIG['prediction'] == 'CTC':
        preds = net(IMAGE, TEXT).log_softmax(2)
        preds_size = torch.IntTensor([preds.size(1)] * batch_size)
        perds = preds.permute(1, 0, 2)

        # disable cudnn for ctc_loss
        torch.backends.cudnn.enabled = False
        cost = loss_fn(preds, TEXT.to(device), preds_size.to(device), LENGTH.to(device))
        torch.backends.cudnn.enabled = True
    else:
        preds = net(IMAGE, TEXT[:, :-1])  # align with attention.forward()
        target = TEXT[:, 1:]  # prune [GO] symbol
        cost = loss_fn(preds.view(-1, preds.shape[-1]), target.contiguous().view(-1))

    net.zero_grad()
    cost.backward()
    torch.nn.utils.clip_grad_norm_(net.parameters(), CONFIG['grad_clip'])  # gradient clipping with 5
    optimizer.step()
    return cost


start_ = time.time()
best_acc = -1
# training loop start here
for epoch in range(CONFIG['num_epochs']):
    i = 0
    while i < len(train_loader):
        for p in model.parameters():
            p.requires_grad = True
        model.train()

        cost = train_batch(model, train_loader, loss_fn, optimizer)
        avg_loss.add(cost)
        i += 1

        if i % CONFIG['val_interval'] == 0:
            elapsed_ = time.time() - start_
            with open(os.path.join(CONFIG['log_dir'], 'log_train.txt'), 'a') as log:
                model.eval()
                with torch.no_grad():
                    valid_loss, accuracy, preds, confidence_, label, infer_, len_data = \
                            evaluation(model, val_dataset, loss_fn)
                model.train()

                # record some info into logs file
                loss_log = f'[{i}/{CONFIG["num_iters"]}] train_loss: {avg_loss.val():0.5f} | val_loss: {valid_loss:0.5f} | elapsed time: {elapsed_:0.5f}'
                avg_loss.reset()

                model_log = f'{"accuracy":20s}: {accuracy:0.3f}'

                if accuracy > best_acc:
                    best_acc = accuracy
                    torch.save(model.state_dict(), os.path.join(CONFIG['log_dir'], 'best_acc.pth'))
                best_model_log = f'{"best accuracy":20s}: {best_acc:0.3f}'

                loss_model_log = f'{loss_log}\n{model_log}\n{best_model_log}\n'
                print(loss_model_log)
                log.write(loss_model_log)

                # write prediction result to log
                pred_head = f'{"ground truth":20s} | {"prediction":20s} | confidence | T&F'
                pred_log = f'{DASHED}\n{pred_head}\n{DASHED}\n'
                for gt, pred, confidence in zip(label[5:], preds[5:], confidence_[5:]):
                    if CONFIG['prediction'] == 'Attention':
                        gt = gt[:gt.find('[s]')]
                        pred = pred[:pred.find('[s]')]

                    pred_log += f'{gt:20s} | {pred:20s} | {confidence:0.4f} | {str(pred==gt)}\n'
                pred_log += f'{DASHED}'
                print(pred_log)
                log.write(pred_log + '\n')
                log.close()

        if i % CONFIG['save_interval'] == 0:
            torch.save(model.state_dict(), os.path.join(CONFIG['log_dir'], f'iter_{i}.pth'))

        if i == CONFIG['num_iters']:
            print('Stop training here.')
            sys.exit()
