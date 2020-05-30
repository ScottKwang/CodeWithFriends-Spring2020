package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import song.Style;
import song.StylePhase;

public class StyleScreen extends OptionScreen {
    private final StylePhase phase;

    public StyleScreen(StylePhase phase){
        super(phase);
        this.phase = phase;

        var choices = new ChoiceBox<String>();
        for(Style style : Style.values()){
            choices.getItems().add(style.name);
        }
        var content = new VBox();
        var label = new Label("Choose a style of music. (This can't be changed!)");
        label.setId("title");
        var complete = new Button("Confirm");
        complete.setOnMouseClicked(e -> {
//            System.out.println("StyleScreen: \"next\" button clicked");
            choices.disableProperty().bind(phase.completed);
            phase.setStyle(Style.getStyle(choices.getValue()));
        });
        complete.disableProperty().bind(choices.valueProperty().isNull());
        complete.visibleProperty().bind(phase.completed.not());
        content.getChildren().addAll(label, choices, complete);
        content.setId("content");
        setCenter(content);
    }


}
