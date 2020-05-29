from collections.abc import Iterable
from itertools import product

import torch

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')


# https://github.com/meijieru/crnn.pytorch/blob/master/utils.py
class CTCLabelConverter(object):
    #  Convert between text-label and text-index

    def __init__(self, character):
        # character (str): set of the possible characters.
        dict_character = list(character)

        self.dict = {}
        for i, char in enumerate(dict_character):
            # NOTE: 0 is reserved for 'blank' token required by CTCLoss
            self.dict[char] = i + 1

        self.character = ['[blank]'] + dict_character  # dummy '[blank]' token for CTCLoss (index 0)

    def encode(self, text, batch_max_len=25):
        # convert text-label into text-index.
        length = [len(s) for s in text]
        text = ''.join(text)
        text = [self.dict[char] for char in text]

        return (torch.IntTensor(text), torch.IntTensor(length))

    def decode(self, text, length):
        # convert text-index into text-label.
        texts = []
        index = 0
        for l in length:
            t = text[index:index + l]

            char_list = []
            for i in range(l):
                if t[i] != 0 and (not (i > 0 and t[i - 1] == t[i])):  # removing repeated characters and blank.
                    char_list.append(self.character[t[i]])
            text = ''.join(char_list)

            texts.append(text)
            index += l
        return texts


class AttnLabelConverter(object):
    # Convert between text-label and text-index

    def __init__(self, character, sep=None):
        # character (str): set of the possible characters.
        # [GO] for the start token of the attention decoder. [s] for end-of-sentence token.
        self.dict = {}
        list_token = ['[GO]', '[s]']  # ['[s]','[UNK]','[PAD]','[GO]']
        list_character = list(character)
        self.character = list_token + list_character
        self.sep = sep
        if self.sep is not None:
            # MORAN is just a STUPID paper tbh if it works it works
            self.character = character.split(sep)
        for i, char in enumerate(self.character):
            # print(i, char)
            self.dict[char] = i

    # TODO: add scan() functionality to remove unknown token
    def encode(self, text, batch_max_len=25):
        # convert text-label into text-index.
        if self.sep is not None:
            # MORAN mode
            # already included the separator in the character list for some fucking reason the author who wrote MORAN's paper is literally on crack
            if isinstance(text, str):
                text = [self.dict[char] for char in text]
                length = [len(text)]
            elif isinstance(text, Iterable):
                length = [len(s) for s in text]
                text = ''.join(text)
                text, _ = self.encode(text)
            return (torch.LongTensor(text), torch.LongTensor(length))
        else:
            length = [len(s) + 1 for s in text]  # +1 for [s] at end of sentence.
        # batch_max_len = max(length) # this is not allowed for multi-gpu setting
        batch_max_len += 1
        # additional +1 for [GO] at first step. batch_text is padded with [GO] token after [s] token.
        batch_text = torch.LongTensor(len(text), batch_max_len + 1).fill_(0)
        for i, t in enumerate(text):
            text = list(t)
            text.append('[s]')
            text = [self.dict[char] for char in text]
            batch_text[i][1:1 + len(text)] = torch.LongTensor(text)  # batch_text[:, 0] = [GO] token
            return (batch_text.to(device), torch.IntTensor(length).to(device))

    def decode(self, text, length):
        # convert text-index into text-label.
        if self.sep is not None:
            # MORAN : this is not index, rather a encoded text
            if length.numel() == 1:
                length = length[0]
                assert text.numel() == length, f'text with length {text.numel()} does not match with declared length {length}'
                return ''.join([self.character[i] for i in text])
            else:
                # batch mode
                assert text.numel() == length.sum(), f'text with length {text.numel()} does not match with declared length {length}'
                texts = []
                index = 0
                for i in range(length.numel()):
                    l = length[i]
                    texts.append(self.decode(text[index:index + l], torch.LongTensor([l])))
                index += l
            return texts
        else:
            # text here is a text_idx <list>
            texts = []
            for index, l in enumerate(length):
                text = ''.join([self.character[i] for i in text[index, :]])
                texts.append(text)
            return texts


class Averager(object):
    # Compute average for torch.Tensor, used for loss average.

    def __init__(self):
        self.reset()

    def add(self, v):
        count = v.data.numel()
        v = v.data.sum()
        self.n_count += count
        self.sum += v

    def reset(self):
        self.n_count = 0
        self.sum = 0

    def val(self):
        res = 0
        if self.n_count != 0:
            res = self.sum / float(self.n_count)
        return res


# implements levenshtein edit-distance
# lev a,b(|a|,|b|) == max(|a|,|b|) if min(|a|,|b|)== 0 else min([lev a,b(|a|-1,|b|)+1], [lev a,b(|a|,|b|-1)+1], [lev a,b(|a|-1, |b|-1)+1])
# iter through lev to check whether insertion/substitution/deletion
def step(lev, i, j, s1, s2, subs=1):
    c1, c2 = s1[i - 1], s2[j - 1]
    # skips the first two character in s1, s2
    a1 = lev[i - 1][j] + 1
    a2 = lev[i][j - 1] + 1
    r = lev[i - 1][j - 1] + (subs if c1 != c2 else 0)
    lev[i][j] = min(a1, a2, r)


def edit_distance(s1, s2, subs=1):
    l1, l2 = len(s1) + 1, len(s2) + 1
    lev = [[0] * l2 for _ in range(l1)]
    for i in range(l1):
        lev[i][0] = i
    for j in range(l2):
        lev[0][j] = j
    for i, j in product(range(l1 - 1), range(l2 - 1)):
        step(lev, i + 1, j + 1, s1, s2, subs=subs)
    return lev[l1 - 1][l2 - 1]
