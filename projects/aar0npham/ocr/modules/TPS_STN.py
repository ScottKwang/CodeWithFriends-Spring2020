import numpy as np
import torch
import torch.nn as nn

DEVICE = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
# direct ported from https://github.com/clovaai/deep-text-recognition-benchmark/blob/master/modules/transformation.py
# I have tried to implements this in tensorflow-keras but the result is not as good as shown below


class TPS_SpatialTransformerNetwork(nn.Module):
    """ Rectification Network of RARE, namely TPS based STN """
    def __init__(self, F, im_size, im_rectified, num_channels=1, device=DEVICE):
        # TPS based STN
        super(TPS_SpatialTransformerNetwork, self).__init__()
        self.F = F
        self.im_size = im_size
        self.im_rectified = im_rectified  # (I_r_height, I_r_width)
        self.num_channels = num_channels
        self.LocalizationNetwork = LocalizationNetwork(self.F, self.num_channels)
        self.GridGenerator = GridGenerator(self.F, self.im_rectified, device=device)

    def forward(self, inputs):
        outputs = self.LocalizationNetwork(inputs)  # batch_size x K x 2
        build_P_prime = self.GridGenerator.build_P_prime(outputs)  # batch_size x n (= I_r_width x I_r_height) x 2
        build_P_prime_reshape = build_P_prime.reshape([build_P_prime.size(0), self.im_rectified[0], self.im_rectified[1], 2])

        outputs = nn.functional.grid_sample(inputs, grid=build_P_prime_reshape, padding_mode='border', align_corners=True)

        return outputs


class LocalizationNetwork(nn.Module):
    """ Localization Network of RARE, which predicts C' (K x 2) from I (I_width x I_height) """
    def __init__(self, F, num_channels):
        super(LocalizationNetwork, self).__init__()
        self.F = F
        self.num_channels = num_channels
        self.conv = nn.Sequential(
            nn.Conv2d(in_channels=self.num_channels, out_channels=64, kernel_size=3, stride=1, padding=1, bias=False),
            nn.BatchNorm2d(64),
            nn.ReLU(True),
            nn.MaxPool2d(2, 2),  # batch_size x 64 x I_height/2 x I_width/2
            nn.Conv2d(64, 128, 3, 1, 1, bias=False),
            nn.BatchNorm2d(128),
            nn.ReLU(True),
            nn.MaxPool2d(2, 2),  # batch_size x 128 x I_height/4 x I_width/4
            nn.Conv2d(128, 256, 3, 1, 1, bias=False),
            nn.BatchNorm2d(256),
            nn.ReLU(True),
            nn.MaxPool2d(2, 2),  # batch_size x 256 x I_height/8 x I_width/8
            nn.Conv2d(256, 512, 3, 1, 1, bias=False),
            nn.BatchNorm2d(512),
            nn.ReLU(True),
            nn.AdaptiveAvgPool2d(1)  # batch_size x 512
        )

        self.localization_fc1 = nn.Sequential(nn.Linear(512, 256), nn.ReLU(True))
        self.localization_fc2 = nn.Linear(256, self.F * 2)

        # Init fc2 in LocalizationNetwork
        self.localization_fc2.weight.data.fill_(0)
        # see RARE paper Fig. 6 (a)
        # init localization_fc2 with coordinates placeholder
        self.localization_fc2.bias.data = torch.from_numpy(
            np.concatenate([
                np.stack([np.linspace(-1., 1., int(F / 2)), np.linspace(0., -1., int(F / 2))], axis=1),
                np.stack([np.linspace(-1., 1., int(F / 2)), np.linspace(1., 0., int(F / 2))], axis=1)
            ], axis=0)).float().view(-1) # yapf: disable

    def forward(self, inputs):
        # inputs: inputs image [batch_size x num_channels x im_height x im_width]
        # outputs predicted coordinates of fiducial points for input batch [batch_size x F x 2]
        batch_size = inputs.size(0)  # yapf: enable
        features = self.conv(inputs).view(batch_size, -1)
        outputs = self.localization_fc2(self.localization_fc1(features)).view(batch_size, self.F, 2)
        return outputs


class GridGenerator(nn.Module):
    """ Grid Generator of RARE, which produces P_prime by multipling T with P """
    def __init__(self, F, im_rectified, device=DEVICE):
        """ Generate P_hat and inv_delta_C for later """
        super(GridGenerator, self).__init__()
        self.eps = 1e-6
        self.device = device
        self.I_r_height, self.I_r_width = im_rectified
        self.F = F
        self.C = self._build_C(self.F)  # F x 2
        self.P = self._build_P(self.I_r_width, self.I_r_height)
        self.register_buffer("inv_delta_C", torch.Tensor(self._build_inv_delta_C(self.F, self.C)).float())  # F+3 x F+3
        self.register_buffer("P_hat", torch.Tensor(self._build_P_hat(self.F, self.C, self.P)).float())  # n x F+3
        ## for fine-tuning with different image width, you may use below instead of self.register_buffer
        # self.inv_delta_C = torch.Tensor(self._build_inv_delta_C(self.F, self.C)).float().cuda()  # F+3 x F+3
        # self.P_hat = torch.Tensor(self._build_P_hat(self.F, self.C, self.P)).float().cuda()  # n x F+3

    def _build_C(self, F):
        """ Return coordinates of fiducial points in I_r; C """
        C_x = np.linspace(-1.0, 1.0, int(F / 2))
        C_yt = -1 * np.ones(int(F / 2))
        C_yb = np.ones(int(F / 2))
        C = np.concatenate([np.stack([C_x, C_yt], axis=1), np.stack([C_x, C_yb], axis=1)], axis=0)
        return C  # F x 2

    def _build_inv_delta_C(self, F, C):
        """ Return inv_delta_C which is needed to calculate T """
        hat_C = np.zeros((F, F), dtype=float)  # F x F
        for i in range(0, F):
            for j in range(i, F):
                r = np.linalg.norm(C[i] - C[j])
                hat_C[i, j] = r
                hat_C[j, i] = r
        np.fill_diagonal(hat_C, 1)
        hat_C = (hat_C**2) * np.log(hat_C)
        # print(C.shape, hat_C.shape)
        delta_C = np.concatenate(  # F+3 x F+3
            [
                np.concatenate([np.ones((F, 1)), C, hat_C], axis=1),  # F x F+3
                np.concatenate([np.zeros((2, 3)), np.transpose(C)], axis=1),  # 2 x F+3
                np.concatenate([np.zeros((1, 3)), np.ones((1, F))], axis=1)  # 1 x F+3
            ],
            axis=0)
        inv_delta_C = np.linalg.inv(delta_C)
        return inv_delta_C  # F+3 x F+3

    def _build_P(self, I_r_width, I_r_height):
        I_r_grid_x = (np.arange(-I_r_width, I_r_width, 2) + 1.0) / I_r_width  # self.I_r_width
        I_r_grid_y = (np.arange(-I_r_height, I_r_height, 2) + 1.0) / I_r_height  # self.I_r_height
        P = np.stack(  # self.I_r_width x self.I_r_height x 2
            np.meshgrid(I_r_grid_x, I_r_grid_y), axis=2)
        return P.reshape([-1, 2])  # n (= self.I_r_width x self.I_r_height) x 2

    def _build_P_hat(self, F, C, P):
        n = P.shape[0]  # n (= self.I_r_width x self.I_r_height)
        P_tile = np.tile(np.expand_dims(P, axis=1), (1, F, 1))  # n x 2 -> n x 1 x 2 -> n x F x 2
        C_tile = np.expand_dims(C, axis=0)  # 1 x F x 2
        P_diff = P_tile - C_tile  # n x F x 2
        rbf_norm = np.linalg.norm(P_diff, ord=2, axis=2, keepdims=False)  # n x F
        rbf = np.multiply(np.square(rbf_norm), np.log(rbf_norm + self.eps))  # n x F
        P_hat = np.concatenate([np.ones((n, 1)), P, rbf], axis=1)
        return P_hat  # n x F+3

    def build_P_prime(self, batch_C_prime):
        """ Generate Grid from batch_C_prime [batch_size x F x 2] """
        batch_size = batch_C_prime.size(0)
        batch_inv_delta_C = self.inv_delta_C.repeat(batch_size, 1, 1)
        batch_P_hat = self.P_hat.repeat(batch_size, 1, 1)
        batch_C_prime_with_zeros = torch.cat((batch_C_prime, torch.zeros(batch_size, 3, 2).float().to(self.device)), dim=1)  # batch_size x F+3 x 2
        batch_T = torch.bmm(batch_inv_delta_C, batch_C_prime_with_zeros)  # batch_size x F+3 x 2
        batch_P_prime = torch.bmm(batch_P_hat, batch_T)  # batch_size x n x 2
        return batch_P_prime  # batch_size x n x 2
