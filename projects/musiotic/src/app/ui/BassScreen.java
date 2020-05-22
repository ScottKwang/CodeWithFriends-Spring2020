package ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import song.BassPhase;

public class BassScreen extends MidiScreen {
    private final BassPhase phase;
    private final Node screen;
    private final MidiGrid midiGrid;

    public BassScreen(BassPhase phase){
        super(phase);
        this.phase = phase;
        midiGrid = new MidiGrid(phase);
        screen = new Label("Bass");
    }

    public Node getScreen(){
        return screen;
    }

    public MidiGrid getMidiGrid() {
        return midiGrid;
    }
}
