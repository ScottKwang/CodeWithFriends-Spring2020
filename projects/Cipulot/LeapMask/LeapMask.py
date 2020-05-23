#from __future__ import absolute_import, division, print_function, unicode_literals
import os
import sys
import io
import time
import cv2
import Leap
import serial
import imutils
import numpy as np
import threading
from threading import Thread
from Leap import SwipeGesture
from playsound import playsound
from imutils.video import VideoStream
'''
Tensorflow terminal messages level
  Level | Level for Humans | Level Description
 -------|------------------|------------------------------------
  0     | DEBUG            | [Default] Print all messages
  1     | INFO             | Filter out INFO messages
  2     | WARNING          | Filter out INFO & WARNING messages
  3     | ERROR            | Filter out all messages
  '''
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.applications.mobilenet_v2 import preprocess_input
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtGui import QImage, QPixmap, QMovie

# Flag to disable feature detection
feed_only = False

# UI object as global to enable access to if from anywhere
main_ui = None
AboutWindow = None

# Flag to indicate if Leap Motion controller is connected
Leap_connected = True

# Flag to indicate if Leap Motion controller is enabled
Leap_enabled = False

# Flag used to check if any thread raised an exception or exited by the user
Stopped = False

# Palm position vector
Palm_position = None

# Flag to indicate if the user "pressed" the virtual button
Pressed = False

# Audio files path
stop_audio_path = 'audios/stop.mp3'
hello_audio_path = 'audios/hello.mp3'
Yoo_audio_path = 'audios/Yoo.mp3'

# Image and gifs files path
Left_swipe_img_path = 'imgs/Swipe_left_img.png'
Right_swipe_img_path = 'imgs/Swipe_right_img.png'
Yoo_img_path = 'imgs/Yoo_img.png'
Scott_gif_path = 'gifs/scott'
Icon_path = 'imgs/v3ct0red.png'

# Paths to serialized detector and loads
prototxt_Path = 'models/deploy.prototxt'
weights_Path = 'models/mask_model.caffemodel'
model_Path = 'models/mask_detector.model'

# Set a threshold for face detection
confidence_threshold = 0.6

# Read deep learning network
faceNet = cv2.dnn.readNet(prototxt_Path, weights_Path)

# load the face mask detector model from disk
maskNet = load_model(model_Path)

# Handle high resolution displays:
QtWidgets.QApplication.setAttribute(QtCore.Qt.AA_EnableHighDpiScaling, True)
QtWidgets.QApplication.setAttribute(QtCore.Qt.AA_UseHighDpiPixmaps, True)

# Class for main ui
class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        global Icon_path
        MainWindow.setObjectName("MainWindow")
        MainWindow.setWindowModality(QtCore.Qt.NonModal)
        MainWindow.setFixedSize(800, 380)
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap(Icon_path), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        MainWindow.setWindowIcon(icon)
        self.centralwidget = QtWidgets.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.feature_label = QtWidgets.QLabel(self.centralwidget)
        self.feature_label.setGeometry(QtCore.QRect(150, 2, 261, 71))
        font = QtGui.QFont()
        font.setFamily("Calibri Light")
        font.setPointSize(16)
        self.feature_label.setFont(font)
        self.feature_label.setObjectName("feature_label")
        self.qtframe = QtWidgets.QLabel(self.centralwidget)
        self.qtframe.setGeometry(QtCore.QRect(290, 10, 491, 321))
        self.qtframe.setScaledContents(True)
        self.qtframe.setObjectName("qtframe")
        self.label = QtWidgets.QLabel(self.centralwidget)
        self.label.setGeometry(QtCore.QRect(10, 10, 181, 51))
        font = QtGui.QFont()
        font.setFamily("Calibri Light")
        font.setPointSize(20)
        font.setBold(False)
        font.setWeight(50)
        self.label.setFont(font)
        self.label.setObjectName("label")
        self.leap_label = QtWidgets.QLabel(self.centralwidget)
        self.leap_label.setGeometry(QtCore.QRect(40, 100, 181, 221))
        self.leap_label.setScaledContents(True)
        self.leap_label.setObjectName("leap_label")
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtWidgets.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 814, 26))
        self.menubar.setObjectName("menubar")
        self.menuFile = QtWidgets.QMenu(self.menubar)
        self.menuFile.setObjectName("menuFile")
        self.menuAbout = QtWidgets.QMenu(self.menubar)
        self.menuAbout.setObjectName("menuAbout")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtWidgets.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)
        self.actionExit = QtWidgets.QAction(MainWindow)
        self.actionExit.setObjectName("actionExit")
        self.actionAbout_LeapMask = QtWidgets.QAction(MainWindow)
        self.actionAbout_LeapMask.setObjectName("actionAbout_LeapMask")
        self.actionGitHub_Page = QtWidgets.QAction(MainWindow)
        self.actionGitHub_Page.setObjectName("actionGitHub_Page")
        self.menuFile.addAction(self.actionExit)
        self.menuAbout.addAction(self.actionAbout_LeapMask)
        self.menuAbout.addAction(self.actionGitHub_Page)
        self.menubar.addAction(self.menuFile.menuAction())
        self.menubar.addAction(self.menuAbout.menuAction())

        self.retranslateUi(MainWindow)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

        self.actionExit.triggered.connect(self.exit)
        self.actionGitHub_Page.triggered.connect(self.GitHub_link)
        self.actionAbout_LeapMask.triggered.connect(self.open_about)

    def retranslateUi(self, MainWindow):
        _translate = QtCore.QCoreApplication.translate
        MainWindow.setWindowTitle(_translate("MainWindow", "LeapMask GUI"))
        self.feature_label.setText(_translate("MainWindow", "Face mask"))
        self.label.setText(_translate("MainWindow", "Detected:"))
        self.menuFile.setTitle(_translate("MainWindow", "File"))
        self.menuAbout.setTitle(_translate("MainWindow", "Help"))
        self.actionExit.setText(_translate("MainWindow", "Exit"))
        self.actionAbout_LeapMask.setText(
            _translate("MainWindow", "About LeapMask"))
        self.actionGitHub_Page.setText(_translate("MainWindow", "GitHub Page"))

    # Open github repo
    def GitHub_link(self):
        os.system(
            "start \"\" https://github.com/Cipulot/CodeWithFriends-Spring2020/tree/master/projects/Cipulot")

    # Open about dialog window
    def open_about(self):
        global AboutWindow
        AboutWindow.show()

    # Trigger global exit flag
    def exit(self):
        global Stopped
        Stopped = True

    # I need this function cause closing the gui from the X on top-right caused a total crash. This seems also the proper way to handle closign of gui
    def closeEvent(self, event):
        global Stopped
        Stopped = True

# Class for about window ui
class Ui_AboutWindow(object):
    def setupUi(self, AboutWindow):
        global Scott_gif_path, Icon_path
        AboutWindow.setObjectName("AboutWindow")
        AboutWindow.setWindowModality(QtCore.Qt.NonModal)
        AboutWindow.setEnabled(True)
        AboutWindow.setFixedSize(270, 400)
        icon = QtGui.QIcon()
        icon.addPixmap(QtGui.QPixmap(Icon_path), QtGui.QIcon.Normal, QtGui.QIcon.Off)
        AboutWindow.setWindowIcon(icon)
        self.pushButton = QtWidgets.QPushButton(AboutWindow)
        self.pushButton.setGeometry(QtCore.QRect(90, 370, 93, 28))
        self.pushButton.setObjectName("pushButton")
        self.label = QtWidgets.QLabel(AboutWindow)
        self.label.setGeometry(QtCore.QRect(80, 10, 161, 41))
        font = QtGui.QFont()
        font.setFamily("Calibri")
        font.setPointSize(22)
        font.setBold(True)
        font.setItalic(True)
        font.setWeight(75)
        self.label.setFont(font)
        self.label.setObjectName("label")
        self.label_2 = QtWidgets.QLabel(AboutWindow)
        self.label_2.setGeometry(QtCore.QRect(10, 70, 301, 31))
        font = QtGui.QFont()
        font.setFamily("Calibri")
        font.setPointSize(14)
        self.label_2.setFont(font)
        self.label_2.setObjectName("label_2")
        self.label_3 = QtWidgets.QLabel(AboutWindow)
        self.label_3.setGeometry(QtCore.QRect(55, 340, 201, 31))
        self.label_3.setObjectName("label_3")
        self.label_4 = QtWidgets.QLabel(AboutWindow)
        self.label_4.setGeometry(QtCore.QRect(45, 110, 181, 221))
        self.label_4.setText("")
        self.label_4.setScaledContents(True)
        self.label_4.setObjectName("label_4")
        self.movie = QtGui.QMovie(Scott_gif_path)
        self.movie.setCacheMode(QMovie.CacheAll)
        self.movie.setSpeed(100)
        self.label_4.setMovie(self.movie)
        self.movie.start()

        self.retranslateUi(AboutWindow)
        QtCore.QMetaObject.connectSlotsByName(AboutWindow)
        self.pushButton.clicked.connect(self.Ok_clicked)

    def retranslateUi(self, AboutWindow):
        _translate = QtCore.QCoreApplication.translate
        AboutWindow.setWindowTitle(_translate("AboutWindow", "About LeapMask"))
        self.pushButton.setText(_translate("AboutWindow", "OK"))
        self.label.setText(_translate("AboutWindow", "LeapMask"))
        self.label_2.setText(_translate(
            "AboutWindow", "Code With Friends - Spring 2020"))
        self.label_3.setText(_translate(
            "AboutWindow", "Developed by: Luca Seva' (Cipulot)"))

    # Handle window closing when button pressed
    def Ok_clicked(self):
        global AboutWindow
        AboutWindow.close()

    # I need this function cause closing the gui from the X on top-right caused a total crash. This seems also the proper way to handle closign of gui
    def closeEvent(self, event):
        global Stopped
        Stopped = True


class GUIThread(Thread):
    '''
    Provides a dedicated thread for the GUI
    '''

    def __init__(self, name):
        Thread.__init__(self)
        self.name = name

    def run(self):
        global Stopped, main_ui, AboutWindow

        try:
            # Set up all gui stuff
            app = QtWidgets.QApplication([])
            MainWindow = QtWidgets.QMainWindow()
            AboutWindow = QtWidgets.QDialog()
            main_ui = Ui_MainWindow()
            main_ui.setupUi(MainWindow)
            about_ui = Ui_AboutWindow()
            about_ui.setupUi(AboutWindow)

            # Now show main ui
            MainWindow.show()

            # If the app has exited set global flag
            if(app.exec_() == 0):
                Stopped = True

        except:
            print("\n\nThe GUI thread raised an exception. Terminating...\n\n")
            Stopped = True


class FeedThread(Thread):
    '''
    Provides a dedicated thread for the openCV operations, including image classification.
    '''

    def __init__(self, name):
        Thread.__init__(self)
        self.name = name

    def run(self):
        global Stopped, feed_only, Palm_position
        try:
            # Flag to indicate previous frame feature detected: True-> facemask | False->no facemask | None-> no feature
            Prev_feature = None

            # Variable to store timestamps
            First_frame_timestamp = 0

            # Just a variable to store the zip object of the prediction
            zip_features = None

            vs = VideoStream(src=0).start()

            while (Stopped == False):
                # Grab frame and resize it.
                # This will also help with workload reduction in larger frames
                frame = vs.read()
                frame = imutils.resize(frame, width=400)
                if(feed_only == False):
                    # Evaluate if there is a face mask in the frame, returning position and prediction value
                    (locs, preds) = self.mask_evaluation(frame, faceNet, maskNet)

                    zip_features = zip(locs, preds)

                    # For each detected face draw a bounding box around it with proper color
                    for (box, pred) in zip_features:
                        # Unpacking of bounding box and prediction values
                        (startX, startY, endX, endY) = box

                        Prev_feature, First_frame_timestamp, color = self.feature_timer(
                            pred, Prev_feature, First_frame_timestamp)

                        cv2.rectangle(frame, (startX, startY),
                                    (endX, endY), color, 2)

                # show the output frame, converting it to QImage for the gui
                height, width, channel = frame.shape
                bytesPerLine = 3 * width

                # Proceed with the conversion swapping the rgb values for color correction
                qImg = QImage(frame.data, width, height,
                              bytesPerLine, QImage.Format_RGB888).rgbSwapped()

                # Call function that display frame (as QImage) on the main gui
                update_main_ui_frame(qImg)

                # For debug you can run the "normal" vc2 window instead. Just comment out the above and uncomment this:
                #cv2.imshow(self.name, frame)

                # Check for pressed keys
                k = cv2.waitKey(1)

                # Implement only if using cv2 window
                '''
                if k % 256 == 27:
                    # ESC pressed
                    print("Closing Opencv...\n")
                    break
                '''
        except:
            # Clean stuff and flag that the thread is being  stopped
            cv2.destroyAllWindows()
            vs.stop()
            print(
                "\n\nThe feature detection thread raised an exception. Terminating...\n\n")
            Stopped = True

    # Function that evaluates the feature values and position in the frame
    def mask_evaluation(self, frame, faceNet, maskNet):
        faces = []
        locs = []
        preds = []

        # Create a blob from image to process it through network classification
        (h, w) = frame.shape[:2]
        blob = cv2.dnn.blobFromImage(frame, 1.0, (300, 300),
                                     (104.0, 177.0, 123.0))

        # Set the network input and execute detection
        faceNet.setInput(blob)
        detections = faceNet.forward()

        for i in range(0, detections.shape[2]):
            # Get confidence values of the prediction
            confidence = detections[0, 0, i, 2]

            # Based on the threshold that we specify at the beginning of the code we exclude low value predictions
            if confidence > confidence_threshold:
                # Get (X,Y) coordinates for bounding box
                box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
                (startX, startY, endX, endY) = box.astype("int")

                # Check if boxes are in frame
                (startX, startY) = (max(0, startX), max(0, startY))
                (endX, endY) = (min(w - 1, endX), min(h - 1, endY))

                # Get face's ROI and do a resize
                face = frame[startY:endY, startX:endX]
                face = cv2.cvtColor(face, cv2.COLOR_BGR2RGB)
                face = cv2.resize(face, (224, 224))
                face = img_to_array(face)
                face = preprocess_input(face)
                face = np.expand_dims(face, axis=0)

                # Add the face and bounding box coordinates to the list that will be later returned
                faces.append(face)
                locs.append((startX, startY, endX, endY))

        # Predict only if a face is detected
        if len(faces) == 1:
            preds = maskNet.predict(faces)

        # Return location of detected face and prediction value
        return (locs, preds)

    # Function that keeps track of the elapsed time since the first frame of a feature.
    # Basically it start over to count if the detected feature changes....that's all.
    def feature_timer(self, prediction, prev, timestamp):
        global Leap_enabled

        (mask, withoutMask) = prediction

        # Face mask detected
        if(mask > withoutMask):
            # First frame with a face mask
            if(prev == None) or (prev == False):
                timestamp = time.process_time()
                prev = True

            # Successive frames still with a face mask
            else:
                # Check if enough time elapsed with the same feature
                if((time.process_time() - timestamp) >= 3):
                    # Call function to update a label on the gui
                    update_main_ui_label("Face Mask")

                    # Comment out if you want terminal feedback
                    #print("facemask time")

                    # Enable motion detection for user inputs
                    Leap_enabled = True

        # No face mask detected
        elif(mask < withoutMask):
            # First frame without a face mask
            if(prev == None) or (prev == True):
                timestamp = time.process_time()
                prev = False

            # Successive frames still without a face mask
            else:
                # Check if enough time elapsed with the same feature
                if((time.process_time() - timestamp) >= 3):
                    # Call function to update a label on the gui
                    update_main_ui_label("Face Only")

                    # Comment out if you want terminal feedback
                    #print("face time")

                    # Disable motion detection for user inputs
                    Leap_enabled = False

        # Set color based on facemask value
        color = (0, 255, 0) if (mask > withoutMask) else (0, 0, 255)

        return prev, timestamp, color


class LeapThread(Thread):
    '''
    Provides a dedicated thread for Leap Motion declarations and stuff.
    '''

    def __init__(self, name):
        Thread.__init__(self)
        self.name = name

    def run(self):
        global Stopped

        try:
            # Create a sample listener and controller
            listener = SampleListener()
            controller = Leap.Controller()

            # Have the sample listener receive events from the controller
            controller.add_listener(listener)

            print("Waiting for Leap Motion connection...\n")

            # This loop ensure that this thread remains active until the user decides to exit the app
            while(Stopped == False):
                time.sleep(0.5)

        # If ANY exception is raised then the specified thread will be closed
        except:
            print("\n\nThe Leap Motion thread raised an exception. Terminating...\n\n")
            Stopped = True


class SampleListener(Leap.Listener):

    # State of gesture names
    state_names = ['STATE_INVALID', 'STATE_START', 'STATE_UPDATE', 'STATE_END']

    def on_init(self, controller):
        print("Initialized\n")

    def on_connect(self, controller):
        global Leap_connected
        # Set the global flag so that other threads know that the connection is established
        Leap_connected = True
        print("Connected\n")

        # Enable gesture recognition (specific gesture based)
        controller.enable_gesture(Leap.Gesture.TYPE_SWIPE)

    def on_disconnect(self, controller):
        print("Disconnected\n")

    def on_exit(self, controller):
        print("Exited Leap Motion\n")

    def on_frame(self, controller):
        global Stopped, Leap_enabled, Palm_position, Pressed, Yoo_audio_path

        # Do all the motion detection stuff only if user gesture is enabled (face mask on)
        if(Leap_enabled == True) and (Stopped == False):
            # Get the most recent frame
            frame = controller.frame()

            # Get hands, even tho we'll use only one
            hands = frame.hands

            # Get gesture as list
            gestures = list(frame.gestures())

            # Count hands and gestures
            numHands = len(hands)
            numGestures = len(gestures)

            if numHands >= 1:
                # Get the first hand
                hand = hands[0]

                # Get the palm position
                palm = hand.palm_position
                normal = hand.palm_normal
                Palm_position = palm

                # If the user put his/her hand at a certain distance from the sensor it triggers the ringbell
                if(Palm_position[1] <= 120) and (Pressed == False):
                    Pressed = True
                    update_leap_label("YOO")
                    Yoo = AudioThread("Yoo", Yoo_audio_path)
                    Yoo.daemon = True
                    Yoo.start()

            elif numHands == 0:
                # Just reset position to none if no hand is detected and reset the flag for next user press
                Palm_position = None
                Pressed = False

            # If one or more gesture (2 hand, different gesture) are detected cycle through them
            if numGestures >= 1:
                for gesture in gestures:
                    if gesture.type == Leap.Gesture.TYPE_SWIPE:
                        swipe = SwipeGesture(gesture)
                        Swipe_direction = swipe.direction

                        if(self.state_names[gesture.state] == 'STATE_START'):
                            print("Swipe start")

                        if(self.state_names[gesture.state] == 'STATE_UPDATE'):
                            print("Swipe update")

                        if(self.state_names[gesture.state] == 'STATE_END'):
                            print("Swipe end")
                            if(Swipe_direction[0] > 0):
                                update_leap_label("RIGHT")
                            elif(Swipe_direction[0] < 0):
                                update_leap_label("LEFT")

                        # Complete set of gesture details...
                        '''
                        print("  Swipe id: %d, state: %s, position: %s, direction: %s, speed: %f" % (
                            gesture.id, self.state_names[gesture.state],
                            swipe.position, swipe.direction, swipe.speed))
                        '''

    # Don't even know why I've made this tbh....since I got the state_names[] at the top.
    def state_string(self, state):
        if state == Leap.Gesture.STATE_START:
            return "STATE_START"

        if state == Leap.Gesture.STATE_UPDATE:
            return "STATE_UPDATE"

        if state == Leap.Gesture.STATE_STOP:
            return "STATE_STOP"

        if state == Leap.Gesture.STATE_INVALID:
            return "STATE_INVALID"


class AudioThread(Thread):
    '''
    Privides an audio player thread to play sound files.
    '''
    # This thread might seem unnecessary, maybe it is. The playsound call will block successive calls until it stops to play.
    # That's why I've backed it into this thread.

    def __init__(self, name, path):
        Thread.__init__(self)
        self.name = name
        self.path = path

    def run(self):
        playsound(self.path)


# Update the video on the gui with the most updated frame
def update_main_ui_frame(img):
    global Stopped, main_ui
    try:
        # Check if the app is still "allowed" to run.
        # Picture this: another function in the code set the flag, but this function, being in its oun call, is still be executed, while the gui app no longer exists.
        # This would be a problem, but not with this check
        if(Stopped == False):
            main_ui.qtframe.setPixmap(QtGui.QPixmap.fromImage(img))
    except:
        sys.exit()
        # This is a brutal way to handle the exception (I don't...) but some exceptions that I couldn't figure out where are generated
        # sometimes prevent the exit of the app.

# Update the detected feature label on the gui
def update_main_ui_label(text: str):
    global main_ui

    # Same as for update_main_ui_frame()
    if(Stopped == False):
        main_ui.feature_label.setText(text)

def update_leap_label(flag: str):
    global main_ui, Left_swipe_img_path, Right_swipe_img_path, Yoo_img_path

    # Same as for update_main_ui_frame() plus case-based selection
    if(flag == "LEFT") and (Stopped == False):
        main_ui.leap_label.setPixmap(QtGui.QPixmap(Left_swipe_img_path))
    elif(flag == "RIGHT") and (Stopped == False):
        main_ui.leap_label.setPixmap(QtGui.QPixmap(Right_swipe_img_path))
    if(flag == "YOO") and (Stopped == False):
        main_ui.leap_label.setPixmap(QtGui.QPixmap(Yoo_img_path))

# Main
def LeapMask_main():
    global Stopped, Leap_connected

    try:
        print("\n######## LeapMask ########\n")

        # Create a GUI thread to manage the visual stuff
        Gui_thread = GUIThread("GUI")
        Gui_thread.daemon = True
        Gui_thread.start()

        # This sleep time IS REQUIRED!
        # This allows the PyQt to initialize all the stuff and the globals to be updated.
        # Otherwise this main will "see" the globals as their default value for XYZ period of time
        time.sleep(1)

        # Put something here
        update_main_ui_label("HELLO")

        # Create a Leap Motion thread that will handle connection and data gathering
        Leap_thread = LeapThread("Leap")
        Leap_thread.daemon = True
        Leap_thread.start()

        # Initialize a time-out timer variable
        time_out_timer = 0

        # Wait (10 sec) for the Leap Motion connected event to set the global flag.
        # If passed that timeframe the flag is still set to false exit the whole application
        while(Leap_connected == False):
            time.sleep(0.5)
            time_out_timer += 0.5
            if(time_out_timer >= 10):
                print(
                    "Timeout: cannot connect to the Leap Motion sensor in time :(\nExiting...\n")
                sys.exit()

        print("Starting detection feed thread...\n")
        # Create a thread that will handle face and facemask detection
        Camera_thread = FeedThread("Feed")
        Camera_thread.daemon = True
        Camera_thread.start()

        # Loop to keep the thread of main alive
        while(True):
            if Stopped == False:
                time.sleep(0.1)
            else:
                print("Closing LeapMask...")
                sys.exit()

    except:
        sys.exit()

if __name__ == '__main__':
    # Call main function and pass args given by user
    LeapMask_main()
