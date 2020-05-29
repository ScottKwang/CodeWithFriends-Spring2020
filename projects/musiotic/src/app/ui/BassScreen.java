package ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import song.BassPhase;

public class BassScreen extends MidiScreen {
    private final BassPhase phase;

    public BassScreen(BassPhase phase){
        super(phase);
        this.phase = phase;
        setBottom(new Label("Bass Screen")); // For example

    }
}
