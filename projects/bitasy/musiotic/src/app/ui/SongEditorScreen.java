package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import song.InstrumentalPhase;
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
//            System.out.println("SongEditorScreen: styleButton pressed");
            currentPhase = stylePhase;
            setCenter(stylePhase.getScreen());
        });

        menuButtons = new VBox();
        menuButtons.getChildren().add(styleButton);
        menuButtons.getStyleClass().add("menu-buttons");

//        var menu = new AnchorPane();
//        menu.getChildren().add(menuButtons);
//        menu.setId("menu");

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


        String playImage = getClass().getResource("/images/GitHub-Mark-120px-plus.png").toExternalForm();
        ImageView discordImage = new ImageView(new Image(playImage));
        Button discordButton = new Button("", discordImage);
        discordImage.setFitWidth(70);
        discordImage.setFitHeight(70);
        discordButton.setOnMouseClicked(e -> {
            try {
//                System.out.println("Going to github");
                Desktop.getDesktop().browse(new URI("https://github.com/bitasy/CodeWithFriends-Spring2020"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }
            e.consume();
        });
        discordButton.setId("discord-button");

        HBox actions = new HBox(previous, discordButton, next);
        actions.setAlignment(Pos.CENTER);
        actions.setId("actions"); // For future styling

        setTop(toolbar);
        setLeft(menuButtons);
        setBottom(actions);

        currentPhase = stylePhase;
        setCenter(stylePhase.getScreen());
    }

    public void populate(Collection<Phase> phases){
//        System.out.println("SongEditorScreen: populate(phases)");
        var buttons = new ArrayList<VBox>();

        for (var phase : phases) {
            if (phase == manager.stylePhase) continue;
            Button button = new Button(phase.getType().name);
            button.setOnMouseClicked(e -> {
//                System.out.println("currentPhase: " + currentPhase.getType().name);
                goToPhase(phase);
            });
            button.disableProperty().bind(phase.disabled);
            VBox box;
            if (!(phase instanceof InstrumentalPhase) ) {
                //No mute button
                box = new VBox(button);
            } else {
                //Add mute button
                var mute = new ToggleButton("");
                var soundOn = new ImageView(new Image(getClass().getResource("/images/sound_on.png").toExternalForm()));
                soundOn.setFitHeight(25);
                soundOn.setFitWidth(25);
                mute.setGraphic(soundOn);
                mute.setSelected(true);
                mute.disableProperty().bind(phase.completed.not());
                mute.setOnAction(e -> {
                    if (mute.isSelected()) {
                        ((InstrumentalPhase)phase).setMute(false);
                        var image = new ImageView(new Image(getClass().getResource("/images/sound_on.png").toExternalForm()));
                        image.setFitWidth(25);
                        image.setFitHeight(25);
                        mute.setGraphic(image);
                    } else {
                        ((InstrumentalPhase)phase).setMute(true);
                        var image = new ImageView(new Image(getClass().getResource("/images/sound_off.png").toExternalForm()));
                        image.setFitWidth(25);
                        image.setFitHeight(25);
                        mute.setGraphic(image);
                    }
                });
                // Set mute to right side
                var muteBox = new HBox(mute);
                muteBox.setAlignment(Pos.BASELINE_RIGHT);

                box = new VBox(button, muteBox);
                box.setSpacing(5);
            }
            buttons.add(box);
        }
        menuButtons.getChildren().remove(1, menuButtons.getChildren().size());
        menuButtons.getChildren().addAll(buttons);
        if (currentPhase == manager.stylePhase) {
            var next = manager.phaseMap.getNext(Phase.Type.Style);
            if(next != null){
                goToPhase(next);
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
