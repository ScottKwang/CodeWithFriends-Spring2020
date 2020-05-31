package launcher;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Play;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.QUARTER_NOTE;
import static jm.constants.Pitches.REST;

public class DrumMIDIExample {
    public static void main(String[] args){
        Score pattern1 = new Score("JMDemo - Kit");
        Part drums = new Part("Drums", 0, 9); // 9 = MIDI channel 10
        Phrase phrBD = new Phrase(0.0);
        Phrase phrSD = new Phrase(0.0);
        Phrase phrHH = new Phrase(0.0);

        //Let us know things have started
//        System.out.println("Creating drum patterns . . .");

        // make bass drum
        for(int i=0;i<4;i++){
            Note note = new Note(36, QUARTER_NOTE);
            phrBD.addNote(note);
            Note rest = new Note(REST, QUARTER_NOTE); //rest
            phrBD.addNote(rest);
        }

        // make snare drum
        for(int i=0;i<4;i++){
            Note rest = new Note(REST, QUARTER_NOTE); //rest
            phrSD.addNote(rest);
            Note note = new Note(38, QUARTER_NOTE);
            phrSD.addNote(note);
        }

        // make hats
        for(int i=0;i<15;i++){
            Note note = new Note(42, EIGHTH_NOTE);
            phrHH.addNote(note);
        }
        Note note = new Note(46, EIGHTH_NOTE); // open hi hat
        phrHH.addNote(note);

        // loop the drum pattern for 16 bars
        Mod.repeat(phrBD, 7);
        Mod.repeat(phrSD, 7);
        Mod.repeat(phrHH, 7);

        // add phrases to the instrument (part)
        drums.addPhrase(phrBD);
        drums.addPhrase(phrSD);
        drums.addPhrase(phrHH);

        // add the drum part to a score.
        pattern1.addPart(drums);

        // write the score to a MIDIfile
        Play.midi(pattern1);
    }
}