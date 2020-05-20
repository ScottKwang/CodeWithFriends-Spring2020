package music;

import javafx.scene.Node;
import song.Melody1Phase;
import ui.MidiScreen;

public class Melody1Sequence extends MIDISequence {
    private final MidiScreen screen;

    public Melody1Sequence(Melody1Phase phase) {
        super(phase);
        screen = new MidiScreen(this);
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }
}
