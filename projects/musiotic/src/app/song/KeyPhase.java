package song;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import jm.JMC;
import ui.KeyScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyPhase extends Phase {
    private final KeyScreen screen;
    private List<StringProperty> scale;

    public KeyPhase(SongManager manager) {
        super(manager);
        screen = new KeyScreen(this);
    }

    private int tonic;
    private int[] mode;

    @Override
    public Type getType() {
        return Type.Key;
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }

    public static final Map<String, Integer> roots = Map.of(
            "A", JMC.A3,
            "B", JMC.B3,
            "C", JMC.C4,
            "D", JMC.D4,
            "E", JMC.E4,
            "F", JMC.F4,
            "G", JMC.G4
    );

    public static final Map<String, int[]> modes = Map.of(
            "Major", JMC.MAJOR_SCALE,
            "Minor", JMC.MINOR_SCALE,
            "Harmonic Minor", JMC.HARMONIC_MINOR_SCALE,
            "Lydian", JMC.LYDIAN_SCALE,
            "Mixolydian", JMC.MIXOLYDIAN_SCALE
    );
    public void setChoices(String tonic, String mode) {
        var newTonic = roots.get(tonic);
        if(newTonic != this.tonic){
            this.tonic = newTonic;
            manager.changeTonic(newTonic);
        }
        var newMode = modes.get(mode);
        if(newMode != this.mode){
            this.mode = newMode;
            manager.changeMode(newMode);
        }
        setScale(tonic.toCharArray()[0], this.mode);
        completed.setValue(true);
        manager.goToPhase(manager.phaseMap.getNext(getType()));
    }

    private void setScale(char newTonic, int[] mode) {
        var noteList = new ArrayList<String>();

        for(char current = newTonic; current < newTonic + manager.numNotes; current++){
            var noteVal = (char) (((current - 'A') % 7) + 'A');
            var noteName = new StringBuilder(String.valueOf(noteVal));
            var jmVal = roots.get(String.valueOf(noteVal)) % 12 + this.tonic;
            var realVal = (this.tonic + mode[(current - newTonic) % 7]) % 12 + this.tonic;
            switch (realVal - jmVal){
                case 2:
                    noteName.append(Character.toChars(0x1D12A));
                    break;
                case 1:
                    noteName.append("♯");
                    break;
                case 0:
                    break;
                case -1:
                    noteName.append("♭");
                    break;
                case -2:
                    noteName.append(Character.toChars(0x1D12B));
                    break;
            }
            noteList.add(noteName.toString());
        }

        if(scale == null){
            scale = noteList.stream().map(SimpleStringProperty::new).collect(Collectors.toList());
        } else{
            for(int i = 0; i < noteList.size(); i++){
                scale.get(i).setValue(noteList.get(i));
            }
        }
    }

    protected List<StringProperty> getScale() {
        return scale;
    }
}
