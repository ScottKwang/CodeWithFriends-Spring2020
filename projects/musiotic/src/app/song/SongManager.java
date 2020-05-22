package song;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import ui.SongEditorScreen;
import util.MappedLinkedList;

import java.util.ArrayList;

public class SongManager {
    public final StylePhase stylePhase;
    private SongEditorScreen screen;
    public MappedLinkedList<Phase.Type, Phase> phaseList;
    private Property<Phase> currentPhase;
    private final BooleanProperty nextAvailable;
    private final BooleanProperty prevAvailable;

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
    }

    public void setScreen(SongEditorScreen screen){
        this.screen = screen;
    }

    public void populateStyle(){
        if(screen == null) throw new IllegalStateException("SongEditorScreen not set.");
        System.out.println("SongManager: populateStyle()");
        var phases = stylePhase.getPhases();
        phaseList = new MappedLinkedList<>(phases);
        screen.populate(phases.values());
    }

    public void goToPhase(Phase phase){
        if(screen == null) throw new IllegalStateException("SongEditorScreen not set.");
        screen.updatePhase(updatePhase(phase));
    }

    public Phase updatePhase(Phase phase){
        var next = phaseList.getNext(phase.getType());
        nextAvailable.bind(next == null ? new SimpleBooleanProperty(false) : next.disabled.not());
        currentPhase.setValue(phase);
        return phase;
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
        //TODO:
    }

    private void addMeasuresRight() {
        //TODO:
    }

    public ArrayList<String> getScale() {
        return ((KeyPhase) phaseList.get(Phase.Type.Key)).getScale();
    }
}
