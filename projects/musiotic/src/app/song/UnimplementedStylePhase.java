package song;

import javafx.scene.Node;
import javafx.scene.text.Text;

public class UnimplementedStylePhase extends Phase{
    public UnimplementedStylePhase(SongManager manager) {
        super(manager);
    }

    @Override
    public Type getType() {
        return Type.UnimplementedStyle;
    }

    @Override
    public Node getScreen() {
        manager.stylePhase.completed.setValue(false);
        return new Text("This style has not yet been implemented! Try another style.");
    }
}
