import os
from collections import OrderedDict
from functools import cmp_to_key

import cv2
import numpy as np
import torch
import torch.backends.cudnn as cudnn
import torch.nn as nn
import torch.nn.functional as F
import yaml
from PIL import Image

from model import CRNNet, Placeholder, VGG_UNet
from tools import (AttnLabelConverter, CTCLabelConverter, ResizeNormalize, adjustResultCoordinates, compare_rects, getDetBoxes, normalizeMeanVariance,
                   resizeAspectRatio)

DEVICE = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
MODEL_PATH = os.path.join(os.path.dirname(os.path.relpath(__file__)), 'save_models')
with open(os.path.join(os.path.dirname(os.path.realpath(__file__)), 'config.yml'), 'r') as yf:
    CONFIG = yaml.safe_load(yf)


def copyStateDict(state_dict):
    if list(state_dict.keys())[0].startswith('module'):
        start_idx = 1
    else:
        start_idx = 0

    new_state_dict = OrderedDict()
    for k, v in state_dict.items():
        name = '.'.join(k.split('.')[start_idx:])
        new_state_dict[name] = v
    return new_state_dict


class CRAFT(Placeholder):
    def __init__(self, state_dict='CRAFT.pth', device=DEVICE, docker=False):
        super().__init__()
        self.model_path = os.path.join(MODEL_PATH, state_dict)
        self.net = VGG_UNet().to(device)
        self.device = device
        if docker:
            self.toContainer(docker=docker)
        self.canvas_size = 1280
        self.magnify_ratio = 1.5
        self.txtThreshold = 0.7
        self.linkThreshold = 0.4
        self.lowTxtScore = 0.4
        self.enablePoly = False
        self.load()

    def toContainer(self, docker=False):
        if docker:
            self.net = self.net.to('cpu')
        else:
            self.net = self.net.to('cuda')

    def load(self):
        if self.device == 'cuda':
            self.net.load_state_dict(copyStateDict(torch.load(self.model_path)))
            if torch.cuda.device_count() > 1:
                self.net = nn.DataParallel(self.net).to(self.device)
            cudnn.benchmark = False
        else:
            # added compatibility for running on non-cuda device
            self.net.load_state_dict(copyStateDict(torch.load(self.model_path, map_location='cpu')))

        self.net.eval()

    def preproc(self, image):
        # refactor the code
        # returns image tensor, target w, h resized
        img_resized, target_ratio, _ = resizeAspectRatio(image, self.canvas_size, interpolation=cv2.INTER_LINEAR, mag_ratio=self.magnify_ratio)
        ratio_h = ratio_w = 1 / target_ratio

        x = normalizeMeanVariance(img_resized)
        x = torch.from_numpy(x).permute(2, 0, 1)  # [h x w x c] -> [c x h x w]
        x = torch.Tensor(x.unsqueeze(0)).to(self.device)
        return x, ratio_w, ratio_h

    def getCoords(self, inputs, ratio_w, ratio_h):
        # returns rectified boxes

        boxes, polys = getDetBoxes(inputs[0], inputs[1], self.txtThreshold, self.linkThreshold, self.lowTxtScore, self.enablePoly)
        boxes = adjustResultCoordinates(boxes, ratio_w, ratio_h)
        polys = adjustResultCoordinates(boxes, ratio_w, ratio_h)
        for k, _ in enumerate(polys):
            if polys[k] is None:
                polys[k] = boxes[k]

        rects = list()
        for box in boxes:
            poly = np.array(box).astype(np.int32)
            y0, x0 = np.min(poly, axis=0)
            y1, x1 = np.max(poly, axis=0)
            rects.append([x0, y0, x1, y1])
        return rects

    def process(self, image):
        roi = list()  # extract ROI
        with torch.no_grad():
            im_tensor, ratio_w, ratio_h = self.preproc(image)
            y, _ = self.net(im_tensor)
            score_text = y[0, :, :, 0].cpu().data.numpy()
            score_link = y[0, :, :, 1].cpu().data.numpy()
            rects = self.getCoords([score_text, score_link], ratio_w, ratio_h)
            for rect in sorted(rects, key=cmp_to_key(compare_rects)):
                x0, y0, x1, y1 = rect
                sub = image[x0:x1, y0:y1, :]
                roi.append(sub)
        # can return : roi, boxes, polys, image
        return roi


class CRNN(Placeholder):
    def __init__(self, state_dict='CRNN.pth', device=DEVICE, docker=False):
        super().__init__()
        self.alphabet = '0123456789abcdefghijklmnopqrstuvwxyz'
        self.model_path = os.path.join(MODEL_PATH, state_dict)
        self.net = CRNNet(CONFIG, device).to(device)
        if docker:
            self.toContainer(docker=docker)
        self.config = CONFIG
        self.device = device
        self.load()

    def toContainer(self, docker=False):
        if docker:
            self.net = self.net.to('cpu')
        else:
            self.net = self.net.to('cuda')

    def load(self):
        self.transformer = ResizeNormalize((100, 32))
        if self.device == 'cuda':
            if torch.cuda.device_count() > 1:
                self.net = nn.DataParallel(self.net).to(self.device)
            self.net.load_state_dict(torch.load(self.model_path))
        # print(f'loading pretrained from {self.model_path}')
        else:
            self.net.load_state_dict(torch.load(self.model_path, map_location='cpu'))
        if self.config['prediction'] == 'CTC':
            self.converter = CTCLabelConverter(self.alphabet)
        else:
            self.converter = AttnLabelConverter(self.alphabet)

        for p in self.net.parameters():
            p.requires_grad = False
        self.net.eval()

    def getPreds(self, inputs):
        pred = self.config['prediction']
        # since we only process one image -> batch_size=1
        image = Image.fromarray(inputs).convert('L')
        # print(image.size)
        image = self.transformer(image).to(self.device)
        image = image.view(1, *image.size()).to(self.device)
        len_pred = torch.IntTensor([self.config['batch_max_len']]).to(self.device)
        text_pred = torch.LongTensor(1, self.config['batch_max_len'] + 1).fill_(0).to(self.device)

        if pred == 'CTC':
            preds = self.net(image, text=text_pred)
            preds_size = torch.IntTensor([preds.size(1)])
            _, preds_idx = preds.max(2)
            preds_idx = preds_idx.view(-1)
            raw_pred = self.converter.decode(preds_idx.data, preds_size.data)
        else:
            preds = self.net(image, text=text_pred, training=False)
            _, preds_idx = preds.max(2)
            raw_pred = self.converter.decode(preds_idx, len_pred)
        return raw_pred, preds

    def process(self, image):
        res = dict()
        raw_pred, preds = self.getPreds(image)
        probs = F.softmax(preds, dim=2)
        max_probs, _ = probs.max(dim=2)
        with open(os.path.join(os.path.dirname(os.path.relpath(__file__)), 'test', 'log_results.txt'), 'w+') as f:
            for max_prob in max_probs:
                # returns prediction here
                if self.config['prediction'] == 'Attention':
                    try:
                        # [EOS] token process in a list
                        pred_EOS = raw_pred[0].index('[s]')
                        raw_pred = raw_pred[0][:pred_EOS]
                        max_prob = max_prob[:pred_EOS]
                    except ValueError:
                        print('Not found EOS token, continue.\n(potential error)')
                        continue  # when there isn't a EOS token
                confidence = max_prob.cumprod(dim=0)[-1]
                res[confidence] = raw_pred
                f.write(f'results: {raw_pred}\nconfidence score: {confidence:.4f}\n')
                print(f'results: {raw_pred}\nconfidence score: {confidence:.4f}\n')
        return raw_pred, res
