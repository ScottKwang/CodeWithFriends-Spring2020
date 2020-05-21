package song;

import javafx.scene.Node;
import ui.BassScreen;
import ui.MidiScreen;

public class BassPhase extends Phase {
    private final BassScreen screen;

    public BassPhase(SongManager manager) {
        super(manager);
        screen = new BassScreen(this);
    }

    @Override
    public Type getType() {
        return Type.Bass;
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }

    @Override
    public MidiScreen getMidiScreen() {
        return screen.getMidiScreen();
    }
}
