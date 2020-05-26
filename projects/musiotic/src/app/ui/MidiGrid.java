package ui;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import song.Phase;
import util.IntegerArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MidiGrid {
    private final Phase phase;
    private final HBox actionButtons;
    private ScrollPane scrollPane;
    private GridPane gridPane;

    // Setting to each pane which {col, row} it's in.
    private HashMap<IntegerArray, MidiPane> cells;

    //TODO: CSS them borders.
    //TODO: Set up Add measures for left and right side (add buttons as well)

    // Variables for horizontal scrolling
    int scrollPos = 0;
    final int scrollMinPos = 0;
    final int scrollMaxPos = 20;
    String mode = "ADD";

    // 1 is sixteenth, 2 is eighth, 4 is quarter etc:...
    int noteLength = 4;

    public MidiGrid(Phase phase) {
        this.phase = phase;
        gridPane = new GridPane();
        scrollPane = new ScrollPane();
        scrollPane.setPannable(false); // So click + drag doesn't move scroll inadvertently
        scrollPane.setContent(gridPane);

        // https://stackoverflow.com/questions/32544574/javafx-scrollpane-horizontal-panning-with-scroll-wheel
        scrollPane.setOnScroll(event -> {
            if (event.getDeltaY() > 0)
                scrollPane.setHvalue(scrollPos == scrollMinPos ? scrollMinPos : scrollPos--);
            else
                scrollPane.setHvalue(scrollPos == scrollMaxPos ? scrollMaxPos : scrollPos++);
        });
        scrollPane.setHmin(scrollMinPos);
        scrollPane.setHmax(scrollMaxPos);

        cells = new HashMap<IntegerArray, MidiPane>();

//        initializeCells(gridPane);

        HBox modeButtons = initializeModeButtons();
        HBox lengthButtons = initializeNoteLengthButtons();

        actionButtons = new HBox(modeButtons, lengthButtons);

        String styleSheet = getClass().getResource("/css/midi_grid.css").toExternalForm();
        gridPane.getStylesheets().add(styleSheet);
    }

    private HBox initializeModeButtons() {
        Button add = new Button("ADD NOTE");
        Button edit = new Button("EDIT NOTE");
        Button delete = new Button("DELETE NOTE");
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

        return new HBox(add, edit, delete);
    }

    private HBox initializeNoteLengthButtons() {
        Button sixteenthNote = new Button("¼");
        Button eighthNote = new Button("½");
        Button quarterNote = new Button("1");
        Button halfNote = new Button("2");
        Button wholeNote = new Button("4");
        Button doubleWholeNote = new Button("8");

        sixteenthNote.setOnMouseClicked(e -> {
            noteLength = 1;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        });
        eighthNote.setOnMouseClicked(e -> {
            noteLength = 2;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        });
        quarterNote.setOnMouseClicked(e -> {
            noteLength = 4;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        });
        halfNote.setOnMouseClicked(e -> {
            noteLength = 8;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        });
        wholeNote.setOnMouseClicked(e -> {
            noteLength = 16;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        });
        doubleWholeNote.setOnMouseClicked(e -> {
            noteLength = 32;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        });
        return new HBox(sixteenthNote, eighthNote, quarterNote, halfNote, wholeNote, doubleWholeNote);
    }

    public void initializeCells() {
        // Top Vertical Bar
        for(int i = 0; i < phase.manager.numMeasures; i++) {
            Label measureNumber = new Label(" Measure " + Integer.toString(i + 1));
            gridPane.add(measureNumber, i*16 + 1, 0, 4, 1);
        }

        if (phase.getType() == Phase.Type.Drums) {
            //TODO: Differentiate for Drums
            // Kick, Snare, Hihat, open hihat, anything else?
            // this should be differentiated by constructor parameter perhaps :)
            // or better yet, if the functionality is significantly different then abstract out the commonalities
        } else {
            // Left Side Horizontal Bar
            List<StringProperty> scale = phase.manager.getScale();
            for(int j = 0; j < phase.manager.numNotes; j++) {
                Label noteLabel = new Label();
                noteLabel.textProperty().bind(scale.get(j));
                gridPane.add(noteLabel, 0, j+1, 1, 1);
            }
            for(int i = 1; i < phase.manager.numMeasures*16 + 1; i++) {
                for(int j = 1; j < phase.manager.numNotes + 1; j++) {
                    Integer[] indexes = {i, j};
                    IntegerArray arr = new IntegerArray(indexes);

                    cells.put(arr, createCell(Color.WHITESMOKE, gridPane, i, j));
                    System.out.println("CELLS.PUT: row: " + j + " col: " + i);

                }
            }
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
        }

        //FOR DEBUGGING
        //gridPane.setGridLinesVisible(true);
    }

    private MidiPane createCell(Color c, GridPane gridPane, int col, int row) {
        MidiPane pane = new MidiPane(row, col);
        pane.getStyleClass().add("grid-cell-off");
        Rectangle rectangle = new Rectangle(40, 40, c);
        rectangle.setY(1);
        rectangle.setX(1);
//        rectangle.setFill(c);
        pane.getChildren().add(rectangle);
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
                CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));

//        pane.setBorder(new Border(new BorderStroke(Color.BLACK,
//                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT, Insets.EMPTY)));
        setPane(pane);
        gridPane.add(pane, col, row);
        return pane;
    }

    private void arrangeBorders(int numNotes, int numMeasures) {
        for (int row = 0; row < numNotes; row++) {
            for(int col = 0; col < numMeasures*16; col++) {
                //System.out.println("arrangeBorders(): row: " + row + " col: " + col);
                Integer[] indexes = {col + 1, row + 1};
                IntegerArray arr = new IntegerArray(indexes);

                MidiPane midiPane = cells.get(arr);

                if (midiPane == null) {
                    System.out.println("NULL!!");
                }
                arrangeBorder(numNotes, numMeasures, col, midiPane, noteLength);
            }
        }
    }

    private void arrangeBorder(int numNotes, int numMeasures, int col, MidiPane midiPane, int noteLength) {
        if (noteLength > 7) {
            arrangeBorderLess(numNotes, numMeasures, col, midiPane);
        } else if (noteLength > 1) {
            arrangeBorderSome(numNotes, numMeasures, col, midiPane);
        } else {
            arrangeBorderMost(numNotes, numMeasures, col, midiPane);
        }
//        if (noteLength == 1) {
//            // Start and End of border
//            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
//                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
//                    CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));
//        } else if (col % noteLength == 0) {
//            // Start of border
//            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
//                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
//                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
//        } else if ((col % noteLength) == (noteLength-1)) {
//            // End of border
//            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
//                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
//                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
//        } else {
//            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
//                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
//                    CornerRadii.EMPTY, new BorderWidths(1,0,1,0), Insets.EMPTY)));
//            // Middle of border
//        }
    }

    private void arrangeBorderMost(int numNotes, int numMeasures, int col, MidiPane midiPane) {
        // noteLength is 1

        System.out.println("arrangeBorderMost(): col: " + col);
        if (col % 16 == 0) {
            //Start Measure, Left Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 16 == 15) {
            //End Measure, Right Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
        } else if (col % 4 == 0) {
            //Left side Dashed
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 4 == 3) {
            //Right side Dashed with left side single dotted
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED, BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));
        } else if (col % 2 == 0) {
            //Left side Dotted
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 2 == 1) {
            //Right side Dotted with left side single dotted
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED, BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));
        } else {
            // No Border! (Also should never be here!)
            System.out.println("Wrong place! arrangeBorderMost()");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,0), Insets.EMPTY)));
        }
    }

    private void arrangeBorderSome(int numNotes, int numMeasures, int col, MidiPane midiPane) {
        // noteLength is 4 or 2

        System.out.println("arrangeBorderSome(): col: " + col);
        if (col % 16 == 0) {
            //Start Measure, Left Solid
            System.out.println("Left Solid");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 16 == 15) {
            //End Measure, Right Solid
            System.out.println("Right Solid");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
        } else if (col % 4 == 0) {
//            Left side Dashed
            System.out.println("Left Dashed");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 4 == 3) {
            //Right side Dashed
            System.out.println("Right Dashed");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
//        } else if (col % 2 == 0) {
//            //Left side Dotted (COMMENTED OUT FOR MORE SINGLE DOTTED ON ONLY RIGHT SIDE)
//            System.out.println("Left Dotted");
//            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
//                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED,
//                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 2 == 1) {
            //Right side Dotted
            System.out.println("Right Dotted");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
        } else {
            // No Border!
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,0), Insets.EMPTY)));
        }
    }

    private void arrangeBorderLess(int numNotes, int numMeasures, int col, MidiPane midiPane) {
        // noteLength is 32, 16, or 8.

        System.out.println("arrangeBorderLess(): col: " + col);
//        System.out.println("from pane: col: " + midiPane.getCol());
        if (col % 16 == 0) {
            //Start Measure, Left Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 16 == 15) {
            //End Measure, Right Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
        } else if (col % 4 == 0) {
            //Left side Dashed
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 4 == 3) {
            //Right side Dashed
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
        } else {
            // No Border!
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,0), Insets.EMPTY)));
        }
    }

    // Example:
    // https://docs.oracle.com/javafx/2/drag_drop/HelloDragAndDrop.java.html
    private void setPane(MidiPane pane) {
        pane.setOnMouseClicked(e -> {
            //TODO: Fix duplicate notes and different length notes
            switch(mode) {
                case "ADD":
                    System.out.println("ADD");
                    addNote(pane);
                    break;
                case "EDIT":
                    System.out.println("EDIT");
                    break;
                case "DELETE":
                    System.out.println("DELETE");
                    deleteNote(pane);
                    break;
                default:
                    System.out.println("Don't be here.");
            }
        });

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

        pane.setOnDragOver(event -> {
            /* accept it only if it is  not dragged from the same node */
            if (event.getGestureSource() != pane) {
                /* allow for both copying and moving, whatever user chooses */
                System.out.println("onDragOver");
                event.acceptTransferModes(TransferMode.MOVE);
            } else {
                System.out.println("onDragOver SAME OBJECT");
            }

            event.consume();
        });

        pane.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
            System.out.println("onDragEntered");
            /* show to the user that it is an actual gesture target */
            if (event.getGestureSource() != pane) {
                Bloom bloom = new Bloom();
                pane.setEffect(bloom);
            }

            event.consume();
        });

        pane.setOnDragExited(event -> {
            /* mouse moved away, remove the graphical cues */
            pane.setEffect(null);

            event.consume();
        });

        pane.setOnDragDropped(event -> {
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
        });

        pane.setOnDragDone(event -> {
            /* the drag-and-drop gesture ended */
            System.out.println("onDragDone");
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
                r.setFill(Color.WHITESMOKE);
            }

            event.consume();
        });


    }

    private void addNote(MidiPane pane) {
        int col = pane.getCol();
        int row = pane.getRow();
        System.out.println("col: " + col + ". row: " + row);
        Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
        r.setFill(Color.RED);
        pane.getStyleClass().remove("grid-cell-off");
        pane.getStyleClass().add("grid-cell-on");
        String note = getNote(row);
        System.out.println(note + " Added!");

        phase.addNote(note, noteLength, col-1);
    }

    private void deleteNote(MidiPane pane) {
        int col = pane.getCol();
        int row = pane.getRow();
        System.out.println("col: " + col + ". row: " + row);
        Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
        r.setFill(Color.WHITESMOKE);
        pane.getStyleClass().add("grid-cell-off");
        String note = getNote(row);
        System.out.println(note + " Deleted!");

        phase.deleteNote(note, noteLength, col-1);
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
