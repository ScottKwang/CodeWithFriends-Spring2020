package UserInterface;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartScreen extends BorderPane {
    public StartScreen(){
        String styleSheet = getClass().getResource("/css/start_screen.css").toExternalForm();
        getStylesheets().add(styleSheet);

        var newSongBtn = new Button("Create New Song");
        newSongBtn.setOnAction(e -> {
            CreateSongScreen mainScreen = new CreateSongScreen();
            newSongBtn.getScene().setRoot(mainScreen);
        });

        var currSongsBtn = new Button("View Existing Songs");
        var buttons = new VBox();
        buttons.getStyleClass().add("buttons");
        buttons.getChildren().addAll(newSongBtn, currSongsBtn);

        var center = new HBox();
        center.getStyleClass().add("center");
        var image = new ImageView();
        image.setFitWidth(400);
        image.setPreserveRatio(true);
        String musicNote = getClass().getResource("/images/music_note.png").toExternalForm();
        image.setImage(new Image(musicNote));

        center.getChildren().addAll(image, buttons);

        var name = new Label("Musiotic");
        name.getStyleClass().add("name");
        setTop(name);
        setCenter(center);
    }
}
