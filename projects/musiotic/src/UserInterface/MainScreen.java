package UserInterface;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainScreen extends BorderPane {
    public MainScreen(){
        var newSongBtn = new Button("Create New Song");
        var currSongsBtn = new Button("View ExistingSongs");
        var buttons = new VBox(200, newSongBtn, currSongsBtn);
        var center = new AnchorPane();
        center.getChildren().add(buttons);
        AnchorPane.setRightAnchor(buttons, 30.0);
        var name = new Label("Musiotic");
        name.setPadding(new Insets(10, 10, 10, 10));

        getStylesheets().add("projects/musiotic/assets/MainScreen.css");


        setTop(name);
        setCenter(center);
    }
}
