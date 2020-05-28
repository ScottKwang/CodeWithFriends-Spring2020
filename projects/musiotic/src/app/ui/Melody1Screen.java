package ui;

import javafx.scene.control.Label;
import song.Melody1Phase;

public class Melody1Screen extends MidiScreen {
    private final Melody1Phase phase;

    public Melody1Screen(Melody1Phase phase) {
        super(phase);
        this.phase = phase;
        //setBottom(new Label("Melody Screen")); // For example

        // midiGrid is null here but set once the style is confirmed it will be set.
//        System.out.println(midiGrid);
    }
}
