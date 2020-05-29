package launcher;

import ui.StartScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Durations.SIXTEENTH_NOTE;
import static jm.constants.Pitches.C4;
import static jm.constants.Pitches.C5;
import static jm.constants.Pitches.E4;
import static jm.constants.Pitches.G4;

public class MusioticLauncher extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene startScreen = new Scene(new StartScreen(), 1280, 720);
        stage.setScene(startScreen);
        //stage.initStyle(StageStyle.DECORATED);
        //stage.setFullScreen(true);

        stage.setTitle("Musiotic");
        stage.show();
        stage.setMaximized(true);
        Phrase intro = new Phrase();
        intro.add(new Note(C4, SIXTEENTH_NOTE));
        intro.add(new Note(E4, SIXTEENTH_NOTE));
        intro.add(new Note(G4, SIXTEENTH_NOTE));
        intro.add(new Note(C5, EIGHTH_NOTE));
        Score s = new Score(new Part(intro));
        Play.midi(s);
    }

    @Override
    public void stop() throws Exception {
        Play.stopMidi();
        super.stop();
    }
}