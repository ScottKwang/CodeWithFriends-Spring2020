package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import song.Style;
import song.StylePhase;

public class StyleScreen {
    private final StylePhase phase;
    private Node screen;

    public StyleScreen(StylePhase phase){
        this.phase = phase;
        var content = new VBox();
        var label = new Label("Style");
        var complete = new Button("Next");
        complete.setOnMouseClicked(e -> phase.setStyle(Style.PIANO));
        content.getChildren().addAll(label, complete);
        screen = content;
    }

    public Node getScreen(){
        return screen;
    }
}
