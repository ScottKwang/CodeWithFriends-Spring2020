package ui;

import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import song.Phase;
import song.SongManager;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
        menu.setId("menu");

        ToolBar toolbar = new ToolBar();

        Button next = new Button("Next");
        next.disableProperty().bind(manager.nextAvailable().not());
        next.setOnMouseClicked(e -> {
            var nextPhase = manager.phaseMap.getNext(currentPhase.getType());
            goToPhase(nextPhase);
        });
        Button previous = new Button("Previous");
        previous.disableProperty().bind(manager.prevAvailable().not());
        previous.setOnMouseClicked(e -> {
            var prevPhase = manager.phaseMap.getPrev(currentPhase.getType());
            goToPhase(prevPhase);
        });

        //Created for spacing previous to left, right to right
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        String playImage = getClass().getResource("/images/GitHub-Mark-120px-plus.png").toExternalForm();
        ImageView discordImage = new ImageView(new Image(playImage));
        Button discordButton = new Button("", discordImage);
        discordImage.setFitWidth(75);
        discordImage.setFitHeight(75);
        discordButton.setOnMouseClicked(e -> {
            try {
                System.out.println("Going to github");
                Desktop.getDesktop().browse(new URI("https://github.com/bitasy/CodeWithFriends-Spring2020"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            e.consume();
        });
        discordButton.setId("discord-button");

        HBox actions = new HBox(previous, region1, discordButton, region2, next);
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
            var keyPhase = manager.phaseMap.get(Phase.Type.Key); // Does every type have a key? this may return null
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
