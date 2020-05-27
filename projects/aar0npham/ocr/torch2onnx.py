import os
import sys

import cv2
import numpy as np
import onnx
import onnxruntime as ort
import torch
import torch.nn as nn
import torch.onnx
import yaml

from net import CRAFT, CRNN

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
with open(os.path.join('config.yml'), 'r') as f:
    config = yaml.safe_load(f)
os.makedirs(config['onnx_path'], exist_ok=True)
det, rec = config['pipeline'].split('-')
FUNCTION = {'CRAFT': CRAFT, 'CRNN': CRNN}


# TODO: issues at nn.functional.grid_sample as it is not supported ,yet.
def torch2onnx(model_name, target_root=config['onnx_path'], debug=False):
    print(f'processing {model_name}...')
    if not os.path.exists(os.path.join(target_root, f'{model_name}.onnx')):
        assert model_name in [
            'CRAFT',
            'CRNN',
        ], f'supports CRAFT, CRNN, got {model_name} instead, check `config.yml`'
        model = FUNCTION[f'{model_name}']()
        model.load()
        ocr = model.net
        converter = model.converter
        num_channels = 3 if model_name == 'CRAFT' else 1
        # dummy inputs
        inputs = torch.randn(1, num_channels, 244, 244, requires_grad=True).to(device)
        if model_name == 'CRAFT':
            lname = (inputs)
            outputs = ocr(inputs)
        else:
            text_pred = torch.LongTensor(1, config['batch_max_len'] + 1).fill_(0).to(device)
            lname = (inputs, text_pred)
            if config['prediction'] == 'Attention':
                outputs = ocr(inputs, text_pred, training=False)
            else:
                outputs = ocr(inputs, text_pred)
        # yapf: disable
        torch.onnx.export(ocr, lname, os.path.join(target_root, f'{model_name}.onnx'), export_params=True, verbose=True,
                          opset_version=11, do_constant_folding=True, input_names=['inputs'], output_names=['outputs'])
        # yapf: enable
        if debug:
            onnx_model = onnx.load(os.path.join(target_root, f'{model_name}.onnx'))
            onnx.checker.check_model(onnx_model)
            # onnx.helper.printable_graph(onnx_model.graph)
        print(f'finished processing {model_name}.')
    else:
        print(f'{model_name}.onnx already exists, continue.')


def inference(impath, onnx_path, mode='detect'):
    sess = ort.InferenceSession(onnx_path)
    image = cv2.imread(impath)


def to_numpy(tensor):
    try:
        if tensor.requires_grad:
            return tensor.detach().cpu().numpy()
    except:
        return tensor.cpu().numpy()


# traced.save('converted_models/CRNN.pt')

if __name__ == '__main__':
    for k, _ in FUNCTION.items():
        torch2onnx(k)
