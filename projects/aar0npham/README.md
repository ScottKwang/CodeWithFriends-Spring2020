# lightly-ocr

[![CircleCI](https://circleci.com/gh/aar0npham/lightly-ocr/tree/master.svg?style=svg)](https://circleci.com/gh/aar0npham/lightly-ocr/tree/master)

lightly's backend - receipt to text :chart_with_downwards_trend:

OCR tasks can be found in `/ocr`, network-related can be found in `/ingress`


## table of content.
* [credits.](#credits)
* [requirements.](#requirements)
* [instruction.](#instruction)
* [develop.](#develop)
* [structure.](#structure)
* [todo.](#todo)
* [tldr.](#tldr)

## credits.
* [CRAFT-pytorch](https://github.com/clovaai/CRAFT-pytorch)
* [crnn.pytorch](https://github.com/meijieru/crnn.pytorch)

## requirements.
- recommends `pipenv` for virtualenv, refers to [pypa/pipenv](https://github.com/pypa/pipenv) for installation
- python `>=3.7`
- built with `pytorch`

## instruction.
- build local docker image with `(sudo) docker build -f [PATH of Dockerfile] -t aar0npham/lightly-ocr:latest ocr`, to run do `docker run [--gpus=all] aar0npham/lightly-ocr:latest`, for `--gpus=all` requires [nvidia-docker](https://github.com/NVIDIA/nvidia-docker)
- if you want to use MORAN please refer to [tree@66171c8058](https://github.com/aar0npham/lightly-ocr/tree/66171c80586537ae915938b2e92eb83c474cda79)
- Run `bash scripts/download_model.sh` to get the pretrained model
- to test the model do:
```python
python ocr/pipeline.py --img [IM_PATH]
```
- to train your own model refers to [tldr.](#tldr). Train files are located in `ocr/train/`

## develop.
- make sure the repository is up to date with ```git pull origin master```
- create a new branch __BEFORE__ working with ```git checkout -b feats-branch_name``` where `feats` is the feature you are working on and the `branch-name` is the directory containing that features. 
  
  e.g: `git checkout -b YOLO-ocr` means you are working on `YOLO` inside `ocr`
- make your changes as you wish
- ```git status``` to show your changes and then following with ```git add .``` to _stage_ your changes
- commit these changes with ```git commit -m "describe what you do here"```

<details>
  <summary>if you have more than one commit you should <i>rebase/squash</i> small commits into one</summary>

  - ```git status``` to show the amount of your changes comparing to _HEAD_: 
    
    ```Your branch is ahead of 'origin/master' by n commit.``` where `n` is the number of your commit 
  - ```git rebase -i HEAD~n``` to changes commit, __REMEMBER__ `-i`
  - Once you enter the interactive shell `pick` your first commit and `squash` all the following commits after that
  - after saving and exits edit your commit message once the new windows open describe what you did
  - more information [here](https://git-scm.com/docs/git-rebase)

</details></br>

- push these changes with ```git push origin feats-branch_name```, ```git branch``` to check which branch you are on
- then make a pull-request on github!

## structure.
overview in `src` as follows:
```bash
./
├── save_models/                 # location for model save
├── modules/                     # contains core file for setting up models
├── train/                       # code to train specific model
├── test/                        # contains unit testing file
├── tools/                       # contains tools to generate dataset/image processing etc.
├── Dockerfile                   # Dockerfile for creating container
├── requirements.txt             # contains modules using for import
├── additional_requirements.txt  # contains modules that is not included in the docker container
├── config.yml                   # config.yml 
├── convert.py                   # convert model to .onnx file format
├── model.py                     # contains model constructed from `modules`
├── net.py                       # contains detector and recognizer segment of OCR
├── server.py                    # run basic server with `flask`
├── pipeline.py                  # end-to-end OCR
└── torch2onnx.py                # conversion to .onnx (future use with onnx.js) _WIP_
```

## todo.
* [ ] define type for python function 
* [ ] autogenerate Dockerfile? → __features__
* [ ] add docstring, fixes `too-many-locals`
* [ ] [ingress](ingress/) controller
* [ ] custom ops for `torch.nn.functional.grid_sample`, refers to this [issues](https://github.com/onnx/onnx/issues/654). Notes and fixes are in [here](ocr/torch2onnx.py)
* [ ] rename state dict
* [ ] updates save weight to google drive (for local testing since circle have weight already)
* [x] ~~complete `__init__.py`~~
* [x] ~~added Dockerfile/CircleCI~~

<details>
<summary>
<a href="ocr/model.py#L9"><b>CRAFT</b></a>
</summary>
  
  * [ ] add `unit_test`
  * [ ] includes training loop (_under construction_)
</details>

<details>
<summary>
<a href="ocr/model.py#L64"><b>CRNN</b></a>
</summary>
  
  * [ ] add `unit_test`
  * [ ] process ICDAR2019 for eval sets in conjunction with MJSynth val data ⇒ reduce biases
  * [x] ~~fixes `batch_first` for AttentionCell in [sequence.py](ocr/modules/sequence.py)~~
  * [x] ~~transfer trained weight to fit with the model~~
  * [x] ~~fix image padding issues with [eval.py](ocr/recognizer/CRNN/tools/eval.py)~~
  * [x] ~~creates a general dataset and generator function for both reconition model~~
  * [x] ~~database parsing for training loop~~
  * [x] ~~__FIXME__: gradient vanishing when training~~
  * [x] ~~generates logs for each training session~~
  * [x] ~~add options for continue training~~
  * [x] ~~modules incompatible shapes~~
  * [x] ~~create lmdb as dataset~~
  * [x] ~~added [generator.py](ocr/recognizer/CRNN/tools/generator.py) to generate lmdb~~
  * [x] ~~merges valuation_fn into [train.py](ocr/recognizer/CRNN/train.py#L136)~~
</details>

## tldr. 

### [CRAFT.](ocr/net.py#L55)
[paper](https://arxiv.org/pdf/1904.01941.pdf) | [original implementation](https://github.com/clovaai/CRAFT-pytorch)

__architecture__: VGG16-Unet as backbone, with [UpConv](ocr/modules/vgg_bn.py#L23) return region/affinity score
* adopt from [ Faster R-CNN ](https://arxiv.org/pdf/1506.01497.pdf)

### [CRNN.](ocr/net.py#L211) 
[paper](https://arxiv.org/pdf/1507.05717.pdf) | [original implementation](https://github.com/bgshih/crnn)

__NOTES__:
* run ```ocr/tools/generator.py``` to create dataset, `ocr/train/crnn.py` to train the model
* model is under [model.py](ocr/model.py)

__architecture__: 4 stage CRNN includes:   
  * [ TPS based STN ](ocr/modules/TPS_STN.py) for image rectification
  * [ ResNet50 ](ocr/modules/resnet50v1.py) for feature extractor
  * [ Bidirectional LSTM ](ocr/modules/biLSTM.py) for sequence modeling
  * either CTC / [ Attention ](ocr/modules/attention.py) layer for sequence prediction


