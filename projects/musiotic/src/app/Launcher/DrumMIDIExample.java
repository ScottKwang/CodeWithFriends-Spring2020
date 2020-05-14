package Launcher;

import jm.audio.Instrument;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;

import static jm.constants.Frequencies.FRQ;
import static jm.constants.Pitches.REST;
import MusicComponents.SimpleSampleInst;

import java.io.File;

public class DrumMIDIExample {
    public static void main(String[] args){
        Score s = new Score("title");
        Part kickPart = new Part("Kick", 0, 9);
        Part snarePart = new Part("Snare", 1, 9);
        Part hatsPart = new Part("Hats", 2, 9);
        Part openHatsPart = new Part("OpenHats", 3, 9);
        Phrase kickPhrase = new Phrase();
        Phrase snarePhrase = new Phrase();
        Phrase hatsPhrase = new Phrase();
        Phrase openHatsPhrase = new Phrase();

        File f = new File("/audio/kicks/ac_kick_6.au");
        f.canRead();

        SimpleSampleInst kickInst = new SimpleSampleInst("/audio/kicks/ac_kick_6.au", FRQ[36]);
//        SimpleSampleInst snareInst = new SimpleSampleInst("Snare.au", FRQ[38]);
//        SimpleSampleInst hatsInst = new SimpleSampleInst("Hats.au", FRQ[42]);
//        SimpleSampleInst openHatsInst = new SimpleSampleInst("HHOpen.au", FRQ[46]);
        Instrument[] drumKit = {kickInst};//, snareInst, hatsInst, openHatsInst};
//
        // kick
        for(int i=0;i<2;i++) {
            Note n = new Note(36,2.0);
            kickPhrase.addNote(n);
        }
//
//        //snare
//        for(int i=0;i<2;i++) {
//            Note r = new Note(REST,1.0);
//            snarePhrase.addNote(r);
//            Note n = new Note(38,1.0);
//            n.setDuration(n.getRhythmValue());
//            snarePhrase.addNote(n);
//        }
//
//        //hats
//        for(int i=0;i<16;i++) {
//            if(i%2 == 1 && Math.random() < 0.1) {
//                Note n = new Note(46,0.25, (int)(Math.random()*127));
//                n.setDuration(n.getRhythmValue());
//                openHatsPhrase.addNote(n);
//                Note r = new Note(REST, 0.25);
//                hatsPhrase.addNote(r);
//            } else {
//                Note n = new Note(42,0.25, (int)(Math.random()*127));
//                n.setDuration(n.getRhythmValue());
//                hatsPhrase.addNote(n);
//                Note r = new Note(REST, 0.25);
//                openHatsPhrase.addNote(r);
//            }
//        }
//
        //pack
        kickPart.addPhrase(kickPhrase);
//        snarePart.addPhrase(snarePhrase);
//        hatsPart.addPhrase(hatsPhrase);
//        openHatsPart.addPhrase(openHatsPhrase);
//
        s.addPart(kickPart);
//        s.addPart(snarePart);
//        s.addPart(hatsPart);
//        s.addPart(openHatsPart);
//
//        //write midi
        Write.midi(s, "/app/AudioDrums.mid");
//
//        //write au
//        Write.au(s, "/audio/AudioDrums.au", drumKit);
    }
}
