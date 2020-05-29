import collections
import string
import sys
import unittest

import torch

from ocr.tools import recog_utils


def equal(a1, a2):
    if isinstance(a1, torch.Tensor):
        return a1.equal(a2)
    elif isinstance(a1, str):
        return a1 == a2
    elif isinstance(a1, collections.Iterable):
        res = True
        for (i, j) in zip(a1, a2):
            res = res and equal(i, j)
        return res
    else:
        return a1 == a2


class utils_test(unittest.TestCase):
    def check_converter(self):
        encoder = recog_utils.CTCLabelConverter(string.ascii_lowercase)

        res = encoder.encode('fifa')
        tar = (torch.IntTensor([6, 9, 6, 1]), torch.IntTensor([4]))
        self.assertTrue(equal(res, tar))

        # batch mode
        res = encoder.encode(['eff', 'ab'])
        tar = (torch.IntTensor([5, 6, 6, 1, 2, 3]), torch.IntTensor([3, 2]))
        self.assertTrue(equal(res, tar))

        result = encoder.decode(torch.IntTensor([6, 9, 6, 1]), torch.IntTensor([4]))
        target = 'fifa'
        self.assertTrue(equal(result, target))

        result = encoder.decode(torch.IntTensor([5, 5, 0, 1]), torch.IntTensor([4]))
        target = 'ea'
        self.assertTrue(equal(result, target))

        # raise Assertion error
        def f():
            result = encoder.decode(torch.IntTensor([5, 5, 0, 1]), torch.IntTensor([3]))

        self.assertRaises(AssertionError, f)

        # batch mode
        result = encoder.decode(torch.IntTensor([5, 6, 6, 1, 2, 3]), torch.IntTensor([3, 2]))
        target = ['eff', 'ab']
        self.assertTrue(equal(result, target))

    def check_averager(self):
        acc = recog_utils.Averager()
        acc.add(torch.Tensor([1, 2]))
        acc.add(torch.Tensor([[5, 6]]))
        assert acc.val() == 3.5


def __suite__():
    suite = unittest.TestSuite()
    suite.addTest(utils_test("check_converter"))
    suite.addTest(utils_test("check_averager"))
    return suite


if __name__ == '__main__':
    suite = __suite__()
    runner = unittest.TextTestRunner()
    runner.run(suite)
