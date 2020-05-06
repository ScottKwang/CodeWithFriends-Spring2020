package MusicComponents;

public abstract class MIDISequence {

    // Refer to Camelot wheel in /assets/camelot.png.
    // Default: 8B (C Major).
    String key = "8B";

    // 4 / 4 Time. The top number signifies 4 notes of the bottom number type of note.
    // Default: 4 quarter notes per measure.
    int timeSigTop = 4;
    int timeSigBot = 4;

    // How many measures the MIDI Sequence will have.
    // Default: 4 measures (4 bar loops).
    int measures = 4;

    // Todo: Java Sequencers?


}
