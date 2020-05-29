# refers to https://github.com/bgshih/crnn/blob/master/tool/create_dataset.py
import os
import sys

import cv2
import lmdb
import numpy as np


def checkImgValid(img_bin):
    if img_bin is None:
        return False
    imgbuf = np.frombuffer(img_bin, dtype=np.uint8)
    img = cv2.imdecode(imgbuf, cv2.IMREAD_GRAYSCALE)
    h, w = img.shape[0], img.shape[1]
    if h * w == 0:
        return False
    return True


def writeCache(env, cache):
    with env.begin(write=True) as txn:
        for k, v in cache.items():
            txn.put(k, v)


def anno2list(out='data/', root='/mnt/Vault/database/mjsynth/', l=['annotation_test.txt', 'annotation_train.txt', 'annotation_val.txt']):
    if os.path.exists(f'{out}/train_list.txt'):
        print('list has already been created, exit.')
        return 0
    os.makedirs(out, exist_ok=True)
    for f in [os.path.join(root, i) for i in l]:
        fname = os.path.basename(f)
        outputs = f'{fname.split("_")[1].split(".")[0]}_list.txt'
        with open(os.path.join(out, outputs), 'w') as txt:
            lines = open(f, 'r').readlines()
            for l in lines:
                path = l.strip().split(' ')[0]
                label = os.path.basename(path).split('_')[1]
                txt.write(f'{path} {label}\n')


def DatasetGenerator(root, output_path, list_path, log_path, check_valid=True):
    # creates lmdb dataset
    # root: based dir for mjsynth
    # image_path: train_lists.txt -> images, labels
    os.makedirs(output_path, exist_ok=True)
    env = lmdb.open(output_path, map_size=107374182400)  # -> 100GB
    cache = {}
    c = 1
    images, labels = [], []
    with open(list_path, 'r', encoding='utf-8') as f:
        data = f.readlines()
        for l in data:
            images.append(os.path.join(root, l.split()[0][2:]))
            labels.append(l.split()[1])

    num_samples = len(images)
    for i in range(num_samples):
        impath = images[i]
        label = labels[i]
        with open(impath, 'rb') as f:
            img_bin = f.read()
        if check_valid:
            try:
                if not checkImgValid(img_bin):
                    print(f'{impath} is not a valid image')
                    continue
            except Exception:
                with open(os.path.join(log_path, 'error_image.txt'), 'w') as log:
                    log.write(f'{str(i)}th image: errored\n')
                continue

        img_key = f'image-{c}'.encode()
        label_key = f'label-{c}'.encode()
        cache[img_key] = img_bin
        cache[label_key] = label.encode()

        if c % 1000 == 0:
            writeCache(env, cache)
            cache = {}
            print(f'written {c}/{num_samples}')
        c += 1
    num_samples = c - 1
    cache['num-samples'.encode()] = str(num_samples).encode()
    writeCache(env, cache)
    print(f'created dataset with {num_samples} samples')


if __name__ == '__main__':
    anno2list()
    file_ = [f for f in os.listdir('data') if os.path.isfile(os.path.join('data', f))]
    for f in file_:
        DatasetGenerator(root='/mnt/Vault/database/mjsynth',
                         output_path=f'data/{f.split("_")[0]}',
                         list_path=os.path.join('data', f),
                         log_path='data/logs')
