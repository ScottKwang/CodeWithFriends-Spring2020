from abc import ABC

import torch
import torch.nn as nn

from modules import (Attention, BidirectionalLSTM, ResNet_FeatureExtractor, TPS_SpatialTransformerNetwork, UpConv, init_weights, vgg16_bn)


class VGG_UNet(nn.Module):
    def __init__(self, pretrained=False, freeze=False):
        super(VGG_UNet, self).__init__()
        # Base network
        self.basenet = vgg16_bn(pretrained, freeze)
        # U network
        self.upconv1 = UpConv(1024, 512, 256)
        self.upconv2 = UpConv(512, 256, 128)
        self.upconv3 = UpConv(256, 128, 64)
        self.upconv4 = UpConv(128, 64, 32)

        num_class = 2
        self.conv_cls = nn.Sequential(
            nn.Conv2d(32, 32, kernel_size=3, padding=1),
            nn.ReLU(inplace=True),
            nn.Conv2d(32, 32, kernel_size=3, padding=1),
            nn.ReLU(inplace=True),
            nn.Conv2d(32, 16, kernel_size=3, padding=1),
            nn.ReLU(inplace=True),
            nn.Conv2d(16, 16, kernel_size=1),
            nn.ReLU(inplace=True),
            nn.Conv2d(16, num_class, kernel_size=1),
        )

        init_weights(self.upconv1.modules())
        init_weights(self.upconv2.modules())
        init_weights(self.upconv3.modules())
        init_weights(self.upconv4.modules())
        init_weights(self.conv_cls.modules())

    def forward(self, inputs):
        # basenet
        sources = self.basenet(inputs)

        # UNet
        y = torch.cat([sources[0], sources[1]], dim=1)
        y = self.upconv1(y)

        y = nn.functional.interpolate(y, size=sources[2].size()[2:], mode='bilinear', align_corners=False)
        y = torch.cat([y, sources[2]], dim=1)
        y = self.upconv2(y)

        y = nn.functional.interpolate(y, size=sources[3].size()[2:], mode='bilinear', align_corners=False)
        y = torch.cat([y, sources[3]], dim=1)
        y = self.upconv3(y)

        y = nn.functional.interpolate(y, size=sources[4].size()[2:], mode='bilinear', align_corners=False)
        y = torch.cat([y, sources[4]], dim=1)
        feature = self.upconv4(y)

        y = self.conv_cls(feature)

        return y.permute(0, 2, 3, 1), feature


class CRNNet(nn.Module):
    # TPS - ResNet - biLSTM - Attn/CTC
    def __init__(self, config, device):
        super(CRNNet, self).__init__()
        self.config = config
        self.stages = {'Trans': config['transform'], 'Feat': config['backbone'], 'Seq': config['sequence'], 'Pred': config['prediction']}

        if config['transform'] == 'TPS':
            self.Transformation = TPS_SpatialTransformerNetwork(F=config['num_fiducial'],
                                                                im_size=(config['height'], config['width']),
                                                                im_rectified=(config['height'], config['width']),
                                                                num_channels=config['input_channel'],
                                                                device=device)
        else:
            print('No tps specified')
        if config['backbone'] == 'ResNet':
            self.FeatureExtraction = ResNet_FeatureExtractor(config['input_channel'], config['output_channel'])
        else:
            raise Exception('No backbone module specified')
        self.FeatureExtraction_output = config['output_channel']  # int(imgH/16-1) * 512
        self.AdaptiveAvgPool = nn.AdaptiveAvgPool2d((None, 1))  # Transform final (imgH/16-1) -> 1

        if config['sequence'] == 'biLSTM':
            self.SequenceModeling = nn.Sequential(BidirectionalLSTM(self.FeatureExtraction_output, config['hidden_size'], config['hidden_size']),
                                                  BidirectionalLSTM(config['hidden_size'], config['hidden_size'], config['hidden_size']))
            self.SequenceModeling_output = config['hidden_size']
        else:
            print('No sequence module specified')
            self.SequenceModeling_output = self.FeatureExtraction_output

        if config['prediction'] == 'CTC':
            self.Prediction = nn.Linear(self.SequenceModeling_output, config['num_classes'])
        elif config['prediction'] == 'Attention':
            self.Prediction = Attention(self.SequenceModeling_output, config['hidden_size'], config['num_classes'], device=device)
        else:
            raise Exception('prediction needs to be either CTC or attention-based sequence prediction')

    def forward(self, inputs, text, training=True):
        if not self.stages['Trans'] == "None":
            inputs = self.Transformation(inputs)
        visual_feature = self.FeatureExtraction(inputs)
        visual_feature = self.AdaptiveAvgPool(visual_feature.permute(0, 3, 1, 2))  # [b, c, h, w] -> [b, w, c, h]
        visual_feature = visual_feature.squeeze(3)
        if self.stages['Seq'] == 'biLSTM':
            contextual_feature = self.SequenceModeling(visual_feature)
        else:
            contextual_feature = visual_feature  # for convenience. this is NOT contextually modeled by BiLSTM
        if self.stages['Pred'] == 'CTC':
            prediction = self.Prediction(contextual_feature.contiguous())
        else:
            prediction = self.Prediction(contextual_feature.contiguous(), text, training, batch_max_len=self.config['batch_max_len'])

        return prediction


class Placeholder(ABC):
    def __init__(self, state_dict=None):
        self.net = None
        self.cuda = False
        self.converter = None
        self.transformer = None
        self.device = None

    def toContainer(self, docker=False):
        pass

    def load(self):
        pass

    def process(self, image):
        pass
