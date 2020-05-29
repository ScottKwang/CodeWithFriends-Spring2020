package ui;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import song.InstrumentalPhase;
import song.Phase;
import util.CustomTimerTask;
import util.IntegerArray;
import util.Scale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MidiGrid {
    private final InstrumentalPhase phase;
    private final HBox actionButtons;
    private ScrollPane mainPane;
    private GridPane gridPane;

    // Setting to each pane which {col, row} it's in.
    private HashMap<IntegerArray, MidiPane> cells;

    // True if currently playing
    private boolean playing = false;
    Timer playingTimer;


    //TODO: CSS measure borders THICCER
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

    public MidiGrid(InstrumentalPhase phase) {
        this.phase = phase;
        gridPane = new GridPane();
        gridPane.setId("grid-pane");
//        gridPane.setId("grid-pane");
        mainPane = new ScrollPane();
        mainPane.setPannable(false); // So click + drag doesn't move scroll inadvertently
        mainPane.setContent(gridPane);


        String styleSheet = getClass().getResource("/css/midi_grid.css").toExternalForm();
        gridPane.getStylesheets().add(styleSheet);
        String scrollStyle = getClass().getResource("/css/scroll.css").toExternalForm();
        mainPane.getStylesheets().add(scrollStyle);
        //gridPane.setStyle("-fx-font-size: 11px;");

        // https://stackoverflow.com/questions/32544574/javafx-scrollpane-horizontal-panning-with-scroll-wheel
        mainPane.setOnScroll(event -> {
            if (event.getDeltaY() > 0)
                mainPane.setHvalue(scrollPos == scrollMinPos ? scrollMinPos : scrollPos--);
            else
                mainPane.setHvalue(scrollPos == scrollMaxPos ? scrollMaxPos : scrollPos++);
        });
        mainPane.setHmin(scrollMinPos);
        mainPane.setHmax(scrollMaxPos);

        cells = new HashMap<IntegerArray, MidiPane>();

//        initializeCells(gridPane);

        BorderPane modeButtons = initializeModeButtons();
        modeButtons.getStyleClass().add("mode-buttons");
        BorderPane lengthButtons = initializeNoteLengthButtons();
        lengthButtons.getStyleClass().add("length-buttons");
        BorderPane playButton = initializePlayButton();
        playButton.getStyleClass().add("play-buttons");
        BorderPane addMeasureButtons = initializeAddMeasureButtons();
        addMeasureButtons.getStyleClass().add("add-measure-buttons");

        actionButtons = new HBox(modeButtons, lengthButtons, playButton, addMeasureButtons);
        actionButtons.getStyleClass().add("buttons");
    }

    private BorderPane initializeAddMeasureButtons() {
        Button left = new Button("Left\n←");
        Button right = new Button("Right\n→");
        makeToolTip(left, "Click to add four empty measures to the left.");
        makeToolTip(right, "Click to add four empty measures to the right.");

        left.setOnMouseClicked(e -> {
            addMeasures(false);
        });
        right.setOnMouseClicked(e -> {
            addMeasures(true);
        });

        HBox buttons = new HBox(left, right);
        buttons.setSpacing(10);
        BorderPane pane = new BorderPane();
        Label label = new Label("Add Measures");

        pane.setTop(label);
        pane.setCenter(buttons);
        pane.setAlignment(label, Pos.CENTER);
        return pane;
    }


    private BorderPane initializePlayButton() {
        String playImage = getClass().getResource("/images/play.png").toExternalForm();

        ImageView imageView = new ImageView(new Image(playImage));
        imageView.setFitHeight(75);
        imageView.setFitWidth(75);
        Label label = new Label("Play");
        Button button = new Button("", imageView);

        button.setPadding(Insets.EMPTY);
        BorderPane pane = new BorderPane();
        pane.setTop(label);
        pane.setAlignment(label, Pos.CENTER);
        pane.setCenter(button);

        playingTimer = new Timer();


        button.setOnMouseClicked(e -> {
            BorderPane tempPane = (BorderPane) button.getParent();
            setPlayButton(tempPane, false);
        });

        return pane;
    }

    private void setPlayButton(BorderPane tempPane, boolean fromTimer) {
        if (!playing) {
            //START PLAYING
//            System.out.println("START PLAYING");
            playing = true;
            phase.manager.play(0);
            //TODO: Create a function that will play the number measure from starting slider.

            Label label = (Label) tempPane.getChildren().get(0);
            Button button = (Button) tempPane.getChildren().get(1);

            label.setText("Stop");

            String playImage = getClass().getResource("/images/stop.png").toExternalForm();
            ImageView imageView = new ImageView(new Image(playImage));
            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            button.setGraphic(imageView);

            double time = (double)(phase.manager.numMeasures*4) * 60 * 1000 / phase.manager.getTempo();
            time += 200;
            System.out.println("Playing for ms: " + time);
            playingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        setPlayButton(tempPane, true);
                    });
                }
            }, (int) time);

        } else {
            //STOP PLAYING
            playingTimer.cancel();
            playingTimer = new Timer();
//            System.out.println("STOP PLAYING");
            playing = false;
            if (!fromTimer) {
                phase.manager.stop();
            }

            Label label = (Label) tempPane.getChildren().get(0);
            Button button = (Button) tempPane.getChildren().get(1);

            label.setText("Play");

            String playImage = getClass().getResource("/images/play.png").toExternalForm();
            ImageView imageView = new ImageView(new Image(playImage));
            imageView.setFitHeight(75);
            imageView.setFitWidth(75);
            button.setGraphic(imageView);
        }
    }

    private BorderPane initializeModeButtons() {
        Button add = new Button("Add Note");
        Button edit = new Button("Resize Note");
        Button delete = new Button("Delete Note");

        makeToolTip(add, "Click anywhere to add a note.");
        makeToolTip(edit, "Click on an existing note, then click again on another spot on that same row to extend/shrink your note.");
        makeToolTip(delete, "Click on any note to delete that note.");

        changeEffects(new Lighting(), add, edit, delete);
        add.setOnMouseClicked(e -> {
//            System.out.println("MODE: ADD");
            mode = "ADD";
            changeEffects(new Lighting(), add, edit, delete);
        });
        edit.setOnMouseClicked(e -> {
//            System.out.println("MODE: EDIT");
            mode = "EDIT";
            changeEffects(new Lighting(), edit, add, delete);
        });
        delete.setOnMouseClicked(e -> {
//            System.out.println("MODE: DELETE");
            mode = "DELETE";
            changeEffects(new Lighting(), delete, add, edit);
        });
        HBox buttons = new HBox(add, edit, delete);
        buttons.setSpacing(10);
        Label label = new Label("Modes");
        BorderPane pane = new BorderPane();
        pane.setCenter(buttons);
        pane.setTop(label);
        pane.setAlignment(label, Pos.CENTER);
        return pane;
    }

    private BorderPane initializeNoteLengthButtons() {
        Button sixteenthNote = new Button("¼");
        Button eighthNote = new Button("½");
        Button quarterNote = new Button("1");
        Button halfNote = new Button("2");
        Button wholeNote = new Button("4");
        Button doubleWholeNote = new Button("8");

        makeToolTip(sixteenthNote, "Add Sixteenth Notes");
        makeToolTip(eighthNote, "Add Eighth Notes");
        makeToolTip(quarterNote, "Add Quarter Notes");
        makeToolTip(halfNote, "Add Half Notes");
        makeToolTip(wholeNote, "Add Whole Notes");
        makeToolTip(doubleWholeNote, "Add Double Whole Notes");

        changeEffects(new Lighting(), quarterNote, sixteenthNote, eighthNote, halfNote, wholeNote, doubleWholeNote);
        sixteenthNote.setOnMouseClicked(e -> {
            noteLength = 1;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
            changeEffects(new Lighting(), sixteenthNote, eighthNote, quarterNote, halfNote, wholeNote, doubleWholeNote);
        });
        eighthNote.setOnMouseClicked(e -> {
            noteLength = 2;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
            changeEffects(new Lighting(), eighthNote, sixteenthNote, quarterNote, halfNote, wholeNote, doubleWholeNote);
        });
        quarterNote.setOnMouseClicked(e -> {
            noteLength = 4;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
            changeEffects(new Lighting(), quarterNote, sixteenthNote, eighthNote, halfNote, wholeNote, doubleWholeNote);
        });
        halfNote.setOnMouseClicked(e -> {
            noteLength = 8;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
            changeEffects(new Lighting(), halfNote, sixteenthNote, eighthNote, quarterNote, wholeNote, doubleWholeNote);
        });
        wholeNote.setOnMouseClicked(e -> {
            noteLength = 16;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
            changeEffects(new Lighting(), wholeNote, sixteenthNote, eighthNote, quarterNote, halfNote, doubleWholeNote);
        });
        doubleWholeNote.setOnMouseClicked(e -> {
            noteLength = 32;
            arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);
            changeEffects(new Lighting(), doubleWholeNote, sixteenthNote, eighthNote, quarterNote, halfNote, wholeNote);
        });

        HBox buttons = new HBox(sixteenthNote, eighthNote, quarterNote, halfNote, wholeNote, doubleWholeNote);
        buttons.setSpacing(10);
        Label label = new Label("Note Lengths");
        BorderPane pane = new BorderPane();
        pane.setCenter(buttons);
        pane.setTop(label);
        pane.setAlignment(label, Pos.CENTER);
        return pane;
    }

    private void changeEffects(Effect effect, Button change, Button ... buttons) {
        change.setEffect(effect);
        for (Button button : buttons) {
            button.setEffect(null);
        }
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
            Scale scale = phase.manager.getScale();
            List<SimpleStringProperty> scaleNames = scale.getNames();
            List<SimpleIntegerProperty> scaleValues = scale.getValues();
            int noteStart = 0;
            if (phase.getType() == Phase.Type.Melody1) {
                noteStart = 7;
//                System.out.println("we MELODY");
            } else {
//                System.out.println("we BASS");
                noteStart = 0;
            }


            for(int j = 0; j < phase.manager.numNotes; j++) {
                Label noteLabel = new Label();
                noteLabel.textProperty().bind(scaleNames.get(j + noteStart));

                Label noteValue = new Label();
                //Sum of 0 + scaleValues.get(j)
                NumberBinding noteVal = Bindings.add(0,scaleValues.get(j + noteStart));
                noteValue.textProperty().setValue(noteVal.getValue().toString());
//                System.out.println("noteVal " + noteVal.getValue().toString());
                noteValue.setVisible(false);
                HBox note = new HBox(noteLabel, noteValue);
                note.setId("note-label");
                note.setAlignment(Pos.CENTER);
                gridPane.add(note, 0, phase.manager.numNotes - j, 1, 1);
            }

            // The GRID
            for(int i = 1; i < phase.manager.numMeasures*16 + 1; i++) {
                for(int j = 1; j < phase.manager.numNotes + 1; j++) {
                    Integer[] indexes = {i-1, j-1};
                    IntegerArray arr = new IntegerArray(indexes);
                    cells.put(arr, createCell(Color.WHITESMOKE, gridPane, i-1, j-1));
//                    System.out.println("CELLS.PUT: row: " + j + " col: " + i);
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
        Rectangle rectangle = new Rectangle(25, 50, c);
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
                } else if (((Rectangle) (midiPane.getChildren().toArray()[0])).getFill() == Color.WHITESMOKE ) {
                    arrangeBorder(midiPane, noteLength);
                }
            }
        }
    }

    private void arrangeBorder(MidiPane midiPane, int noteLength) {
        if (noteLength > 3) {
            arrangeBorderLess(midiPane);
        } else if (noteLength > 1) {
            arrangeBorderSome(midiPane);
        } else {
            arrangeBorderMost(midiPane);
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

    private void arrangeBorderMost(MidiPane midiPane) {
        // noteLength is 1
        int col = midiPane.getCol();
//        System.out.println("arrangeBorderMost(): col: " + col);
        if (col % 16 == 0) {
            //Start Measure, Left Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,3), Insets.EMPTY)));
        } else if (col % 16 == 15) {
            //End Measure, Right Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.DOTTED,
                    CornerRadii.EMPTY, new BorderWidths(1,3,1,1), Insets.EMPTY)));
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

    private void arrangeBorderSome(MidiPane midiPane) {
        // noteLength is 4 or 2
        int col = midiPane.getCol();

//        System.out.println("arrangeBorderSome(): col: " + col);
        if (col % 16 == 0) {
            //Start Measure, Left Solid
//            System.out.println("Left Solid");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,3), Insets.EMPTY)));
        } else if (col % 16 == 15) {
            //End Measure, Right Solid
//            System.out.println("Right Solid");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,3,1,0), Insets.EMPTY)));
        } else if (col % 4 == 0) {
//            Left side Dashed
//            System.out.println("Left Dashed");
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.DASHED,
                    CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
        } else if (col % 4 == 3) {
            //Right side Dashed
//            System.out.println("Right Dashed");
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
//            System.out.println("Right Dotted");
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

    private void arrangeBorderLess(MidiPane midiPane) {
        // noteLength is 32, 16, or 8.
        int col = midiPane.getCol();

//        System.out.println("arrangeBorderLess(): col: " + col);
//        System.out.println("from pane: col: " + midiPane.getCol());
        if (col % 16 == 0) {
            //Start Measure, Left Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(1,0,1,3), Insets.EMPTY)));
        } else if (col % 16 == 15) {
            //End Measure, Right Solid
            midiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                    BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                    CornerRadii.EMPTY, new BorderWidths(1,3,1,0), Insets.EMPTY)));
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
            switch(mode) {
                case "ADD":
//                    System.out.println("ADD");
                    MidiPane startPane = cells.get(new IntegerArray(new Integer[]{ (int)((int)(pane.getCol() / noteLength) * noteLength), pane.getRow()}));
                    addNote(startPane, noteLength);
                    break;
                case "EDIT":
//                    System.out.println("EDIT");
                    //TODO: I believe there's still a bug that if a note is extended over another (consuming it), it doesnt delete that note.
                    // HAVE TO FIX THIS
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
//                    System.out.println("DELETE");
                    ArrayList<Integer> list = findConnectedMidiPanes(pane.getCol(), pane.getRow());
                    if (list == null) {
//                        System.out.println("Nothing to Delete!");
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
//            System.out.println("onDragDetected");

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
//                System.out.println("onDragOver");
                event.acceptTransferModes(TransferMode.MOVE);
            } else {
//                System.out.println("onDragOver SAME OBJECT");
            }

            event.consume();
        });

        pane.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
//            System.out.println("onDragEntered");
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
//            System.out.println("onDragDropped");
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

                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(true);

            }
            event.consume();
        });

        pane.setOnDragDone(event -> {
            /* the drag-and-drop gesture ended */
//            System.out.println("onDragDone");
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

        while (node.isLeft()) {
            startCol--;
            node = cells.get(new IntegerArray(new Integer[] {startCol, row}));
        }
        node = cells.get(new IntegerArray(new Integer[] {endCol, row}));
        while (node.isRight()) {
            endCol++;
            node = cells.get(new IntegerArray(new Integer[] {endCol, row}));
        }

        ArrayList<Integer> res = new ArrayList<>();
        res.add(startCol);
        res.add(endCol-startCol+1);
        //Returns column start and it's note length.
        int tempLength = endCol-startCol+1;
//        System.out.println("findConnectedMidiPanes(): temp length: " + tempLength);
        return res;

    }

    private void addNote(MidiPane pane, int noteLength) {
        int col = pane.getCol();
        int row = pane.getRow();
//        System.out.println("col: " + col + ". row: " + row);
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
                phase.completed.setValue(true);
                Integer[] indexes = {col + i, row};
                IntegerArray arr = new IntegerArray(indexes);
                MidiPane tempMidiPane = cells.get(arr);
                Rectangle tempR = (Rectangle) tempMidiPane.getChildren().toArray()[0];
                tempR.setFill(Color.BLUE);
                tempMidiPane.getStyleClass().remove("grid-cell-off");
                tempMidiPane.getStyleClass().add("grid-cell-on");
                if (noteLength == 1) {
                    tempMidiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY, new BorderWidths(1,1,1,1), Insets.EMPTY)));
                } else if (i == 0) {
                    tempMidiPane.setLeft(false);
                    tempMidiPane.setRight(true);
                    tempMidiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                            BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY, new BorderWidths(1,0,1,1), Insets.EMPTY)));
                } else if (i == noteLength-1) {
                    tempMidiPane.setLeft(true);
                    tempMidiPane.setRight(false);
                    tempMidiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                            BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                            CornerRadii.EMPTY, new BorderWidths(1,1,1,0), Insets.EMPTY)));
                } else {
                    tempMidiPane.setLeft(true);
                    tempMidiPane.setRight(true);
                    tempMidiPane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK,
                            BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE,
                            CornerRadii.EMPTY, new BorderWidths(1,0,1,0), Insets.EMPTY)));
                }
            }
            int note = getNote(row);
            System.out.println(note + " Added!");
            phase.addNote(note, noteLength, col);
        }
    }

    private void deleteNote(MidiPane pane, int noteLength) {
        int col = pane.getCol();
        int row = pane.getRow();
//        System.out.println("col: " + col + ". row: " + row);

        for (int i = 0; i < noteLength; i++) {
            Integer[] indexes = {col + i, row};
            IntegerArray arr = new IntegerArray(indexes);
            MidiPane tempMidiPane = cells.get(arr);
            arrangeBorder(tempMidiPane, this.noteLength);
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
        int note = getNote(row);
        System.out.println(note + " Deleted!");

        phase.deleteNote(note, noteLength, col);
    }

    private int getNote(int row) {
        String noteName = "";
        String noteValue = "";
        // i is 1 here so we don't check the cell at 0,0.
        for (int i = 1; i < gridPane.getChildren().size(); i++) {
            Node node = gridPane.getChildren().get(i);
            if (node != null) {
                try {
                    if (gridPane.getColumnIndex(node) == 0 && (gridPane.getRowIndex(node)-1) == row) {
                        Label labelName = (Label) (((HBox) node).getChildren().get(0));
                        noteName = labelName.getText();
                        Label labelValue = (Label) (((HBox) node).getChildren().get(1));
                        noteValue = labelValue.getText();
                    }
                } catch(NullPointerException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return Integer.valueOf(noteValue);
    }

    private void addMeasures(boolean right) {
        int oldNumMeasures = phase.manager.numMeasures;
        phase.manager.addMeasures();
        for(int i = oldNumMeasures; i < phase.manager.numMeasures; i++) {
            Label measureNumber = new Label(" Measure " + Integer.toString(i + 1));
            gridPane.add(measureNumber, i*16 + 1, 0, 4, 1);
        }
        for(int i = oldNumMeasures*16; i < phase.manager.numMeasures*16 + 1; i++) {
            for(int j = 1; j < phase.manager.numNotes + 1; j++) {
                Integer[] indexes = {i-1, j-1};
                IntegerArray arr = new IntegerArray(indexes);
                cells.put(arr, createCell(Color.WHITESMOKE, gridPane, i-1, j-1));
            }
        }
        arrangeBorders(phase.manager.numNotes, phase.manager.numMeasures);

        if (!right) {
            for(int i = oldNumMeasures*16; i >= 1; i--) {
                for(int j = 1; j < phase.manager.numNotes + 1; j++) {
                    ArrayList<Integer> list = findConnectedMidiPanes(i-1, j-1);
                    if (list != null) {
                        // we've found an pane
                        int col = list.get(0);
                        int length = list.get(1);
                        MidiPane pane = cells.get(new IntegerArray(new Integer[]{col, j-1}));
                        deleteNote(pane, length);
                        MidiPane newPane = cells.get(new IntegerArray(new Integer[]{col+(16*4), j-1}));
                        addNote(newPane, length);
                    }
                }
            }
        }
    }

    public void makeToolTip(Node node, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.seconds(0.05));
        Tooltip.install(node, tooltip);
    }

    public Node getActionButtons() {
        return actionButtons;
    }

    public Node getGrid() {
        return mainPane;
    }
}
