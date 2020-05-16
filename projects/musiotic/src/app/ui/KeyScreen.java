package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import song.KeyPhase;

public class KeyScreen {
    private final KeyPhase phase;
    private final Node screen;

    private class KeyButton extends Button {
        KeyButton(String myKey){
            super(myKey);
            setOnMouseClicked(e -> phase.setKey(myKey));
        }
    }

    public KeyScreen(KeyPhase phase){
        this.phase = phase;
        Button cButton = new KeyButton("C");
        Button dButton = new KeyButton("D");
        Button aButton = new KeyButton("A");

        screen = new VBox(cButton, dButton, aButton);
    }

    public Node getScreen(){
        return screen;
    }
}
