package song;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Play;
import ui.SongEditorScreen;
import util.MappedLinkedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongManager {
    public final StylePhase stylePhase;
    private SongEditorScreen screen;
    public MappedLinkedList<Phase.Type, Phase> phaseMap;
    private Property<Phase> currentPhase;
    private final BooleanProperty nextAvailable;
    private final BooleanProperty prevAvailable;
    private Score score;
    private int tonic;
    private int[] mode;

    // Number of notes a user can input midi for. 8 is one octave.
    public int numNotes;
    // How many measures the MIDI Sequence will have. Default: 4 measures (4 bar loops).
    public int numMeasures;

    public SongManager(){
        stylePhase = new StylePhase(this);
        currentPhase = new SimpleObjectProperty<>();
        currentPhase.setValue(stylePhase);
        nextAvailable = new SimpleBooleanProperty();
        nextAvailable.bind(stylePhase.completed);
        prevAvailable = new SimpleBooleanProperty();
        prevAvailable.bind(Bindings.createBooleanBinding(
                () -> !currentPhase.getValue().equals(stylePhase),
                currentPhase
        ));
        numNotes = 8;
        numMeasures = 4;
        score = new Score();
        tonic = JMC.C4;
        mode = JMC.MAJOR_SCALE;
    }

    public void setScreen(SongEditorScreen screen){
        this.screen = screen;
    }

    public void populateStyle(){
        if(screen == null) throw new IllegalStateException("SongEditorScreen not set.");
        System.out.println("SongManager: populateStyle()");
        var phases = stylePhase.getPhases();
        phaseMap = new MappedLinkedList<>(phases);
        screen.populate(phases.values());
        addMeasuresRight();
    }

    public void goToPhase(Phase phase){
        if(screen == null) throw new IllegalStateException("SongEditorScreen not set.");
        screen.updatePhase(updatePhase(phase));
    }

    public Phase updatePhase(Phase phase){
        var next = phaseMap.getNext(phase.getType());
        nextAvailable.bind(next == null ? new SimpleBooleanProperty(false) : next.disabled.not());
        currentPhase.setValue(phase);
        return phase;
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

    public BooleanProperty nextAvailable() {
        return nextAvailable;
    }

    public BooleanProperty prevAvailable() {
        return prevAvailable;
    }

    public void addMeasures(boolean right) {
        this.numMeasures += 4;
        System.out.println("The number of Measures this MIDI has is: " + numMeasures);
        if (right) addMeasuresRight();
        else addMeasuresLeft();
    }

    private void addMeasuresLeft() {
        Score rest = score.copy();
        for(Part part : rest.getPartArray()){
            part.clean();
            for(int i = 0; i < 4; i++){
                part.addNote(
                        new Note(JMC.REST, JMC.WHOLE_NOTE), part.getEndTime()
                );
            }
        }
        Mod.merge(rest, score);
    }

    private void addMeasuresRight() {
        for(Part part : score.getPartArray()){
            part.addNote(new Note(JMC.REST, JMC.WHOLE_NOTE), part.getEndTime());
        }
    }

    public List<StringProperty> getScale() {
        var keyPhase = (KeyPhase) phaseMap.get(Phase.Type.Key);
        if(keyPhase == null) return null;
        return keyPhase.getScale();
    }

    public void addPart(Part part) {
        score.add(part);
    }

    public void play(int measureNum){
        Phase curr = phaseMap.getNext(Phase.Type.Style);
        List<Part> oldParts = new ArrayList<>();
        while(curr != null){
            if(curr instanceof InstrumentalPhase){
                Part part = ((InstrumentalPhase) curr).part;
                Part oldPart = part.copy();
                var currPhrases = part.getPhraseList();
                currPhrases.removeAll(currPhrases.subList(0, measureNum));
                ((InstrumentalPhase) curr).connectMeasures(); // Prepare part for playing
                ((InstrumentalPhase) curr).part = oldPart; // Reset phase's part for continued editing
                oldParts.add(oldPart);
                Mod.consolidate(part);
            }
            curr = phaseMap.getNext(curr.getType());
        }

        Play.midi(score);
        score.removeAllParts();
        for(Part part : oldParts) score.addPart(part);
    }
}
