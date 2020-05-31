package song;

import javafx.scene.Node;
import ui.Melody1Screen;

public class Melody1Phase extends InstrumentalPhase {
    private final Melody1Screen screen;

    public Melody1Phase(SongManager manager) {
        super(manager);
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

    @Override
    protected void setPlayButton(boolean playing) {
        screen.setPlayButton(playing);
    }
}
