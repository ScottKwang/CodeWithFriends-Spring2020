package music;

import javafx.scene.Node;
import song.BassPhase;
import ui.MidiScreen;

public class BassSequence extends MIDISequence {
    private final MidiScreen screen;

    public BassSequence(BassPhase phase) {
        super(phase);
        screen = new MidiScreen(this);
    }




    @Override
    public Node getScreen() {
        return screen.getScreen();
    }
}
