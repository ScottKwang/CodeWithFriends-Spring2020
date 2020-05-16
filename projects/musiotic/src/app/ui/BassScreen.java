package ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import song.BassPhase;

public class BassScreen {
    private final BassPhase phase;
    private final Node screen;

    public BassScreen(BassPhase phase){
        this.phase = phase;
        screen = new Label("Bass");
    }

    public Node getScreen(){
        return screen;
    }
}
