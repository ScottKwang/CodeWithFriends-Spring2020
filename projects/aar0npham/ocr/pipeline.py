import argparse
import os
import time
from collections import OrderedDict

import cv2
import torch
import yaml

from net import CRAFT, CRNN

# Constant
DEVICE = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
with open(os.path.join(os.path.dirname(os.path.realpath(__file__)), 'config.yml'), 'r') as f:
    CONFIG = yaml.safe_load(f)


# remove module.* on model_save
def remove(key, re='module'):
    if re in key.split('.'):
        key = key[len(re) + 1:]
    return key


def renameStateDict(source, fn=remove, target=None):
    # target is the new path to save source, default: write back to source
    # fn: function to transfer new key
    if target is None:
        target = source

    state_dict = torch.load(source)
    res = OrderedDict()

    for k, v in state_dict.items():
        nk = fn(k)
        res[nk] = v

    torch.save(res, target)


def calcTime(fn=lambda x, *a, **kw: x(*a, **kw)):
    start = time.time()
    fn()
    return f'{fn} took {time.time()-start:.3f}s'


# begin pipeline
def prepModel(config=CONFIG, rename=False, docker=False):
    device = DEVICE if not docker else 'cpu'
    use_detector, use_recognizer = config['pipeline'].split('-')
    if use_detector == 'CRAFT':
        detector = CRAFT(device=device, docker=docker)
    else:
        raise AssertionError(f'only supported CRAFT atm. got {use_detector} instead')
    if use_recognizer == 'CRNN':
        recognizer = CRNN(device=device, docker=docker)
    else:
        raise AssertionError(f'only supports either CRNN or MORAN. got {use_recognizer} instead')
    if rename:
        for p in [detector.model_path, recognizer.model_path]:
            renameStateDict(p)
    # load from pretrained
    return detector, recognizer


def getText(image, detector, recognizer, write=True):
    res = []
    use_recognizer = CONFIG['pipeline'].split('-')[1]
    image = cv2.imread(image)  # use cv2 to read image
    with torch.no_grad():
        # detection
        roi = detector.process(image)

        # recognition
        for _, img in enumerate(roi):
            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
            if use_recognizer == 'CRNN':
                text, res_dict = recognizer.process(gray)
            else:
                raise ValueError(f'support CRNN only, got {use_recognizer} instead')
            res.append(text)
    if write:
        with open(os.path.join(os.path.dirname(os.path.relpath(__file__)), 'test', 'results.txt'), 'w') as test_result:
            for i in res:
                test_result.write(f'prediction: {i}\n')
            print(f'wrote results to {test_result.name}')
            test_result.close()
    torch.cuda.empty_cache()
    return res, res_dict


class serveModel:
    def __init__(self, config_file: str, thresh: int, docker: bool):
        self.docker = docker
        self.config_file = config_file
        self.loadConfig()
        self.thresh = thresh
        self.model_root = self.config['pretrained']
        self.loadModel()

    def loadConfig(self):
        with open(os.path.join(os.path.dirname(os.path.realpath(__file__)), self.config_file), 'r') as cf:
            self.config = yaml.safe_load(cf)

    def loadModel(self):
        self.detector, self.recognizer = prepModel(self.config, docker=self.docker)

    def predict(self, inputs: str):
        getRes = []
        _, res_dict = getText(inputs, self.detector, self.recognizer)
        for k, v in res_dict.items():
            if k > self.thresh:
                getRes.append(v)
        return getRes


# test the model out
if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--config', default='config.yml', help='path to config.yml, default is the same dir as pipeline')
    parser.add_argument('--thresh', default=0.7, help='threshold for confidence score')
    parser.add_argument('--img', required=True, help='image path for running ocr on')
    parser.add_argument('--debug', action='store_true', help='whether to run debug')
    opt = parser.parse_args()
    docker = bool(torch.cuda.is_available())
    model = serveModel(config_file=opt.config, thresh=opt.thresh, docker=docker)
    result = model.predict(opt.img)
