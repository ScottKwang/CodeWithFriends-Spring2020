package song;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class SongManager {
    public final StylePhase stylePhase;
    public Map<Phase.Type, Phase> phaseMap;
    protected Phase currentPhase;
    private Score score;
    private int tonic;
    private int[] mode;


    public SongManager(){
        stylePhase = new StylePhase(this);
        currentPhase = stylePhase;
        score = new Score();
        tonic = JMC.C4;
        mode = JMC.MAJOR_SCALE;
    }

    private Consumer<Collection<Phase>> onStyleSelect;

    public void setOnStyleSelect(Consumer<Collection<Phase>> onStyleSelect){
        System.out.println("SongManager: setOnStyleSelect(onStyleSelect)");
        this.onStyleSelect = onStyleSelect;
    }

    public void populateStyle(){
        System.out.println("SongManager: populateStyle()");
        phaseMap = stylePhase.getPhases();
        onStyleSelect.accept(phaseMap.values());
    }

    public void goToPhase(Phase.Type type){

    }

    public void changeTonic(int tonic){
        Mod.transpose(score, tonic - this.tonic);
        this.tonic = tonic;
    }

    public void changeMode(int[] mode) {
        if (mode.length != this.mode.length) {
            throw new IllegalArgumentException("New mode must be the same size as previous mode.");
        }
        int[] diff = new int[mode.length];
        for (int i = 0; i < mode.length; i++) diff[i] = mode[i] - this.mode[i];

        for (Part part : score.getPartArray())
            for (Phrase phrase : part.getPhraseArray())
                for (Note note : phrase.getNoteArray()) {
                    if (note.getPitchType() == Note.MIDI_PITCH) {
                        var pitch = note.getPitch();
                        var inScale = (pitch + tonic) % 12;
                        var ordInScale = Arrays.binarySearch(this.mode, inScale);
                        if (ordInScale >= 0)
                            Mod.transpose(note, diff[ordInScale]);
                    }
                }

        this.mode = mode;
    }
}
