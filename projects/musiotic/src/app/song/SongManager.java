package song;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Play;
import jm.util.Write;
import ui.SongEditorScreen;
import util.MappedLinkedList;
import util.Scale;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class SongManager {
    public final StylePhase stylePhase;
    private SongEditorScreen screen;
    public MappedLinkedList<Phase.Type, Phase> phaseMap;
    private final Property<Phase> currentPhase;
    private final BooleanProperty nextAvailable;
    private final BooleanProperty prevAvailable;
    private final Score score;
    private int tonic;
    private int[] mode;
    private boolean playing;
    private Timer playingTimer;

    // Number of notes a user can input midi for. 8 is one octave.
    public int numNotes;
    // How many measures the MIDI Sequence will have. Default: 4 measures (4 bar loops).
    public int numMeasures;

    public SongManager(){
        stylePhase = new StylePhase(this);
        currentPhase = new SimpleObjectProperty<>();
        currentPhase.setValue(stylePhase);
        nextAvailable = new SimpleBooleanProperty();
        nextAvailable.bind(stylePhase.completed);
        prevAvailable = new SimpleBooleanProperty();
        prevAvailable.bind(Bindings.createBooleanBinding(
                () -> !currentPhase.getValue().equals(stylePhase),
                currentPhase
        ));
        numNotes = 8;
        numMeasures = 4;
        score = new Score();
        tonic = JMC.C4;
        mode = JMC.MAJOR_SCALE;
        playing = false;
        playingTimer = new Timer();
    }

    public void setScreen(SongEditorScreen screen){
        this.screen = screen;
    }

    public void populateStyle(){
        if(screen == null) throw new IllegalStateException("SongEditorScreen not set.");
//        System.out.println("SongManager: populateStyle()");
        var phases = stylePhase.getPhases();
        phaseMap = new MappedLinkedList<>(phases);
        phases.values().forEach(Phase::initialize);
        screen.populate(phases.values());
    }

    public void goToPhase(Phase phase){
        if(screen == null) throw new IllegalStateException("SongEditorScreen not set.");
        screen.updatePhase(updatePhase(phase));
    }

    public Phase updatePhase(Phase phase){
        var next = phaseMap.getNext(phase.getType());
        nextAvailable.bind(next == null ? new SimpleBooleanProperty(false) : next.disabled.not());
        currentPhase.setValue(phase);
        return phase;
    }

    public void changeTonic(int tonic){
        Mod.transpose(score, tonic - this.tonic);
        this.tonic = tonic;
    }

    public void changeMode(int[] mode) {
        if (mode.length != this.mode.length) {
            throw new IllegalArgumentException("New mode must be the same size as previous mode.");
        }
        int[] diff = new int[mode.length];
        for (int i = 0; i < mode.length; i++) diff[i] = mode[i] - this.mode[i];

        for (Part part : score.getPartArray())
            for (Phrase phrase : part.getPhraseArray())
                for (Note note : phrase.getNoteArray()) {
                    if (note.getPitchType() == Note.MIDI_PITCH && note.getPitch() != JMC.REST) {
                        var pitch = note.getPitch();
                        var inScale = (pitch - tonic) % 12;
                        var ordInScale = Arrays.binarySearch(this.mode, inScale);
                        if (ordInScale >= 0)
                            Mod.transpose(note, diff[ordInScale]);
                    }
                }

        this.mode = mode;
    }

    public BooleanProperty nextAvailable() {
        return nextAvailable;
    }

    public BooleanProperty prevAvailable() {
        return prevAvailable;
    }

    public void addMeasures() {
        this.numMeasures += 4;
//        System.out.println("The number of Measures this MIDI has is: " + numMeasures);
        forInstrumentalPhase(phase -> {
            phase.addMeasure();
            phase.addMeasure();
            phase.addMeasure();
            phase.addMeasure();
        });
    }

    public Scale getScale() {
        var keyPhase = (KeyPhase) phaseMap.get(Phase.Type.Key);
        if(keyPhase == null) return null;
        return keyPhase.getScale();
    }

    public void addPart(Part part) {
        score.add(part);
    }

    public void play(int measureNum) {
        if (playing) {
            System.out.println("play but cancelling in song manager");
            playingTimer.cancel();
            playingTimer = new Timer();
            stop();
            forInstrumentalPhase(phase -> {
                phase.setPlayButton(false);
            });
        } else {
            System.out.println("play in song manager");

            List<Part> oldParts = new ArrayList<>();
            forInstrumentalPhase(phase -> {
                Part part = phase.part;
                phase.backupPart();
                var currPhrases = part.getPhraseArray();
                for(var phrase : currPhrases){
                    if(phrase.getStartTime() < measureNum * 4){
                        part.removePhrase(phrase);
                    }
                }
                var oldPart = phase.consolidatePart();// Prepare part for playing
                oldParts.add(oldPart);
            });
            for(var part : score.getPartArray()){
                for(var phrase : part.getPhraseArray()){
                    phrase.setStartTime(phrase.getStartTime() + 0.25);
                }
            }
            Play.midi(score);
            score.removeAllParts();
            for(Part part : oldParts) score.addPart(part);

            //change buttons on instrumental phrases.
            forInstrumentalPhase(phase -> {
                phase.setPlayButton(true);
            });

            double time = (double)(numMeasures - measureNum) * 4 * 60 * 1000 / this.getTempo();
            time += 200;
            System.out.println("Playing for ms: " + time);
            playingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        forInstrumentalPhase(phase -> {
                            play(measureNum);
                        });
                    });
                }
            }, (int) time);

            playing = true;

        }
    }

    public void stop() {
        playing = false;
        try {
            Play.stopMidi();
        } catch (StackOverflowError e) {
            System.out.println(e.toString());
        }
    }

    protected void forInstrumentalPhase(Consumer<InstrumentalPhase> consumer){
        Phase curr = phaseMap.getNext(Phase.Type.Style);
        while(curr != null){
            if(curr instanceof InstrumentalPhase){
                consumer.accept((InstrumentalPhase) curr);
            }
            curr = phaseMap.getNext(curr.getType());
        }
    }

    public void setTempo(double tempo) {
        score.setTempo(tempo);
    }

    public double getTempo() {
        return score.getTempo();
    }

    public void writeScore(String filename) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Music File");
        fileChooser.setInitialFileName(filename + ".midi");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI file", "midi"));
        File file = fileChooser.showSaveDialog(screen.getScene().getWindow());
        if (file != null) {
            List<Part> oldParts = new ArrayList<>();
            forInstrumentalPhase(phase -> {
                phase.backupPart();
                var oldPart = phase.consolidatePart();// Prepare part for playing
                oldParts.add(oldPart);
            });

            Write.midi(score, file.getAbsolutePath());
            new File(System.getProperty("user.home") + "\\Musiotic").mkdirs();
            Write.midi(score, System.getProperty("user.home") + "\\Musiotic\\" + filename + ".midi");
            score.removeAllParts();
            for(Part part : oldParts) score.addPart(part);
        }
    }
}
