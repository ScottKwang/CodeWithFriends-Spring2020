#This script is intended to be use with the precompiled Python3 64bit compatible files provided
import Leap
import sys

class SampleListener(Leap.Listener):
    def on_init(self, controller):
        print("Initialized")

    def on_connect(self, controller):
        print("Connected")

    def on_disconnect(self, controller):
        print("Disconnected")

    def on_exit(self, controller):
        print("Exited")

    def on_frame(self, controller):
        # Get the most recent frame and report some basic information
        frame = controller.frame()
        hands = frame.hands
        numHands = len(hands)

        if numHands >= 1:
            # Get the first hand
            hand = hands[0]

            # Get the palm position
            palm = hand.palm_position
            print("Palm position:", palm)

            # Get the palm normal vector  and direction
            normal = hand.palm_normal
            direction = hand.direction
            '''
            # Calculate the hand's pitch, roll, and yaw angles
            print("Pitch: %f degrees,  Roll: %f degrees,  Yaw: %f degrees" % (
                direction.pitch * Leap.RAD_TO_DEG,
                normal.roll * Leap.RAD_TO_DEG,
                direction.yaw * Leap.RAD_TO_DEG))

            print("Hand curvature radius: %f mm" % hand.sphere_radius)
            '''


def main():
    # Create a sample listener and controller
    listener = SampleListener()
    controller = Leap.Controller()

    # Have the sample listener receive events from the controller
    controller.add_listener(listener)

    # Keep this process running until Enter is pressed
    print("Press Enter to quit...")
    sys.stdin.readline()

    # Remove the sample listener when done
    controller.remove_listener(listener)


if __name__ == "__main__":
    main()