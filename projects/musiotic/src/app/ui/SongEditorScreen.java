package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import song.Phase;
import song.SongManager;

import java.util.ArrayList;
import java.util.Collection;

public class SongEditorScreen extends BorderPane {

    private final SongManager manager;
    private final VBox menuButtons;
    public Phase currentPhase;

    public SongEditorScreen(SongManager manager) {
        this.manager = manager;
        String styleSheet = getClass().getResource("/css/song_editor_screen.css").toExternalForm();
        getStylesheets().add(styleSheet);

        var stylePhase = manager.stylePhase;
        Button styleButton = new Button(stylePhase.getType().name);
        styleButton.setOnMouseClicked(e -> {
            System.out.println("SongEditorScreen: styleButton pressed");
            currentPhase = stylePhase;
            setCenter(stylePhase.getScreen());
        });

        menuButtons = new VBox();
        menuButtons.getChildren().add(styleButton);
        menuButtons.getStyleClass().add("menu-buttons");

        var menu = new AnchorPane();
        menu.getChildren().add(menuButtons);

        ToolBar toolbar = new ToolBar();

        Button next = new Button("Next");
        next.disableProperty().bind(manager.nextAvailable().not());
        next.setOnMouseClicked(e -> {
            var nextPhase = manager.phaseList.getNext(currentPhase.getType());
            goToPhase(nextPhase);
        });
        Button previous = new Button("Previous");
        previous.disableProperty().bind(manager.prevAvailable().not());
        previous.setOnMouseClicked(e -> {
            var prevPhase = manager.phaseList.getPrev(currentPhase.getType());
            goToPhase(prevPhase);
        });
        HBox actions = new HBox(previous, next);
        actions.setId("actions"); // For future styling

        setTop(toolbar);
        setLeft(menu);
        setBottom(actions);

        currentPhase = stylePhase;
        setCenter(stylePhase.getScreen());
    }

    public void populate(Collection<Phase> phases){
        System.out.println("SongEditorScreen: populate(phases)");
        var buttons = new ArrayList<Button>();
        for(var phase : phases){
            if(phase == manager.stylePhase) continue;
            Button button = new Button(phase.getType().name);
            button.setOnMouseClicked(e -> {
                System.out.println("currentPhase: " + currentPhase.getType().name);
                goToPhase(phase);
            });
            button.disableProperty().bind(phase.disabled);
            buttons.add(button);
        }
        menuButtons.getChildren().addAll(buttons);
        if (currentPhase == manager.stylePhase) {
            var keyPhase = manager.phaseList.get(Phase.Type.Key); // Does every type have a key? this may return null
            if(keyPhase != null){
                goToPhase(keyPhase);
            }
        }
    }

    private void goToPhase(Phase phase){
        updatePhase(phase);
        manager.updatePhase(phase);
    }

    public void updatePhase(Phase phase){
        currentPhase = phase;
        setCenter(phase.getScreen());
    }


}
