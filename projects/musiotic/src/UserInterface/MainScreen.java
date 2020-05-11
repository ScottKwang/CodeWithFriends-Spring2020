package UserInterface;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.MalformedURLException;

public class MainScreen extends BorderPane {
    public MainScreen(){
//        var newSongBtn = new Button("Create New Song");
//        var currSongsBtn = new Button("View ExistingSongs");
//        var buttons = new VBox(200, newSongBtn, currSongsBtn);
//        var center = new AnchorPane();
//        center.getChildren().add(buttons);
//        AnchorPane.setRightAnchor(buttons, 30.0);

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






        // @ brian don't ask, fix it if u can LOL
        // Set style sheet to Main Screen.
        try {
            String styleSheet = new File(System.getProperty("user.dir") + "\\projects\\musiotic\\assets\\css\\main_screen.css").toURI().toURL().toString();
            getStylesheets().add(styleSheet);
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
        }


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
