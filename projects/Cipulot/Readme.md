# LeapMask

Hello there, I'm Cipulot and you're in the LeapMask repository!
This project is a sort of baseline for future custom projects that needs a way to detect faces and provide a touch-less interface.

## General functions
Basicaly this repo gives you the ability to:
* detect when a person is approaching the terminal through facial detection
* detect if this particular person is wearing a facemask
* provide a basic touch-less input interface in order to minimize surface contact with the skin

Here's a quick face mask detection example :

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/gifs/gif_facemask_detection_test.gif)

And a condition based motion sensor data gathering:

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/gifs/gif_nomask_mask_doorbell_example.gif)

## Tech used
For the facial detection a custom model was built and was tested with different implementations.
The first tests were based on an haar cascade in OpenCV (you can see the result in the above gifs) but I quickly realized that this implementation was too much resource hungry and slow (you can test it if you want with this [script](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/OpenCV_only_old.py) and the "old" model). Therefore I've trained another model with Tensorflow, this time with a set of both real and computer generated positive images (source is below and a couple of hundreds taken by myself both via Google Images and in my local area). This has proven to be effective in reducing the narrowness in detection that a "one type only" approach can introduce.

The input interface is based on the [Leap Motion](https://www.ultraleap.com/datasheets/Leap_Motion_Controller_Datasheet.pdf) gesture sensor with ad-hoc compiled files in order to make it work with Python3 (since it's only Python2 compatible out of the box).

## Built with
* [Python 3.7.7](https://www.python.org/downloads/release/python-377/) - Win64
* [Tensorflow 2.2](https://www.tensorflow.org/install) - GPU used
* [Leap Motion v4 SDK](https://developer.leapmotion.com/setup/desktop) - Standard Download

### Note
In order to make the Leap Motion Python library to work with Python3 (64bit for now) you need to use the provided pre-compiled files and put them in your installation path in the folders that are named as the one that are provided. You can find those in the "LM Python3 files" folder. Note that these files are a direct recompilation of the Python2 compatible scripts and not fully tested.

## Specifications
This project was tested on the following configuration:
* i7-1065G7 (stock)
* 16 GB Ram
* NVIDIA GeForce MX250 (2GB, driver version 445.87)

## Known issues
* Sometimes the Leap Motion sensor cannot be accessed by the Python interface and a management service restart must be performed to fix it (this applies even if "normal" Leap Motion enabled apps are working just fine). This might be caused, probabily, by a lack of checks functions in the ported library.

## Possible fixes and workarounds
* Leap Motion access: patchable with a function that restarts automatically the service if the connection fails too many times in a row.
                      In the long term a proper porting of the entire library could end up giving an inside view on the problem (maybe?)
## Reference 
If you're interested looking into the Face Mask detection stuff take a look at this paper (in that you can find a GitHub Repo with a lot of training images and links to positive/negative images databases):
[Masked Face Recognition Dataset and Application](https://arxiv.org/pdf/2003.09093.pdf)
