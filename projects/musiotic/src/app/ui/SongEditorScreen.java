package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import song.Phase;
import song.SongManager;

import java.util.ArrayList;
import java.util.List;

public class SongEditorScreen extends BorderPane {

    private final SongManager manager;
    public Phase currentPhase;
    private final VBox menuButtons;

    public SongEditorScreen(SongManager manager) {
        this.manager = manager;
        String styleSheet = getClass().getResource("/css/song_editor_screen.css").toExternalForm();
        getStylesheets().add(styleSheet);

        var stylePhase = manager.stylePhase;
        Button button = new Button(stylePhase.getType().name);
        button.setOnMouseClicked(e -> {
            currentPhase = stylePhase;
            setCenter(stylePhase.getScreen());
        });

        menuButtons = new VBox();
        menuButtons.getChildren().add(button);
        menuButtons.getStyleClass().add("menu-buttons");

        var menu = new AnchorPane();
        menu.getChildren().add(menuButtons);

        ToolBar toolbar = new ToolBar();
        setTop(toolbar);
        setLeft(menu);

        currentPhase = stylePhase;
        setCenter(stylePhase.getScreen());

        manager.setOnStyleSelect(this::populate);
    }

    private void populate(List<Phase> phases){
        var buttons = new ArrayList<Button>();
        for(var phase : phases){
            Button button = new Button(phase.getType().name);
            button.setOnMouseClicked(e -> {
                currentPhase = phase;
                setCenter(phase.getScreen());
            });
            button.disableProperty().bind(phase.disabled);
            buttons.add(button);
        }
        menuButtons.getChildren().addAll(buttons);
    }


}
