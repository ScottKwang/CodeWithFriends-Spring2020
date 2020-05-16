package song;

import javafx.scene.Node;
import ui.KeyScreen;

public class KeyPhase extends Phase {
    private final KeyScreen screen;

    public KeyPhase(SongManager manager) {
        super(manager);
        screen = new KeyScreen(this);
    }

    public String key; // todo separate into root and major/minor
    // to allow for easier transposition (from CM to DM or from CM to Cm for example)

    @Override
    public Type getType() {
        return Type.Key;
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }


    public void setKey(String key) {
        this.key = key;
        completed.setValue(true);
    }
}
