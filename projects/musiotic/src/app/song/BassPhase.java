package song;

import javafx.scene.Node;
import ui.BassScreen;

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
    public void addNote(String noteName, int noteLength, int notePosition) {

    }

    @Override
    public void deleteNote(String noteName, int noteLength, int notePosition) {

    }
}
