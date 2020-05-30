package ui;

import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import jm.JMC;
import jm.util.Play;
import song.InstrumentationPhase;
import song.Phase;
import util.PlayFixed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class InstrumentationScreen extends OptionScreen {
    private final InstrumentationPhase phase;
    private final List<ChoiceBox> choices;

    private static final Map<String, Integer> INSTRUMENT_VALS = Map.of(
            "Grand Piano", JMC.PIANO,
            "Bright Acoustic", JMC.BRIGHT_ACOUSTIC,
            "Honkytonk Piano", JMC.HONKYTONK_PIANO,
            "Electric Piano", JMC.ELECTRIC_PIANO,
            "Thumb Piano", JMC.THUMB_PIANO
    );

    private static final Map<Integer, String> INSTRUMENT_NAMES = Map.of(
            JMC.PIANO,"Grand Piano",
            JMC.BRIGHT_ACOUSTIC,"Bright Acoustic",
            JMC.HONKYTONK_PIANO,"Honkytonk Piano",
            JMC.ELECTRIC_PIANO,"Electric Piano",
            JMC.THUMB_PIANO,"Thumb Piano"
    );

    public InstrumentationScreen(InstrumentationPhase phase, Map<String, Pair<IntegerProperty, List<Integer>>> map){
        super(phase);
        this.phase = phase;
        choices = new ArrayList<>();
        List<HBox> groups = new ArrayList<>();
        for(var entry : map.entrySet()){
            ChoiceBox<String> choice = new ChoiceBox<>(
                    entry.getValue().getValue().stream().collect(
                            FXCollections::observableArrayList,
                            (list, val) -> list.add(INSTRUMENT_NAMES.get(val)),
                            List::addAll)
            );
            choice.setValue(choice.getItems().get(0));
            IntegerProperty instVal = entry.getValue().getKey();
            choice.valueProperty().addListener((e, oldV, newV) -> {
                instVal.setValue(INSTRUMENT_VALS.get(newV));
            });
            HBox groupBox = new HBox(new Label(entry.getKey()), choice);
            groupBox.setId("inner-content");
            groups.add(groupBox);
        }
        Button playScore = new Button();
        playScore.setPadding(Insets.EMPTY);
        playScore.setMaxWidth(50);
        playScore.setMaxHeight(50);
        Image playImage = new Image(getClass().getResourceAsStream("/images/play.png"), 50, 50, true, true);
        playScore.setGraphic(new ImageView(playImage));
        AtomicBoolean isPlaying = new AtomicBoolean(false);
        playScore.setOnMouseClicked(e -> {
            if(isPlaying.get()) PlayFixed.stopMidi();
            else phase.manager.play(0);
        });
        playScore.visibleProperty().bind(phase.manager.phaseMap.get(Phase.Type.Melody1).completed);
        Tooltip tooltip = new Tooltip("Listen to what your music sounds like with these instruments.");
        playScore.setTooltip(tooltip);


        Button confirm = new Button("Confirm");
        confirm.setPadding(Insets.EMPTY);
        confirm.setOnMouseClicked(e -> {
            phase.manager.goToPhase(phase.manager.phaseMap.getNext(phase.getType()));
            Play.stopMidiCycle();
            PlayFixed.stopMidi();
        });


        Text desc = new Text("Choose which instruments you'd like to play your music!");
        List<Node> elements = new ArrayList<>();
        elements.add(desc);
        elements.addAll(groups);
        elements.add(playScore);
        elements.add(confirm);

        desc.setId("inner-content");
        desc.setId("title");
        playScore.setId("inner-content");

        VBox box = new VBox(elements.toArray(new Node[0]));
        box.setId("content");

        setCenter(box);
        phase.completed.setValue(true);
    }

}
