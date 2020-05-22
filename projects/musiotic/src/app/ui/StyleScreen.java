package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import song.Style;
import song.StylePhase;

public class StyleScreen implements Screen{
    private final StylePhase phase;
    private final Node screen;

    public StyleScreen(StylePhase phase){
        this.phase = phase;
        var choices = new ChoiceBox<String>();
        for(Style style : Style.values()){
            choices.getItems().add(style.name);
        }
        var content = new VBox();
        var label = new Label("Style");
        var complete = new Button("Confirm");
        complete.setOnMouseClicked(e -> {
            System.out.println("StyleScreen: \"next\" button clicked");
            choices.disableProperty().setValue(true);
            phase.setStyle(Style.getStyle(choices.getValue()));
        });
        complete.disableProperty().bind(choices.valueProperty().isNull());
        complete.visibleProperty().bind(phase.completed.not());
        content.getChildren().addAll(label, choices, complete);
        screen = content;
    }

    public Node getScreen(){
        System.out.println("StyleScreen: getScreen()");
        return screen;
    }
}
