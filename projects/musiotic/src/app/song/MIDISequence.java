//package song;
//
//import javafx.scene.Node;
//import song.Phase;
//
//public abstract class MIDISequence {
//
//    // Number of notes a user can input midi for. 8 is one octave.
//    public int numNotes;
//
//    // How many measures the MIDI Sequence will have.
//    // Default: 4 measures (4 bar loops).
//    public int numMeasures;
//
//    // 4 / 4 Time. The top number signifies 4 notes of the bottom number type of note.
//    // Default: 4 quarter notes per measure. Index 0 is top, index 1 is bottom.
//    // int[] timeSig = {4, 4};
//
//    // The Type of phase taken in by the MIDI.
//
//    public final Phase phase;
//
//    // To which degree a user can edit the midi. (1 = Sixteenth Note, 2 = Eighth Note, 4 = Quarter etc.)
//    // Default: 4 (User can currently edit a quarter note).
//    public int editable = 4;
//
//    public MIDISequence(Phase phase) {
//        this(phase, 8);
//    }
//
//    public MIDISequence(Phase phase, int numNotes) {
//        this(phase, numNotes, 4);
//    }
//
//    public MIDISequence(Phase phase, int numNotes, int numMeasures) {
//        this.phase = phase;
//        this.numNotes = numNotes;
//        this.numMeasures = numMeasures;
//    }
//
//    public abstract Node getScreen();
//
//    public Phase getPhase() {
//        return phase;
//    }
//
//    public void addMeasures(boolean right) {
//        this.numMeasures += 4;
//        System.out.println("The number of Measures this MIDI has is: " + numMeasures);
//        if (right) addMeasuresRight();
//        else addMeasuresLeft();
//    }
//
//    private void addMeasuresLeft() {
//
//    }
//
//    private void addMeasuresRight() {
//
//    }
//
//}
