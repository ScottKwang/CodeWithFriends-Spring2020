package music;

public abstract class MIDISequence {

    // Refer to Camelot wheel in /assets/camelot.png.
    // Default: 8B (C Major).
    String key = "8B";

    // 4 / 4 Time. The top number signifies 4 notes of the bottom number type of note.
    // Default: 4 quarter notes per measure. Index 0 is top, index 1 is bottom.
    int[] timeSig = {4, 4};

    // How many measures the MIDI Sequence will have.
    // Default: 4 measures (4 bar loops).
    int measures = 4;

    // To which degree a user can edit the midi. (1 = Whole Note, 2 = Half, 4 = Quarter etc.)
    // Default: 4 (User can only edit a quarter note).
    int editable = 4;


    // Todo: Java Sequencers?


}
