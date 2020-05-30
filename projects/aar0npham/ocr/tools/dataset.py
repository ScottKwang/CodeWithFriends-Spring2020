import math
import os
import random
import re
import sys

import lmdb
import numpy as np
import six
import torch
import torchvision.transforms as transforms
from PIL import Image
from torch.utils.data import Dataset
from torch.utils.data.sampler import Sampler


def load_data(v, data):
    v.resize_(data.size()).copy_(data)


# taken from python docs
def _accumulate(iterable, fn=lambda x, y: x + y):
    # _accumulate([1,2,3,4,5]) -> 1 3 6 10 15
    # return running totals
    it = iter(iterable)
    try:
        total = next(it)
    except StopIteration:
        return
    yield total
    for element in it:
        total = fn(total, element)
        yield total


# refers to https://github.com/meijieru/crnn.pytorch/blob/master/dataset.py
class ResizeNormalize(object):
    def __init__(self, size, interpolation=Image.BICUBIC):
        self.size = size
        self.interpolation = interpolation
        self.toTensor = transforms.ToTensor()

    def __call__(self, img):
        img = img.resize(self.size, self.interpolation)
        img = self.toTensor(img)
        img.sub_(0.5).div_(0.5)
        return img


class NormalizePad(object):
    def __init__(self, max_size, pad_type='right'):
        self.toTensor = transforms.ToTensor()
        self.max_size = max_size
        self.max_width_half = math.floor(max_size[2] / 2)
        self.pad_type = pad_type

    def __call__(self, img):
        img = self.toTensor(img)
        img.sub_(0.5).div_(0.5)
        c, h, w = img.size()
        padded = torch.FloatTensor(*self.max_size).fill_(0)
        padded[:, :, w:] = img  # -> right pad
        if self.max_size[2] != w:
            padded[:, :, w:] = img[:, :, w - 1].unsqueeze(2).expand(c, h, self.max_size[2] - w)
        return padded


class AlignCollate(object):
    def __init__(self, height=32, width=100, keep_ratio=False):
        self.height = height
        self.width = width
        self.keep_ratio = keep_ratio

    def __call__(self, batch):
        batch = filter(lambda x: x is not None, batch)
        images, labels = zip(*batch)

        if self.keep_ratio:
            resized_max_w = self.width
            input_channel = 3 if images[0].mode == 'RGB' else 1
            transform = NormalizePad((input_channel, self.height, resized_max_w))

            resized_images = []
            for image in images:
                w, h = image.size
                ratio = w / float(h)
                if math.ceil(self.height * ratio) > self.width:
                    resized_w = self.width
                else:
                    resized_w = math.ceil(self.height * ratio)

                resized = image.resize((resized_w, self.height), Image.BICUBIC)
                resized_images.append(transform(resized))

            image_tensors = torch.cat([t.unsqueeze(0) for t in resized_images], 0)
        else:
            transform = ResizeNormalize((self.width, self.height))
            image_tensors = [transform(image) for image in images]
            image_tensors = torch.cat([t.unsqueeze(0) for t in image_tensors], 0)

        return image_tensors, labels


class RandomSequentialSampler(Sampler):
    def __init__(self, data_, batch_size):
        self.num_samples = len(data_)
        self.batch_size = batch_size

    def __iter__(self):
        num_batch = len(self) // self.batch_size
        tail = len(self) % self.batch_size  # deal with tail case
        index = torch.LongTensor(len(self)).fill_(0)
        for i in range(num_batch):
            random_start = random.randint(0, len(self) - self.batch_size)
            batch_idx = random_start + torch.range(0, self.batch_size - 1)
            index[i * self.batch_size:(i + 1) * self.batch_size] = batch_idx

        if tail:
            random_start = random.randint(0, len(self) - self.batch_size)
            tail_idx = random_start + torch.range(0, tail - 1)
            index[(i + 1) * self.batch_size:] = tail_idx
        return iter(index)

    def __len__(self):
        return self.num_samples


class LMDBDataset(Dataset):
    def __init__(self, config, root=None, transform=None, target_transform=None):
        self.config = config
        self.root = root
        self.transform = transform
        self.target_transform = target_transform
        self.env = lmdb.open(self.root, max_readers=32, readonly=True, lock=False, readahead=False, meminit=False)
        if not self.env:
            print(f'cannot create LMDB from {self.root}')
            sys.exit(0)

        with self.env.begin(write=False) as txn:
            num_samples = int(txn.get('num-samples'.encode()))
            self.num_samples = num_samples

            self.filtered_idx_ = []
            for index in range(self.num_samples):
                index += 1
                label_key = f'label-{index}'.encode()
                label = txn.get(label_key).decode('utf-8')

                if len(label) > self.config['batch_max_len']:
                    continue
                out_of_char = f'[^{self.config["character"]}]'  # remove unusual character -> future updates for special vn character
                if re.search(out_of_char, label.lower()):
                    continue
                self.filtered_idx_.append(index)

            self.num_samples = len(self.filtered_idx_)

    def __len__(self):
        return self.num_samples

    def __getitem__(self, index):
        assert index <= len(self), 'index range error'
        index = self.filtered_idx_[index]

        with self.env.begin(write=False) as txn:
            img_key = 'image-{index}'.encode()
            imgbuf = txn.get(img_key)

            buf = six.BytesIO()
            buf.write(imgbuf)
            buf.seek(0)
            try:
                if self.config[' rgb ']:
                    img = Image.open(buf).convert('RGB')  # for color image
                else:
                    img = Image.open(buf).convert('L')

            except IOError:
                print(f'Corrupted image for {index}')
                # remove dummy images since it is unnecessary
                return self[index + 1]

            if self.transform is not None:
                img = self.transform(img)

            label_key = f'label-{index}'.encode()
            label = txn.get(label_key).decode('utf-8')

            # We only train and evaluate on alphanumerics (or pre-defined character set in train.py)
            out_of_char = f'[^{self.config["character"]}]'
            label = re.sub(out_of_char, '', label)

        return (img, label)
