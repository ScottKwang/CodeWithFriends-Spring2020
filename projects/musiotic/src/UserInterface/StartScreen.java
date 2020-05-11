package UserInterface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.File;
import java.net.MalformedURLException;

public class StartScreen extends BorderPane {
    public StartScreen(){
        try {
            String styleSheet = new File(System.getProperty("user.dir") + "\\projects\\musiotic\\assets\\css\\start_screen.css").toURI().toURL().toString();
            getStylesheets().add(styleSheet);
        } catch (MalformedURLException e) {
            System.out.println(e.toString());
        }

//        setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET, null, null)));
        var newSongBtn = new Button("Create New Song");
        newSongBtn.setOnAction(e -> {
            MainScreen mainScreen = new MainScreen();
            newSongBtn.getScene().setRoot(mainScreen);
        });

        var currSongsBtn = new Button("View Existing Songs");
        var buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(100);
        buttons.setPrefWidth(800);
        buttons.setMaxWidth(800);
        buttons.setMaxHeight(Double.MAX_VALUE);
        buttons.setPadding(new Insets(-200, 150, 0, 150));
        newSongBtn.setMaxWidth(Double.MAX_VALUE);
        currSongsBtn.setMaxWidth(Double.MAX_VALUE);
        buttons.getChildren().addAll(newSongBtn, currSongsBtn);

        var center = new HBox();
        center.setPadding(new Insets(20, 0, 0, 100));
        var image = new ImageView();
        image.setFitWidth(400);
        image.setPreserveRatio(true);
        File musicNote = new File("projects/musiotic/assets/images/music_note.png");
        image.setImage(new Image(musicNote.toURI().toString()));

        center.getChildren().addAll(image, buttons);

        var name = new Label("Musiotic");
        name.setMaxWidth(Double.MAX_VALUE);
        name.setAlignment(Pos.CENTER);
        name.setFont(Font.font("sans-serif", 90));
        name.setTextFill(Paint.valueOf("black"));
        setTop(name);
        setCenter(center);
    }
}
