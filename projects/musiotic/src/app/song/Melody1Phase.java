package song;

import javafx.scene.Node;
import ui.Melody1Screen;
import ui.MidiScreen;

public class Melody1Phase extends Phase {
    private final Melody1Screen screen;

    public Melody1Phase(SongManager manager) {
        super(manager);
        screen = new Melody1Screen(this);
    }

    @Override
    public Type getType() {
        return Type.Melody1;
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
