package ui;

import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import song.KeyPhase;

public class KeyScreen implements Screen{
    private final KeyPhase phase;
    private final Node screen;

    public KeyScreen(KeyPhase phase){
        this.phase = phase;

        Text description = new Text("Choose the options that will determine what your music sounds like." +
                " Hover over the option names for more information. You can change these settings at any time.");

        Label tonic = new Label("Tonic");
        Tooltip tonicTooltip = new Tooltip();
        tonicTooltip.setText("This determines the pitch that your music will start at. " +
                "Set it to match your vocal range if you'd like to sing along!");
        tonic.setTooltip(tonicTooltip);

        var tonicChoice = new ChoiceBox<String>();
        String[] notes = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        tonicChoice.getItems().addAll(notes);
        tonicChoice.setValue("C");

        Label scale = new Label("Scale");
        Tooltip scaleTooltip = new Tooltip();
        scaleTooltip.setText("This determines the atmosphere and mood of your music. " +
                "Try selecting different options once you have some music to work with!");
        scale.setTooltip(scaleTooltip);

        var modeChoice = new ChoiceBox<String>();
        String[] modes = new String[]{"Major", "Minor", "Harmonic Minor", "Lydian", "Mixolydian"};
        modeChoice.getItems().addAll(modes);
        modeChoice.setValue("Major");

        Label tempo = new Label("Tempo");
        Tooltip tempoTooltip = new Tooltip();
        tempoTooltip.setText("This determines the speed of the music in beats per minute. Listen to how fast your " +
                "selection is with the play button!");
        tempo.setTooltip(tempoTooltip);


        var tempoChoice = new Slider(30, 150, 90);
        tempoChoice.valueProperty().addListener((obs, oldval, newVal) ->
                tempoChoice.setValue(Math.round(newVal.doubleValue())));
        tempoChoice.setBlockIncrement(1);
        var tempoDisplay = new Text();
        tempoDisplay.textProperty().bind(Bindings.createStringBinding(
                () -> (int) (tempoChoice.valueProperty().doubleValue() + 0.5) + " BPM",
                tempoChoice.valueProperty())
        );
        var tempoSample = new Button();
        tempoSample.setMaxWidth(16);
        tempoSample.setMaxHeight(16);
        Image playImage = new Image(getClass().getResourceAsStream("/images/play.png"), 16, 16, true, true);
        tempoSample.setGraphic(new ImageView(playImage));
        Part metronome = new Part("Metronome", 0, 9);
        metronome.addPhrase(new Phrase(new Note[]{
                new Note(75, 1, 120),
                new Note(76, 1, 100),
                new Note(76, 1, 100),
                new Note(76, 1, 100)
        }));
        Score sample = new Score(metronome);
        tempoSample.setOnMouseClicked(e -> {
            if(Play.cycleIsPlaying()) {
                Play.stopMidiCycle();
                Play.stopMidi();
            }
            else Play.midiCycle(sample);
        });
        tempoChoice.valueProperty().addListener((e, oldV, newV) -> {
            sample.setTempo(newV.doubleValue());
            Play.updateScore(sample);
        });



        Button confirm = new Button("Confirm");
        confirm.setOnMouseClicked(e -> {
            phase.setChoices(tonicChoice.getValue(), modeChoice.getValue(), tempoChoice.getValue());
            Play.stopMidiCycle();
            Play.stopMidi();
        });


        screen = new VBox(
                description,
                new HBox(tonic, tonicChoice),
                new HBox(scale, modeChoice),
                new HBox(tempo, tempoChoice, tempoDisplay, tempoSample),
                confirm
        );
    }

    public Node getScreen(){
        return screen;
    }
}
