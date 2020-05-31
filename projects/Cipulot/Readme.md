# LeapMask

Hello there, I'm Cipulot and you're in the LeapMask repository!
This project is a sort of baseline for future custom projects that needs a way to detect faces and provide a touch-less interface.

## Inspiration
I've been inspired by the global urge to detect if proper safety rules are followed by people in both public and private spaces. Being in university with this difficult situation I've found that the "real" problem will be the reopening of the spaces and the organization of classes, so I decided to put my experience into play.

## General functions
Basicaly this repo gives you the ability to:
* detect when a person is approaching the terminal through facial detection
* detect if this particular person is wearing a facemask
* provide a basic touch-less input interface in order to minimize surface contact with the skin (gesture recognition capable)

Here's a quick face mask detection example :

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/gifs/gif_facemask_detection_test.gif)

And a condition based motion sensor data gathering:

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/gifs/gif_nomask_mask_doorbell_example.gif)

## Tech used
For the facial detection a custom model was built and was tested with different implementations.
The first tests were based on an haar cascade in OpenCV (you can see the result in the above gifs) but I quickly realized that this implementation was too much resource hungry and slow (you can test it if you want with this [script](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/OpenCV_only_old.py) and the "old" model). Therefore I've trained another model with Tensorflow, this time with a set of both real and computer generated positive images (source is below and a couple of hundreds taken by myself both via Google Images and in my local area). This has proven to be effective in reducing the narrowness in detection that a "one type only" approach can introduce.

The input interface is based on the [Leap Motion](https://www.ultraleap.com/datasheets/Leap_Motion_Controller_Datasheet.pdf) gesture sensor with ad-hoc compiled files in order to make it work with Python3 (since it's only Python2 compatible out of the box).

## GUI
To use this Python script I've implemented a very simple GUI that:
* shows the camera feed with bounding boxes around detected features
* gives a text output of the feature
* indicates if a gesture has been recognized by the motion sensor

Feature label change:

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/gifs/Main_gui.gif)

Gesture and virtual press:

![](https://github.com/Cipulot/CodeWithFriends-Spring2020/blob/master/projects/Cipulot/LeapMask/gifs/Gesture_gui.gif)

## Built with
* [Python 3.7.7](https://www.python.org/downloads/release/python-377/) - Win64
* [PyQt5](https://pypi.org/project/PyQt5/) - For basic gui
* [Tensorflow 2.2](https://www.tensorflow.org/install) - GPU used
* [Leap Motion v2 SDK](https://developer.leapmotion.com/setup/desktop) - With [fix](https://forums.leapmotion.com/t/resolved-windows-10-fall-creators-update-bugfix/6585)

### Note
In order to make the Leap Motion Python library to work with Python3 (64bit for now) you need to use the provided pre-compiled files and put them in your installation path in the folders that are named as the one that are provided. You can find those in the "LM Python3 files" folder. Note that these files are a direct recompilation of the Python2 compatible scripts and not fully tested.
The Leap Motion V4 SDK also works but introduces a tremendous amount of bugs with Python 3 that prevents the connection in many occasions.

## Specifications
This project was tested on the following configuration:
* i7-1065G7 (stock)
* 16 GB Ram
* NVIDIA GeForce MX250 (2GB, driver version 445.87)

## Known issues
* Showing the frames in the gui with the feature detection enabled slows down the system drastically (at least for my system config). This could be solved using a more powerful GPU and furthermore optimizing the queue of the frames and the conversion between np.array frame type to QImage.
If you want to compare the fluidity of the video feed w/ and w/o the feature detection process you can set the flag ```feed_only```: ```True``` disables the detection, ```False``` enables it.
* When using dual (or more) monitor setup and you move the gui through the screens an error of scaling and ui element placement can occur. This is due to the fact that PyQt doesn't check natively if the dpi scale is changing (ex. from laptop pc to external monitor). This can cause the gui to look messed up if you try to run the app on a screen with different dpi scaling than the one that was designed on.

## Reference 
If you're interested looking into the Face Mask detection stuff take a look at this paper (in that you can find a GitHub Repo with a lot of training images and links to positive/negative images databases):
[Masked Face Recognition Dataset and Application](https://arxiv.org/pdf/2003.09093.pdf)
