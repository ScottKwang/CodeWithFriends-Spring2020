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
    private int scrollPos = 0;
    private final int scrollMinPos = 0;
    private final int scrollMaxPos = 20;
    String mode = "ADD";

    // For MODE "EDIT"
    private MidiPane editPane = null;

    // 1 is sixteenth, 2 is eighth, 4 is quarter etc:...
    private int noteLength = 4;

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
        Button add = new Button("Add Note");
        Button edit = new Button("Extend/Shrink Note");
        Button delete = new Button("Delete Note");
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
                    Integer[] indexes = {i-1, j-1};
                    IntegerArray arr = new IntegerArray(indexes);

                    cells.put(arr, createCell(Color.WHITESMOKE, gridPane, i-1, j-1));
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
        Rectangle rectangle = new Rectangle(20, 40, c);
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
        gridPane.add(pane, col+1, row+1);
        return pane;
    }

    private void arrangeBorders(int numNotes, int numMeasures) {
        for (int row = 0; row < numNotes; row++) {
            for(int col = 0; col < numMeasures*16; col++) {
                //System.out.println("arrangeBorders(): row: " + row + " col: " + col);
                Integer[] indexes = {col, row};
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
        if (noteLength > 3) {
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
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED,
                    CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));
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
                    addNote(pane, noteLength);
                    break;
                case "EDIT":
                    System.out.println("EDIT");
                    if (editPane == null) {
                        Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
                        if (r.getFill() == Color.WHITESMOKE) {
                            // Do nothing!
                            editPane = null;
                        } else {
                            editPane = pane;
                        }
                    } else {
                        if (editPane.getRow() == pane.getRow()) {
                            // Same row! Let's check for extend/shrink
                            ArrayList<Integer> list = findConnectedMidiPanes(editPane.getCol(), editPane.getRow());
                            MidiPane editPaneStart = cells.get(new IntegerArray(new Integer[] {list.get(0), editPane.getRow()}));
                            int editPaneStartCol = editPaneStart.getCol();
                            int editPaneEndCol = editPaneStartCol + list.get(1);
                            int newPaneCol = pane.getCol();
                            if (editPaneStartCol + list.get(1) < newPaneCol) {
                                // Extend to the right!
                                int newLength = newPaneCol - editPaneStartCol + 1;
                                deleteNote(editPaneStart, list.get(1));
                                addNote(editPaneStart, newLength);
                            } else if (newPaneCol < editPaneStartCol) {
                                // Extend to the left!
                                int newLength = editPaneEndCol-newPaneCol;
                                deleteNote(editPaneStart, list.get(1));
                                addNote(pane, newLength);
                            } else {
                                // Shrink in between!
                                if (Math.abs(editPaneEndCol - newPaneCol + 1) <= Math.abs(newPaneCol - editPaneStartCol + 1)) {
                                    // Picking which side to shrink from
                                    // From right side to middle
                                    int newLength = editPaneEndCol-newPaneCol;
                                    deleteNote(editPaneStart, list.get(1));
                                    addNote(pane, newLength);
                                } else {
                                    // From left side to middle
                                    int newLength = newPaneCol-editPaneStartCol+1;
                                    deleteNote(editPaneStart, list.get(1));
                                    addNote(editPaneStart, newLength);
                                }
                            }
                            editPane = null;
                        } else {
                            // Not the same row! Can't extend/shrink
                            // Set editPane to this new Pane.
                            editPane = pane;
                        }
                    }
                    break;
                case "DELETE":
                    System.out.println("DELETE");
                    ArrayList<Integer> list = findConnectedMidiPanes(pane.getCol(), pane.getRow());
                    if (list == null) {
                        System.out.println("Nothing to Delete!");
                    } else {
                        MidiPane deletePane = cells.get(new IntegerArray(new Integer[] {list.get(0), pane.getRow()}));
                        if (deletePane == null) {
                            System.out.println("NULL!!!!");
                        }
                        deleteNote(deletePane, list.get(1));
                    }
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
            MidiPane oldPane = (MidiPane) event.getGestureSource();

            int oldCol = oldPane.getCol();
            int oldRow = oldPane.getRow();
            ArrayList<Integer> oldStartData = findConnectedMidiPanes(oldCol, oldRow);
            if (oldStartData == null) {
                // Trying to move a rest! Don't do that!
            } else {
                int oldColStart = oldStartData.get(0);
                int oldNoteLength = oldStartData.get(1);
                MidiPane startOfOldPane = cells.get(new IntegerArray(new Integer[] {oldColStart, oldRow}));
                if (startOfOldPane == null) {
                    System.out.println("NULL!!!!");
                }
                deleteNote(startOfOldPane, oldNoteLength);
                addNote(pane, oldNoteLength);

//                Rectangle oldR = (Rectangle) oldPane.getChildren().toArray()[0];
//                Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
//
//                if (r.getFill() != Color.WHITESMOKE && oldR.getFill() == Color.WHITESMOKE) {
//                    pane.getStyleClass().remove("grid-cell-on");
//                    pane.getStyleClass().add("grid-cell-off");
//                } else if (oldR.getFill() == Color.RED) {
//                    pane.getStyleClass().remove("grid-cell-off");
//                    pane.getStyleClass().add("grid-cell-on");
//                    oldPane.getStyleClass().remove("grid-cell-on");
//                    oldPane.getStyleClass().add("grid-cell-off");
//                }
//                r.setFill(oldR.getFill());

                // TODO: Be able to move groups only


                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(true);

            }
            event.consume();
        });

        pane.setOnDragDone(event -> {
            /* the drag-and-drop gesture ended */
            System.out.println("onDragDone");
            /* if the data was successfully moved, clear it */
//            if (event.getTransferMode() == TransferMode.MOVE) {
//                Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
//                r.setFill(Color.WHITESMOKE);
//            }

            event.consume();
        });


    }

    private ArrayList<Integer> findConnectedMidiPanes(int col, int row) {
        int startCol = col;
        int endCol = col;


        MidiPane node = cells.get(new IntegerArray(new Integer[] {startCol, row}));
        Rectangle check = (Rectangle) node.getChildren().toArray()[0];
        if (check.getFill() == Color.WHITESMOKE) {
            return null;
        }

        while (node.isLeft() == true) {
            startCol--;
            node = cells.get(new IntegerArray(new Integer[] {startCol, row}));
        }
        node = cells.get(new IntegerArray(new Integer[] {endCol, row}));
        while (node.isRight() == true) {
            endCol++;
            node = cells.get(new IntegerArray(new Integer[] {endCol, row}));
        }

        ArrayList<Integer> res = new ArrayList<>();
        res.add(startCol);
        res.add(endCol-startCol+1);
        //Returns column start and it's note length.
        int tempLength = endCol-startCol+1;
        System.out.println("findConnectedMidiPanes(): temp length: " + tempLength);
        return res;

    }

    private void addNote(MidiPane pane, int noteLength) {
        int col = pane.getCol();
        int row = pane.getRow();
        System.out.println("col: " + col + ". row: " + row);
        boolean canPlace = true;
        for (int i = 0; i < noteLength; i++) {
            Integer[] indexes = {col + i, row};
            IntegerArray arr = new IntegerArray(indexes);
            MidiPane tempMidiPane = cells.get(arr);
            Rectangle tempR = (Rectangle) tempMidiPane.getChildren().toArray()[0];
            if (tempR.getFill() != Color.WHITESMOKE) {
                canPlace = false;
                break;
            }
        }
        if (!canPlace) {
            System.out.println("Can't place Note! Incorrect size for empty space.");
        } else {
            //can Place!
            for (int i = 0; i < noteLength; i++) {
                Integer[] indexes = {col + i, row};
                IntegerArray arr = new IntegerArray(indexes);
                MidiPane tempMidiPane = cells.get(arr);
                Rectangle tempR = (Rectangle) tempMidiPane.getChildren().toArray()[0];
                tempR.setFill(Color.RED);
                tempMidiPane.getStyleClass().remove("grid-cell-off");
                tempMidiPane.getStyleClass().add("grid-cell-on");
                if (i == 0) {
                    tempMidiPane.setLeft(false);
                    tempMidiPane.setRight(true);
                } else if (i == noteLength-1) {
                    tempMidiPane.setLeft(true);
                    tempMidiPane.setRight(false);
                } else {
                    tempMidiPane.setLeft(true);
                    tempMidiPane.setRight(true);
                }
            }
            String note = getNote(row);
            System.out.println(note + " Added!");
            phase.addNote(note, noteLength, col);
        }
    }

    private void deleteNote(MidiPane pane, int noteLength) {
        int col = pane.getCol();
        int row = pane.getRow();
        System.out.println("col: " + col + ". row: " + row);


        for (int i = 0; i < noteLength; i++) {
            Integer[] indexes = {col + i, row};
            IntegerArray arr = new IntegerArray(indexes);
            MidiPane tempMidiPane = cells.get(arr);
            Rectangle tempR = (Rectangle) tempMidiPane.getChildren().toArray()[0];
            tempR.setFill(Color.WHITESMOKE);
            tempMidiPane.getStyleClass().remove("grid-cell-on");
            tempMidiPane.getStyleClass().add("grid-cell-off");
            tempMidiPane.setLeft(false);
            tempMidiPane.setRight(false);
        }


//        Rectangle r = (Rectangle) pane.getChildren().toArray()[0];
//        r.setFill(Color.WHITESMOKE);
//        pane.getStyleClass().add("grid-cell-off");
        String note = getNote(row);
        System.out.println(note + " Deleted!");

        phase.deleteNote(note, noteLength, col);
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
