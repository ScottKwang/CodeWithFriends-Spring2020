import javafx.stage.Stage;
import jm.music.data.*;
import jm.util.*;

import static jm.constants.Durations.MINIM;
import static jm.constants.Pitches.C4;

import javafx.application.Application;

public class MusioticLauncher extends Application{
    public static void main(String[] args){
        Score s = new Score(new Part(new Phrase(new Note(C4, MINIM))));
        Play.midi(s);
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("meow");
    }
}