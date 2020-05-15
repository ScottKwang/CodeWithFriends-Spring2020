package song;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class KeyPhase extends Phase {
    public KeyPhase(SongManager manager) {
        super(manager);
    }

    public String key;

    @Override
    public Type getType() {
        return Type.Key;
    }

    @Override
    public Node getScreen() {
        Button CButton = new Button ("C");
        Button DButton = new Button ("D");
        Button AButton = new Button ("A");
        VBox testButtons = new VBox(CButton, DButton, AButton);

        CButton.setOnMouseClicked(e -> {
            key = "C";
        });
        DButton.setOnMouseClicked(e -> {
            key = "D";
        });
        AButton.setOnMouseClicked(e -> {
            key = "A";
        });

        return testButtons;
    }
}
