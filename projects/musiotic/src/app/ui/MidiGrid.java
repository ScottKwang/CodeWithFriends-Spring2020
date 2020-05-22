package ui;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Bloom;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import song.Phase;

import java.util.ArrayList;
import java.util.HashMap;

public class MidiGrid {
    private final Phase phase;
    private final HBox actionButtons;
    private ScrollPane scrollPane;
    private GridPane gridPane;

    // Setting to each pane which {col, row} it's in.
    private HashMap<Pane, int[]> cells;

    //TODO: CSS them borders.
    //TODO: Set up Add measures for left and right side (add buttons as well)

    // Variables for horizontal scrolling
    int scrollPos = 0;
    final int scrollMinPos = 0;
    final int scrollMaxPos = 20;
    String mode = "ADD";

    public MidiGrid(Phase phase) {
        this.phase = phase;
        gridPane = new GridPane();
        scrollPane = new ScrollPane();
        scrollPane.setPannable(false); // So click + drag doesn't move scroll inadvertently
        scrollPane.setContent(gridPane);

        // https://stackoverflow.com/questions/32544574/javafx-scrollpane-horizontal-panning-with-scroll-wheel
        scrollPane.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                if (event.getDeltaY() > 0)
                    scrollPane.setHvalue(scrollPos == scrollMinPos ? scrollMinPos : scrollPos--);
                else
                    scrollPane.setHvalue(scrollPos == scrollMaxPos ? scrollMaxPos : scrollPos++);

            }
        });
        scrollPane.setHmin(scrollMinPos);
        scrollPane.setHmax(scrollMaxPos);

        cells = new HashMap<>();
//        initializeCells(gridPane);


        Button add = new Button("ADD NOTE");
        Button edit = new Button("EDIT NOTE");
        Button delete = new Button("DELETE NOTE");
        actionButtons = new HBox(add, edit, delete);
        add.setOnMouseClicked(e -> {
            System.out.println("MODE: ADD");
            mode = "ADD";
        });
        edit.setOnMouseClicked(e -> {
            System.out.println("MODE: EDIT");
            mode = "EDIT";
        });
        delete.setOnMouseClicked(e -> {
            System.out.println("MODE: DELETE");
            mode = "DELETE";
        });
    }

    public void initializeCells() {
        // Top Vertical Bar
        for(int i = 0; i < phase.manager.numMeasures; i++) {
            Label measureNumber = new Label(Integer.toString(i + 1));
            gridPane.add(measureNumber, i*4 + 1, 0, 4, 1);
        }


        if (phase.getType() == Phase.Type.Drums) {
            //TODO: Differentiate for Drums
            // Kick, Snare, Hihat, open hihat, anything else?
            // this should be differentiated by constructor parameter perhaps :)
            // or better yet, if the functionality is significantly different then abstract out the commonalities
        } else {
            // Left Side Horizontal Bar
            ArrayList<String> scale = phase.manager.getScale();
            for(int j = 0; j < phase.manager.numNotes; j++) {
                Label noteLabel = new Label(scale.get(j));
                gridPane.add(noteLabel, 0, j+1, 1, 1);
            }
            for(int i = 1; i < phase.manager.numMeasures*4 + 1; i++) {
                for(int j = 1; j < phase.manager.numNotes + 1; j++) {
                    cells.put(createCell(Color.WHITESMOKE, gridPane, i, j), new int[] {i, j});
                }
            }
        }

        //FOR DEBUGGING
        gridPane.setGridLinesVisible(true);
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
    private void setPane(Pane pane) {
        pane.setOnMouseClicked(e -> {
            //TODO: Fix duplicate notes and different length notes
            // tip: recommend switch statement for 3+ branches (especially if there is a possibility of adding more!)
            if (mode.equals("ADD")) {
                System.out.println("ADD");
                addNote(pane);
            } else if (mode.equals("EDIT")) {
                System.out.println("EDIT");
            } else if (mode.equals("DELETE")) {
                System.out.println("DELETE");
                deleteNote(pane);
            } else {
                System.out.println("Don't be here.");
            }
        });

        // todo tip: use lambdas
        pane.setOnDragDetected(event -> {
            /* drag was detected, start drag-and-drop gesture*/
            System.out.println("onDragDetected");

            /* allow any transfer mode */
            Dragboard db = pane.startDragAndDrop(TransferMode.ANY);
            ClipboardContent cc = new ClipboardContent();
            cc.putString("Needs this to work"); // Lol API
            db.setContent(cc);

            event.consume();
        });

        //todo i'll let you do this one
        pane.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* accept it only if it is  not dragged from the same node */
                if (event.getGestureSource() != pane) {
                    /* allow for both copying and moving, whatever user chooses */
                    System.out.println("onDragOver");
                    event.acceptTransferModes(TransferMode.MOVE);
                } else {
                    System.out.println("onDragOver SAME OBJECT");
                }

                event.consume();
            }
        });

        //todo and this one (and the rest)
        pane.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != pane) {
                    Bloom bloom = new Bloom();
                    pane.setEffect(bloom);
                }

                event.consume();
            }
        });

        pane.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                pane.setEffect(null);

                event.consume();
            }
        });

        pane.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Pane oldPane = (Pane) event.getGestureSource();
                Rectangle oldR = (Rectangle) oldPane.getChildren().toArray()[0];
                Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
                r.setFill(oldR.getFill());
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(true);

                event.consume();
            }
        });

        pane.setOnDragDone(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                    Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
                    r.setFill(Color.WHITESMOKE);
                }

                event.consume();
            }
        });


    }

    private void addNote(Pane pane) {
        int[] location = cells.get(pane);
        int col = location[0];
        int row = location[1];
        System.out.println("col: " + col + ". row: " + row);
        Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
        r.setFill(Color.RED);
        String note = getNote(row);
        System.out.println(note + " Added!");
        //TODO: ADD MIDI HERE FROM JMUSIC
    }

    private void deleteNote(Pane pane) {
        int[] location = cells.get(pane);
        int col = location[0];
        int row = location[1];
        System.out.println("col: " + col + ". row: " + row);
        Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
        r.setFill(Color.WHITESMOKE);
        String note = getNote(row);
        System.out.println(note + " Deleted!");
        //TODO: DELETE MIDI HERE FROM JMUSIC
    }

    private String getNote(int row) {
        String note = "";
        // i is 1 here so we don't check the cell at 0,0.
        for (int i = 1; i < gridPane.getChildren().size(); i++) {
            Node node = gridPane.getChildren().get(i);
            if (node != null) {
                try {
                    if (gridPane.getColumnIndex(node) == 0 && gridPane.getRowIndex(node) == row) {
                        note = ((Label) node).getText();
                    }
                } catch(NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return note;
    }


    public Node getActionButtons() {
        return actionButtons;
    }

    public Node getGrid() {
        return scrollPane;
    }
}
