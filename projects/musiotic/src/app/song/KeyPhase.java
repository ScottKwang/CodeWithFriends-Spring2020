package song;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import jm.JMC;
import ui.KeyScreen;
import util.Scale;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyPhase extends Phase {
    private final KeyScreen screen;
    private Scale scale;

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
            "A", JMC.A2,
            "B", JMC.B2,
            "C", JMC.C3,
            "D", JMC.D3,
            "E", JMC.E3,
            "F", JMC.F3,
            "G", JMC.G3
    );

    public static final Map<String, int[]> modes = Map.of(
            "Major", JMC.MAJOR_SCALE,
            "Minor", JMC.MINOR_SCALE,
            "Harmonic Minor", JMC.HARMONIC_MINOR_SCALE,
            "Lydian", JMC.LYDIAN_SCALE,
            "Mixolydian", JMC.MIXOLYDIAN_SCALE
    );
    public void setChoices(String tonic, String mode, double tempo) {
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
        if(!completed.getValue()){
            manager.forInstrumentalPhase(InstrumentalPhase::initialize);
            completed.setValue(true);
        }
        manager.setTempo(tempo);
        manager.goToPhase(manager.phaseMap.getNext(getType()));
    }

    private void setScale(char newTonic, int[] mode) {
        var noteList = new ArrayList<String>();
        var noteValues = new ArrayList<Integer>();

        var startVal = (char) (((newTonic - 'A') % 7) + 'A');
        var startJmVal = roots.get(String.valueOf(startVal));
        var count = 0;

        for(char current = newTonic; current < newTonic + manager.numNotes*2-1; current++){
            var noteVal = (char) (((current - 'A') % 7) + 'A');
            if(noteVal == startVal) count++;
            var noteName = new StringBuilder(String.valueOf(noteVal));
            var jmVal = roots.get(String.valueOf(noteVal));
            if(jmVal < this.tonic) jmVal += 12;
            if(jmVal > startJmVal+12) jmVal += 12;
            if(count == 2) jmVal += 12;
            var realVal = this.tonic + mode[(current - newTonic) % 7];
            if(current > newTonic && realVal == this.tonic) realVal += 12;
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
            noteValues.add(realVal);
            System.out.println(noteName.toString() + " with value: " + realVal);
        }

        if(scale == null) {
            scale = new Scale(noteList.stream().map(SimpleStringProperty::new).collect(Collectors.toList())
                    , noteValues.stream().map(SimpleIntegerProperty::new).collect(Collectors.toList()));

        } else{
            for(int i = 0; i < noteList.size(); i++){
                scale.getValues().get(i).setValue(noteValues.get(i));
                scale.getNames().get(i).setValue(noteList.get(i));
            }
        }
    }

    protected Scale getScale() {
        return scale;
    }
}
