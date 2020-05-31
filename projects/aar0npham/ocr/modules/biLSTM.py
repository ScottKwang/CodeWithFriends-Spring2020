import numpy as np
import torch
import torch.nn as nn
import torch.nn.functional as F

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')


class BidirectionalLSTM(nn.Module):
    def __init__(self, nIn, nHidden, nOut, dropout=None):
        super(BidirectionalLSTM, self).__init__()
        # batch_first=True-> [b, seq, feats]
        self.dropout = dropout
        if self.dropout is not None:
            self.rnn = nn.LSTM(nIn, nHidden, bidirectional=True)
            self.embedding = nn.Linear(nHidden * 2, nOut)
        else:
            self.rnn = nn.LSTM(nIn, nHidden, bidirectional=True, batch_first=True)
            self.linear = nn.Linear(nHidden * 2, nOut)

    def forward(self, inputs):
        if torch.cuda.is_available():
            self.rnn.flatten_parameters()
        # [b, T, nIn] -> [b, T, (2*nHidden)]
        recurrent, _ = self.rnn(inputs)
        if self.dropout is not None:
            T, b, h = recurrent.size()
            t_rec = recurrent.view(T * b, h)
            outputs = self.embedding(t_rec)  # [T*b, nOut]
            outputs = outputs.view(T, b, -1)
        else:
            outputs = self.linear(recurrent)  # returns [b, T, nOut]
        return outputs
