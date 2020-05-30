package ui;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import song.InstrumentalPhase;

public abstract class MidiScreen implements Screen {
    private final InstrumentalPhase phase;
    protected MidiGrid midiGrid;
    private final BorderPane screen;

    protected MidiScreen(InstrumentalPhase phase){
        this.phase = phase;
        screen = new BorderPane();
        String styleSheet = getClass().getResource("/css/midi_screen.css").toExternalForm();
        screen.getStylesheets().add(styleSheet);
    }

    protected void setBottom(Node bottom){
        screen.setBottom(bottom);
    }

    protected void setLeft(Node left){
        screen.setLeft(left);
    }

    protected void setRight(Node right){
        screen.setRight(right);
    }

    // Allow each phase to be styled independently
    protected void setStyleSheet(String cssURI){
        String styleSheet = getClass().getResource(cssURI).toExternalForm();
        screen.getStylesheets().add(styleSheet);
    }

    @Override
    public Node getScreen() {
        if (midiGrid == null) {
            midiGrid = new MidiGrid(phase);
            midiGrid.initializeCells();
            screen.setTop(midiGrid.getActionButtons());
            screen.setCenter(midiGrid.getGrid());
        }
        return screen;
    }

    public void setPlayButton(boolean playing) {
        if (midiGrid != null) {
            midiGrid.setPlayButton(playing);
        }
    }
}
