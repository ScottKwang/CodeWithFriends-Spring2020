import numpy as np
import torch
import torch.nn as nn
import torch.nn.functional as F

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')


# TODO: parse dynamic tensor with torch.jit.trace #L20,L29,L30
class Attention(nn.Module):
    def __init__(self, nIn, nHidden, num_classes, device='cuda'):
        super(Attention, self).__init__()
        self.device = device
        self.attention_cell = AttentionCell(nIn, nHidden, num_classes, device=device)
        self.nHidden = nHidden
        self.num_classes = num_classes
        self.generator = nn.Linear(nHidden, num_classes)

    def char2onehot(self, input_char, onehot_dim=38):
        input_char = input_char.unsqueeze(1)
        batch_size = input_char.size(0)
        onehot = torch.FloatTensor(batch_size, onehot_dim).zero_().to(self.device)
        return onehot.scatter_(1, input_char, 1).to(self.device)

    def forward(self, inputs, text, training=True, batch_max_len=25):
        # inputs is the hidden state of lstm [batch, num_steps, num_classes]
        # text index of each image [batch_size x (max_len+1)] -> include [GO] token
        # return probability distribution of each step [batch_size, num_steps, num_classes]
        batch_size = inputs.size(0)
        num_steps = batch_max_len + 1
        h = torch.FloatTensor(batch_size, num_steps, self.nHidden).fill_(0).to(self.device)
        nh = (torch.FloatTensor(batch_size, self.nHidden).fill_(0).to(self.device), torch.FloatTensor(batch_size,
                                                                                                      self.nHidden).fill_(0).to(self.device))
        if training:
            for i in range(num_steps):
                onehotChar = self.char2onehot(text[:, i], onehot_dim=self.num_classes)
                nh, alpha = self.attention_cell(
                    nh, inputs, onehotChar)  # nh: decoder's hidden at s_(t-1), inputs: encoder's hidden H, onehotChar: onehot(y_(t_1))
                h[:, i, :] = nh[0]  # -> lstm hidden index (0:hidden, 1:cell)
            probs = self.generator(h)
        else:
            targets = torch.LongTensor(batch_size).fill_(0).to(self.device)
            probs = torch.FloatTensor(batch_size, num_steps, self.num_classes).fill_(0).to(self.device)

            for i in range(num_steps):
                onehotChar = self.char2onehot(targets, onehot_dim=self.num_classes)
                nh, alpha = self.attention_cell(nh, inputs, onehotChar)
                probs_step = self.generator(nh[0])
                probs[:, i, :] = probs_step
                _, n_in = probs_step.max(1)  # -> returns next inputs
                targets = n_in

        return probs


class AttentionCell(nn.Module):
    # TODO: Fixes batch_first in LSTM to fix foward() difference when processing i2h
    def __init__(self, nIn, nHidden, num_embeddings, device='cuda'):
        super(AttentionCell, self).__init__()
        self.device = device
        self.nHidden = nHidden
        self.i2h = nn.Linear(nIn, nHidden, bias=False)
        self.h2h = nn.Linear(nHidden, nHidden)
        self.score = nn.Linear(nHidden, 1, bias=False)
        self.rnn = nn.LSTMCell(nIn + num_embeddings, nHidden)

    def forward(self, h, feats, cur_embed, test=False):
        # [b, num_steps, num_channels] -> [b, num_steps, nHidden]
        mh = dict()
        feats_ = self.i2h(feats.to(self.device))
        h_ = self.h2h(h[0].to(self.device)).unsqueeze(0)
        emit = self.score(torch.tanh(feats_ + h_)).to(self.device)
        alpha = F.softmax(emit, dim=1)  # nT x nB
        # context : batch x num_channels
        context = torch.bmm(alpha.permute(0, 2, 1), feats).squeeze(1).to(self.device)
        # batch x ( num_channels + num_embeddings )
        concat = torch.cat([context, cur_embed.to(self.device)], 1)
        for i, hs in enumerate(h):
            mh[i] = hs.to(self.device)
        nh = self.rnn(concat, tuple(v for _, v in mh.items()))
        return nh, alpha
