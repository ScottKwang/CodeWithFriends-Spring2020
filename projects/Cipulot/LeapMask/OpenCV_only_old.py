import os
import sys
import time
import datetime
import shutil
import threading
from threading import Thread
from playsound import playsound
import imutils
import cv2
import Leap

# Global variables and flags
Leap_connected = False      # Flag to indicate if Leap Motion controller is connected
Position = None             # Palm position vector
Stopped = False             # Flag to indicate threads if the thread was user stopped
Pressed = False             # Flag to indicate if the user "pressed" the virtual button
Face_time_count = 0         # Timestamp of the particular frame for face
Mask_time_count = 0         # Timestamp of the particular frame for mask
Start_time_count_face = 0   # Timestamp of the frame in which the face was detected
Start_time_count_mask = 0   # Timestamp of the frame in which the mask was detected
First_face_flag = True      # Flag to determin if the frame time should be saved in the Start_time_count or not
First_mask_flag = True      # Flag to determin if the frame time should be saved in the Start_time_count or not
stop_audio_flag = False     # Flag to avoid multiple calls for the same audio file in a shot time (multiple playbacks at the same time)
hello_audio_flag = False    # Flag to avoid multiple calls for the same audio file in a shot time (multiple playbacks at the same time)
open_door_enable = False    # Flag to indicate that you can "open the door"

# Load the cascade
face_cascade = cv2.CascadeClassifier('models/haarcascade_frontalface_alt2.xml')

# CASCADE PARAMETER FOR FACEMASK (from my tests)
# Model ver.= 5 ; scaleFactor=1.1 minNeighbors=20
# Model ver.= 10 ; scaleFactor=1.05 minNeighbors=20
# Model ver.= 5 ; scaleFactor=1.2 minNeighbors=20
# Model ver.= 5 ; scaleFactor=1.05 minNeighbors=10

mask_cascade = cv2.CascadeClassifier('models/haarcascade_facemask_14.xml')
recognizer = cv2.face.LBPHFaceRecognizer_create()

# Audio files path
stop_audio_path = 'audios/stop.mp3'
hello_audio_path = 'audios/hello.mp3'
door_audio_path = 'audios/door.mp3'


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


class FeedThread(Thread):
    '''
    Provides a dedicated thread for the openCV operations, including image classification.
    '''

    def __init__(self, name):
        Thread.__init__(self)
        self.name = name

    def run(self):
        global Stopped, open_door_enable

        try:
            # Set the default webcam
            cap = cv2.VideoCapture(0)
            #cap.set(cv2.CAP_PROP_FRAME_WIDTH, 533)
            #cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 400)
            while (Stopped == False):
                # Read the frame
                _, img = cap.read()

                # Resize the image just a bit to reduce the overall work for the cpu when detecting
                # Not needed if you have a very powerful system
                img = imutils.resize(img, width=400)

                # Start detection of desired features
                Feed = self.detection(img)

                # Place a text on the frame indicating the hand palm position
                cv2.putText(img, str(Position), (10, 15),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.55, (255, 255, 255), 2)

                # Display
                cv2.imshow(self.name, Feed)

                # Check for pressed keys
                k = cv2.waitKey(1)
                if k % 256 == 27:
                    # ESC pressed
                    print("Closing Opencv...")
                    break

            # Release the VideoCapture object and terminate the thread
            cap.release()
            cv2.destroyAllWindows()
            Stopped = True

        # If ANY exception is raised then the specified thread will be closed
        except:
            print("\n\n\nThread '" + self.name +
                  "' has exited due to an exception raised in function 'run'!\n\n")
            sys.exit()

    def detection(self, img):
        global open_door_enable, Pressed
        global hello_audio_flag, stop_audio_flag, stop_audio_path, hello_audio_path
        global First_face_flag, First_mask_flag
        global Face_time_count, Start_time_count_face, Mask_time_count, Start_time_count_mask

        try:
            # If we've enabled the motion sensor we can now avoid call detection functions that will put load on system
            # So simply return the frame
            if(open_door_enable == True):
                return img

            # Convert to grayscale
            gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

            # Detect the faces
            faces = face_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=15)

            # Reset flags for next face if no faces detected
            if(len(faces) == 0):
                First_face_flag = True
                stop_audio_flag = False

            # Draw the rectangle around each face
            for (x_faces, y_faces, w_faces, h_faces) in faces:

                # Various check for audio playback (if not in "open door" phase)
                if((stop_audio_flag == False) and (open_door_enable == False)):
                    Face_time_count = time.clock()

                    # Save the frametime of the frame at which we detect the face for the first time
                    if(First_face_flag == True):
                        First_Face_time_count = Face_time_count
                        First_face_flag = False

                    # Here we just compare the current frametime with the first one and check if enough time passed we trigger the audio and events
                    if((Face_time_count - First_Face_time_count) >= 3):
                        stop_audio_flag = True
                        Stop = AudioThread("stop", stop_audio_path)
                        Stop.daemon = True
                        Stop.start()
                #print("Face timestamp: {}".format(Face_time_count))

                color = (255, 0, 0)
                stroke = 2

                # 2nd point for the rectangle around detected face (bottom right)
                end_cord_x = x_faces + w_faces
                end_cord_y = y_faces + h_faces

                # Create a frame coordinate that covers only the face box (with some margins)
                # Will be used to detect face mask only where the face is
                roi_gray = gray[y_faces - 5:end_cord_y +
                                5, x_faces - 5:end_cord_x + 5]
                roi_color = img[y_faces:end_cord_y, x_faces:end_cord_x]

                # Draw rectangle around detected face
                cv2.rectangle(img, (x_faces - 5, y_faces - 5),
                              (end_cord_x + 5, end_cord_y + 5), color, stroke)

                # Detect facemasks where a face is present
                masks = mask_cascade.detectMultiScale(roi_gray, scaleFactor=1.05, minNeighbors=10)

                # Reset flags for next mask if no mask detected
                if(len(masks) == 0):
                    First_mask_flag = True
                    hello_audio_flag = False

                for (x_masks, y_masks, w_masks, h_masks) in masks:

                    # Various check for audio playback (if not in "open door" phase)
                    if((hello_audio_flag == False) and (open_door_enable == False)):
                        Mask_time_count = time.clock()

                         # Save the frametime of the frame at which we detect the mask for the first time
                        if(First_mask_flag == True):
                            Start_time_count_mask = Mask_time_count
                            First_mask_flag = False

                        # Here we just compare the current frametime with the first one and check if enough time passed we trigger the audio and events
                        if((Mask_time_count - Start_time_count_mask) >= 2):
                            # If the code made it's way here it means that a person with a facemask wants to knock on door
                            # We now enable the "open door" mode
                            open_door_enable = True
                            hello_audio_flag = True
                            Hello = AudioThread("hello", hello_audio_path)
                            Hello.daemon = True
                            Hello.start()
                    #print("Mask timestamp: {}".format(Face_time_count))

                    # Surround cascade with rectangle
                    cv2.rectangle(roi_color, (x_masks, y_masks),
                                  (x_masks + w_masks, y_masks + h_masks), (0, 0, 255), 2)

            return img

        # If ANY exception is raised then the specified thread will be closed
        except:
            print("\n\n\nThread '" + self.name +
                  "' has exited due to an exception raised in function 'detection'!\n\n")
            sys.exit()


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

            print("Waiting for Leap Motion connection...")

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
    def on_init(self, controller):
        print("Initialized")

    def on_connect(self, controller):
        global Leap_connected
        # Set the global flag so that other threads know that the connection is established
        Leap_connected = True
        print("Connected")

    def on_disconnect(self, controller):
        print("Disconnected")

    def on_exit(self, controller):
        print("Exited Leap Motion")

    def on_frame(self, controller):
        global open_door_enable, Position, Pressed, door_audio_path

        # Get the most recent frame and report some basic information
        frame = controller.frame()
        hands = frame.hands
        numHands = len(hands)

        if numHands >= 1:
            # Get the first hand
            hand = hands[0]

            # Get the palm position
            palm = hand.palm_position
            Position = palm

            # If the user put his/her hand at a certain distance from the sensor it triggers the ringbell
            if((Position[1] <= 120) and (Pressed == False) and (open_door_enable == True)):
                Pressed = True
                Door = AudioThread("door", door_audio_path)
                Door.daemon = True
                Door.start()

        else:
            # Just reset showed position to none if no hand is detected and reset the flag for next user press
            Position = None
            Pressed = False


def main():
    global Leap_connected

    try:
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
                    "Timeout: cannot connect to the Leap Motion sensor in time :(\nExiting...")
                sys.exit()

        print("Starting OpenCV thread...")
        # Create a cv2 thread that will handle face detection
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
                print("Closing main thread...")
                sys.exit()

    # If ANY exception is raised then the specified thread will be closed
    except:
        sys.exit()


if __name__ == '__main__':
    # Call main function and pass args given by user
    main()
