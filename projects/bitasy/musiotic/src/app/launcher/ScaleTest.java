package launcher;

import jm.JMC;
import jm.constants.Instruments;
import jm.midi.MidiSynth;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;

import javax.sound.midi.InvalidMidiDataException;
import java.util.Arrays;

import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C5;
import static jm.constants.Pitches.F4;

public class ScaleTest {

    private static Score score;
    private static int tonic;
    private static int[] mode;

    public static void main(String[] args) {
        score = new Score("12345");
        tonic = C4;
        mode = JMC.MAJOR_SCALE;
        Part p = new Part("Guitar", Instruments.ACOUSTIC_GUITAR, 0);
        Phrase phr = new Phrase("CDEFG", 1.0);

        var notes = new Note[8];

        for(int i = 0; i < notes.length; i++){
            notes[i] = new Note(tonic, SIXTEENTH_NOTE);
            Mod.transpose(notes[i], i, mode, tonic);
        }

        phr.addNoteList(notes);

        p.addPhrase(phr);
        score.addPart(p);

        /*Play.midi(score);
        if(true)return;*/

        MidiSynth ms = new MidiSynth();
        int[][] modes = new int[][]{JMC.MIXOLYDIAN_SCALE, JMC.MINOR_SCALE};
        int[] roots = new int[]{F4, C5, C4};
        try {
            ms.play(score);
            for(int[] mode : modes){
                while(ms.isPlaying()) Thread.sleep(500);
                changeMode(mode);
                ms.play(score);
            }
            for(int root : roots){
                while(ms.isPlaying()) Thread.sleep(500);
                changeTonic(root);
                ms.play(score);
            }


        } catch (InvalidMidiDataException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void changeTonic(int nTonic){
        Mod.transpose(score, nTonic - tonic);
        tonic = nTonic;
    }

    public static void changeMode(int[] nMode) {
        if (nMode.length != mode.length) {
            throw new IllegalArgumentException("New mode must be the same size as previous mode.");
        }
        int[] diff = new int[nMode.length];
        for (int i = 0; i < nMode.length; i++) diff[i] = nMode[i] - mode[i];

        for (Part part : score.getPartArray())
            for (Phrase phrase : part.getPhraseArray())
                for (Note note : phrase.getNoteArray()) {
                    if (note.getPitchType() == Note.MIDI_PITCH) {
                        var pitch = note.getPitch();
                        var inScale = (pitch + tonic) % 12;
                        var ordInScale = Arrays.binarySearch(mode, inScale);
                        if (ordInScale >= 0)
                            Mod.transpose(note, diff[ordInScale]);
                    }
                }
        mode = nMode;
    }
}
