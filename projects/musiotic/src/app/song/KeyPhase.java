package song;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class KeyPhase extends Phase {
    public KeyPhase(SongManager manager) {
        super(manager);
    }

    @Override
    public Type getType() {
        return Type.Key;
    }

    @Override
    public Node getScreen() {
        return new Label("Key");
    }
}
