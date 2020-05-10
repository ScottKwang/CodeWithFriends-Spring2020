# LeapCV

Hello there, I'm Cipulot and you're in the LeapCV repository!
This project is a sort of baseline for future custom projects that needs a way to detect faces and provide a touch-less interface.

## General functions
Basicaly this repo gives you the ability to:
* detect when a person is approaching the terminal through facial detection
* detect if this particular person is wearing a facemask (you know....just to be on the safe side)
* provide a basic touch-less input interface in order to minimize surface contact with the skin

Here's a quick face mask detection example :

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapCV/gifs/gif_facemask_detection_test.gif)

And a condition based motion sensor data gathering:

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapCV/gifs/gif_nomask_mask_doorbell_example.gif)

## Tech used
For the facial detection I'm using Opencv. The facemask cascade is custom built for this project therefore it can (surelly) have some flaws.
The input interface is based on the [Leap Motion](https://www.ultraleap.com/datasheets/Leap_Motion_Controller_Datasheet.pdf) gesture sensor with ad-hoc compiled files in order to make it work with Python3 (since it's only Python2 compatible out of the box).

## Built with
* [Python 3.7.7](https://www.python.org/downloads/release/python-377/) - Win64
* [Leap Motion v4 SDK](https://developer.leapmotion.com/setup/desktop) - Standard Download

### Note
In order to make the Leap Motion Python library to work with Python3 (64bit for now) you need to use the provided pre-compiled files and put them in your installation path in the folders that are named as the one that are provided. You can find those in the "LM Python3 files" folder. Note that these files are a direct recompilation of the Python2 compatible scripts and not fully tested.

## Known issues
* The Face and Facemask detection can be very cpu dependent and can cause unexpected behaviour even in good specs systems. The result is that even if the Leap Motion sensor is connected, the data stream will be temporarily interrupted due to the heavy work put on the cpu by openCV. (tested on i7-1065G7, 16GB ram). A a contributing cause can be my implementation.
* Sometimes the Leap Motion sensor cannot be accessed by the Python interface and a management service restart must be performed to fix it (this applies even if "normal" Leap Motion enabled apps are working just fine). Probabily due to a lack of checks function in the ported library.

## Possible fixes and workarounds
* Resource usage: a solution might be to accelerate the workflow of openCV using a gpu...
                  A workarounds can be to split the work in phases: first enable opencv detection then, if a condition is satisfied, disable the detection functions and move forward with the motion sensor stuff
* Leap Motion access: patchable with a function that restarts automatically the service if the connection fails too many times in a row.
                      In the long term a proper porting of the entire library could end up giving an inside view on the problem (maybe?)
## Reference 
If you're interested looking into the Face Mask detection stuff take a look at this paper (in that you can find a GitHub Repo with a lot of training images and links to positive/negative images databases):
[Masked Face Recognition Dataset and Application](https://arxiv.org/pdf/2003.09093.pdf)
