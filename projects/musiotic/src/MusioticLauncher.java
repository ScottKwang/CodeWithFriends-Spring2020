import UserInterface.MainScreen;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
        mainScene.setCursor(Cursor.DEFAULT);
        stage.setScene(mainScene);
        //stage.initStyle(StageStyle.DECORATED);
        //stage.setFullScreen(true);

        stage.setTitle("Musiotic");
        stage.show();
    }
}