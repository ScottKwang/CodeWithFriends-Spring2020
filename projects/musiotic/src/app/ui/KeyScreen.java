package ui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import song.KeyPhase;

public class KeyScreen {
    private final KeyPhase phase;
    private final Node screen;

    public KeyScreen(KeyPhase phase){
        this.phase = phase;

        Text description = new Text("Choose the options that will determine what your music sounds like." +
                " Hover over the option names for more information.");

        Label tonic = new Label("Tonic");
        Tooltip tonicTooltip = new Tooltip();
        tonicTooltip.setText("This determines the pitch that your music will start at. " +
                "Set it to match your vocal range if you'd like to sing along!");
        tonic.setTooltip(tonicTooltip);

        var tonicChoice = new ChoiceBox<String>();
        String[] notes = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        tonicChoice.getItems().addAll(notes);
        tonicChoice.setValue("C");

        Label scale = new Label("Scale");
        Tooltip scaleTooltip = new Tooltip();
        scaleTooltip.setText("This determines the atmosphere and mood of your music. " +
                "Try selecting different options once you have some music to work with!");
        scale.setTooltip(scaleTooltip);

        var modeChoice = new ChoiceBox<String>();
        String[] modes = new String[]{"Major", "Minor", "Harmonic Minor", "Lydian", "Mixolydian"};
        modeChoice.getItems().addAll(modes);
        modeChoice.setValue("Major");

        Button confirm = new Button("Confirm");
        confirm.setOnMouseClicked(e -> this.phase.setChoices(tonicChoice.getValue(), modeChoice.getValue()));

        screen = new VBox(
                description,
                new HBox(tonic, tonicChoice),
                new HBox(scale, modeChoice),
                confirm
        );
    }

    public Node getScreen(){
        return screen;
    }
}
