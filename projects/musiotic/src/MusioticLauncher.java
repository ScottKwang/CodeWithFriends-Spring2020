import UserInterface.MainScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import static jm.constants.Durations.EIGHTH_NOTE;
import static jm.constants.Pitches.C4;

public class MusioticLauncher extends Application{
    public static void main(String[] args){
        Score s = new Score(new Part(new Phrase(new Note(C4, EIGHTH_NOTE))));
        Play.midi(s);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene mainScene = new Scene(new MainScreen(), 1280, 720);
        stage.setScene(mainScene);
        stage.setTitle("Musiotic");
        stage.show();
    }
}