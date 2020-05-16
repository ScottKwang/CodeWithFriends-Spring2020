package ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import song.Melody1Phase;

public class Melody1Screen {
    private final Melody1Phase phase;
    private final Node screen;

    public Melody1Screen(Melody1Phase phase){
        this.phase = phase;
        screen = new Label("Melody");
    }

    public Node getScreen(){
        return screen;
    }
}
