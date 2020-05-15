package song;

import javafx.scene.Node;
import javafx.scene.control.Label;

public class Melody1Phase extends Phase {
    public Melody1Phase(SongManager manager) {
        super(manager);
    }

    @Override
    public Type getType() {
        return Type.Melody1;
    }

    @Override
    public Node getScreen() {
        return new Label("Melody");
    }
}
