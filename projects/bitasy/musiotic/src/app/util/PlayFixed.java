package util;

import jm.util.Play;

public class PlayFixed{
    public static void stopMidi() {
        try{
            Play.stopMidi();
        } catch (StackOverflowError ignored){}
    }
}
