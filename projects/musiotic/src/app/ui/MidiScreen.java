package ui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import music.MIDISequence;
import song.Phase;

import java.util.ArrayList;
import java.util.HashMap;

public class MidiScreen {
    private final Phase phase;
    private final Node screen;
    private final MIDISequence midiSequence;

    // Setting to each pane which {col, row} it's in.
    private HashMap<Pane, int[]> cells;

    //TODO: Make first column Key column.
    //TODO: CSS them borders.
    //TODO: Buttons for ADD, EDIT, and DELETE.
    //TODO: Place to let Brian connect JMusic.
    //TODO: Set up Add measures for left and right side (add buttons as well)
    //TODO: SCROLL this PANE!


    public MidiScreen(MIDISequence midiSequence) {
        this.phase = midiSequence.getPhase();
        this.midiSequence = midiSequence;
        GridPane gridPane = new GridPane();
        cells = new HashMap<>();
        initializeCells(gridPane);


        //FOR DEBUGGING
        gridPane.setGridLinesVisible(true);

        screen = gridPane;
    }

    public Node getScreen(){
        return screen;
    }

    private void initializeCells(GridPane gridPane) {
        for(int i = 0; i < midiSequence.numMeasures*4; i++) {
            for(int j = 0; j < midiSequence.numNotes; j++) {
                cells.put(createCell(Color.WHITESMOKE, gridPane, i, j), new int[] {i, j});
            }
        }
    }

    private Pane createCell(Color c, GridPane gridPane, int col, int row) {
        Pane pane = new Pane();
        Rectangle rectangle = new Rectangle(100, 50);
        rectangle.setFill(c);
        pane.getChildren().add(rectangle);
//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setPane(pane);
        gridPane.add(pane, col, row);
        return pane;
    }

    // Example:
    // https://docs.oracle.com/javafx/2/drag_drop/HelloDragAndDrop.java.html
    private void setPane(Pane c) {
        c.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");

                /* allow any transfer mode */
                Dragboard db = c.startDragAndDrop(TransferMode.ANY);
                ClipboardContent cc = new ClipboardContent();
                cc.putString("Needs this to work"); // Lol API
                db.setContent(cc);

                event.consume();
            }
        });

        c.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* accept it only if it is  not dragged from the same node */
                if (event.getGestureSource() != c) {
                    /* allow for both copying and moving, whatever user chooses */
                    System.out.println("onDragOver");
                    event.acceptTransferModes(TransferMode.MOVE);
                } else {
                    System.out.println("onDragOver SAME OBJECT");
                }

                event.consume();
            }
        });

        c.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != c) {
                    Bloom bloom = new Bloom();
                    c.setEffect(bloom);
                }

                event.consume();
            }
        });

        c.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                c.setEffect(null);

                event.consume();
            }
        });

        c.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Pane oldC = (Pane) event.getGestureSource();
                Rectangle oldR = (Rectangle) oldC.getChildren().toArray()[0];
                Rectangle r = (Rectangle) c.getChildren().toArray()[0];
                r.setFill(oldR.getFill());
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(true);

                event.consume();
            }
        });

        c.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    Rectangle r = (Rectangle) c.getChildren().toArray()[0];
                    r.setFill(Color.WHITESMOKE);
                }

                event.consume();
            }
        });


    }

}
