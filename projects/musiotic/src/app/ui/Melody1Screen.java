package ui;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import song.Melody1Phase;

import java.util.HashMap;

public class Melody1Screen {
    private final Melody1Phase phase;
    private final Node screen;

    //scroll bar, left right

    public Melody1Screen(Melody1Phase phase) {
        this.phase = phase;

        BorderPane pane = new BorderPane();
        //Scene scene = new Scene(pane, 600, 600);
        //pane.setBorder(new Border(new BorderStroke(Color.BLACK,
        //        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        //scene.setFill(Color.LIGHTGREEN);

        //Pane circles = new Pane(circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9);
//        circles.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
//        circles.setPrefSize(600, 600);
//        circles.setMaxSize(600, 600);
//        circles.setMinSize(600, 600);
        //root.getChildren().add(pane);
//        pane.getChildren().addAll(circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9);
        MidiScreen midiScreen = new MidiScreen(phase.getMidiSequence());
        pane.setCenter(midiScreen.getScreen());

        screen = pane;

    }

    public Node getScreen(){
        return screen;
    }

}
