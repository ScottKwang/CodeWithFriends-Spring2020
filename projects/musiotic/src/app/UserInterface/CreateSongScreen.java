package UserInterface;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class CreateSongScreen extends BorderPane {

    public String menuState = "Style";

    public CreateSongScreen() {
        String styleSheet = getClass().getResource("/css/create_song_screen.css").toExternalForm();
        getStylesheets().add(styleSheet);

        Button styleBtn = new Button("Style");
        Button KeyBtn = new Button("Key");
        Button MelodyBtn = new Button("Melody");
        Button BassBtn = new Button("Bass");
        Button DrumsBtn = new Button("Drums");

        styleBtn.setOnMouseClicked(e -> {    onButtonClick(styleBtn);   });
        KeyBtn.setOnMouseClicked(e -> {      onButtonClick(KeyBtn);     });
        MelodyBtn.setOnMouseClicked(e -> {   onButtonClick(MelodyBtn);  });
        BassBtn.setOnMouseClicked(e -> {     onButtonClick(BassBtn);    });
        DrumsBtn.setOnMouseClicked(e -> {    onButtonClick(DrumsBtn);   });

        VBox menuButtons = new VBox(styleBtn,
                KeyBtn,
                MelodyBtn,
                BassBtn,
                DrumsBtn);

        menuButtons.getStyleClass().add("menu-buttons");

        var left = new AnchorPane();
        left.getChildren().add(menuButtons);

        ToolBar toolbar = new ToolBar();
        setTop(toolbar);
        //setCenter(center);
        setLeft(left);
    }

    private void onButtonClick(Button button) {
        menuState = button.getText();
        System.out.println("Menu State: " + menuState);
    }

}
