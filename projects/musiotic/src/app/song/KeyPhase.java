package song;

import javafx.scene.Node;
import jm.JMC;
import ui.KeyScreen;

import java.util.Map;

public class KeyPhase extends Phase {
    private final KeyScreen screen;

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

    private static final Map<String, Integer> roots = Map.of(
            "A", JMC.A4,
            "B", JMC.B4,
            "C", JMC.C4,
            "D", JMC.D4,
            "E", JMC.E4,
            "F", JMC.F4,
            "G", JMC.G4
    );
    private static final Map<String, int[]> modes = Map.of(
            "Major", JMC.MAJOR_SCALE,
            "Minor", JMC.MINOR_SCALE,
            "Harmonic Minor", JMC.HARMONIC_MINOR_SCALE,
            "Lydian", JMC.LYDIAN_SCALE,
            "Mixolydian", JMC.MIXOLYDIAN_SCALE
    );
    public void setChoices(String tonic, String mode) {
        this.tonic = roots.get(tonic);
        this.mode = modes.get(mode);
        completed.setValue(true);
        manager.goToPhase(manager.phaseList.getNext(getType()));
    }
}
