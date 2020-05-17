#from __future__ import absolute_import, division, print_function, unicode_literals
import os
import sys
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

# Flag to indicate if Leap Motion controller is connected
Leap_connected = False

# Flag to indicate if Leap Motion controller is enabled
Leap_enabled = False

# Flag to indicate threads if the thread was user stopped
Stopped = False

# Palm position vector
Palm_position = None

# Flag to indicate if the user "pressed" the virtual button
Pressed = False

# Audio files path
stop_audio_path = 'audios/stop.mp3'
hello_audio_path = 'audios/hello.mp3'
door_audio_path = 'audios/door.mp3'

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


class FeedThread(Thread):
    '''
    Provides a dedicated thread for the openCV operations, including image classification.
    '''

    def __init__(self, name):
        Thread.__init__(self)
        self.name = name

    def run(self):
        global Stopped, Palm_position, Leap_enabled
        # Flag to indicate previous frame feature detected: True-> facemask | False->no facemask | None-> no feature
        Prev_feature = None
        # Variable to store timestamps
        First_frame_timestamp = 0
        # Just a variable to store the zip object of the prediction
        zip_features = None

        vs = VideoStream(src=0).start()

        while True:
            # Grab frame and resize it.
            # This will also help with workload reduction in larger frames
            frame = vs.read()
            frame = imutils.resize(frame, width=400)

            # Evaluate if there is a face mask in the frame, returning position and prediction value
            (locs, preds) = self.mask_evaluation(frame, faceNet, maskNet)

            zip_features = zip(locs, preds)

            # For each detected face draw a bounding box around it with proper color
            for (box, pred) in zip_features:
                # Unpacking of bounding box and prediction values
                (startX, startY, endX, endY) = box

                Prev_feature, First_frame_timestamp, color = self.feature_timer(
                    pred, Prev_feature, First_frame_timestamp)

                #cv2.putText(frame, label, (startX, startY - 10),cv2.FONT_HERSHEY_SIMPLEX, 0.45, color, 2)
                #cv2.putText(frame, str(Palm_position), (10, 15),cv2.FONT_HERSHEY_SIMPLEX, 0.55, (255, 255, 255), 2)
                cv2.rectangle(frame, (startX, startY), (endX, endY), color, 2)

            # show the output frame
            cv2.imshow(self.name, frame)

            # Check for pressed keys to exit
            k = cv2.waitKey(1)
            if k % 256 == 27:
                # ESC pressed
                print("Closing Opencv...\n")
                break

        # Clean stuff and flag that the thread is being  stopped
        cv2.destroyAllWindows()
        vs.stop()
        Stopped = True

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

    def feature_timer(self, prediction, prev, timestamp):

        (mask, withoutMask) = prediction

        # Face mask detected
        if(mask > withoutMask):
            # First frame with a face mask
            if(prev == None) or (prev == False):
                timestamp = time.clock()
                prev = True

            # Successive frames still with a face mask
            else:
                # Check if enough time elapsed with the same feature
                if((time.clock() - timestamp) >= 3):
                    print("facemask time")

        # No face mask detected
        elif(mask < withoutMask):
            # First frame without a face mask
            if(prev == None) or (prev == True):
                timestamp = time.clock()
                prev = False

            # Successive frames still without a face mask
            else:
                # Check if enough time elapsed with the same feature
                if((time.clock() - timestamp) >= 3):
                    print("face time")

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

            # This loop ensure that this thread remains active and will allow the call of "remove_listener"
            # if the user decides to exit the app via the openCV window
            while(Stopped == False):
                time.sleep(0.5)

            # Remove the sample listener when done
            controller.remove_listener(listener)

        # If ANY exception is raised then the specified thread will be closed
        except:
            print("\n\n\nThread '" + self.name +
                  "' has exited due to an exception raised in function 'run leap'!\n\n")
            sys.exit()


class SampleListener(Leap.Listener):
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
        global Leap_enabled, Palm_position, Pressed, door_audio_path

        # Get the most recent frame and report some basic information
        frame = controller.frame()
        hands = frame.hands
        gestures = list(frame.gestures())
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
            if((Palm_position[1] <= 120) and (Pressed == False) and (Leap_enabled == True)):
                Pressed = True
                Door = AudioThread("door", door_audio_path)
                Door.daemon = True
                Door.start()

        elif numHands == 0:
            # Just reset position to none if no hand is detected and reset the flag for next user press
            Palm_position = None
            Pressed = False

        # If one or more gesture (2 hand, different gesture) are detected cycle through them
        if numGestures >= 1:
            for gesture in gestures:
                if gesture.type == Leap.Gesture.TYPE_SWIPE:
                    swipe = SwipeGesture(gesture)

                    if(self.state_names[gesture.state] == 'STATE_START'):
                        print("Swipe start")

                    if(self.state_names[gesture.state] == 'STATE_UPDATE'):
                        print("Swipe update")

                    if(self.state_names[gesture.state] == 'STATE_END'):
                        print("Swipe end")

                    # Complete set of gesture details...
                    '''
                    print("  Swipe id: %d, state: %s, position: %s, direction: %s, speed: %f" % (
                        gesture.id, self.state_names[gesture.state],
                        swipe.position, swipe.direction, swipe.speed))
                    '''

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

    def __init__(self, name, path):
        Thread.__init__(self)
        self.name = name
        self.path = path

    def run(self):
        playsound(self.path)


def main():
    global Leap_connected

    try:
        print("\n######## LeapMask ########\n")
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

        while(True):
            # Keep this main thread alive if openCV and Leap Motion are still running
            if((Camera_thread.isAlive() == True) and (Leap_thread.isAlive() == True)):
                time.sleep(0.5)

            # If both threads aren't alive this means that both exited correctely (using the global flag Stopped)
            # this cover also the case you decide to exit from one of the threads (if you press esc in opencv the program will stop)
            elif((Camera_thread.isAlive() == False) and (Leap_thread.isAlive() == False)):
                print("Closing main thread...\n")
                sys.exit()

    # If ANY exception is raised then the specified thread will be closed
    except:
        sys.exit()


if __name__ == '__main__':
    # Call main function and pass args given by user
    main()
