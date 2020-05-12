package UserInterface;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CreateSongScreen extends BorderPane {
    public CreateSongScreen(){
        String styleSheet = getClass().getResource("/css/create_song_screen.css").toExternalForm();
        getStylesheets().add(styleSheet);

        ToggleButton toggleButtonStyle = new ToggleButton("Style");
        ToggleButton toggleButtonKey = new ToggleButton("Key");
        ToggleButton toggleButtonMelody = new ToggleButton("Melody");
        ToggleButton toggleButtonBass = new ToggleButton("Bass");
        ToggleButton toggleButtonDrums = new ToggleButton("Drums");
        ToggleGroup toggleGroup = new ToggleGroup();

        toggleButtonStyle.setToggleGroup(toggleGroup);
        toggleButtonKey.setToggleGroup(toggleGroup);
        toggleButtonMelody.setToggleGroup(toggleGroup);
        toggleButtonBass.setToggleGroup(toggleGroup);
        toggleButtonDrums.setToggleGroup(toggleGroup);

        toggleButtonStyle.setOnAction(e -> { onToggleButtonClick(toggleButtonStyle); });
        toggleButtonKey.setOnAction(e -> { onToggleButtonClick(toggleButtonKey); });
        toggleButtonMelody.setOnAction(e -> { onToggleButtonClick(toggleButtonMelody); });
        toggleButtonBass.setOnAction(e -> { onToggleButtonClick(toggleButtonBass); });
        toggleButtonDrums.setOnAction(e -> { onToggleButtonClick(toggleButtonDrums); });

        VBox leftMenu = new VBox(toggleButtonStyle,
                toggleButtonKey,
                toggleButtonMelody,
                toggleButtonBass,
                toggleButtonDrums);

        var left = new AnchorPane();
        left.getChildren().add(leftMenu);

        ToolBar toolbar = new ToolBar();
        setTop(toolbar);
        //setCenter(center);
        setLeft(left);
    }

    private void onToggleButtonClick(ToggleButton button) {
        if (button.isSelected()) {
            System.out.println(button.getText() + " Toggle Opened");
        } else {
            System.out.println(button.getText() + " Toggle Closed");
        }
    }

}
