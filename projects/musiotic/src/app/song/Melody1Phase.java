package song;

import javafx.scene.Node;
import music.MIDISequence;
import music.Melody1Sequence;
import ui.Melody1Screen;

public class Melody1Phase extends Phase {
    private final Melody1Screen screen;
    public final Melody1Sequence sequence;

    public Melody1Phase(SongManager manager) {
        super(manager);
        sequence = new Melody1Sequence(this);
        screen = new Melody1Screen(this);
    }

    @Override
    public Type getType() {
        return Type.Melody1;
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }

    public MIDISequence getMidiSequence() {
        return sequence;
    }
}
