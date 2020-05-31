package ui;

import javafx.scene.layout.Pane;

public class MidiPane extends Pane {

    private int row;
    private int col;

    private boolean left = false;
    private boolean right = false;

    MidiPane(int row, int col) {
        super();
        this.row = row;
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    protected int getRow() {
        return row;
    }

    protected int getCol() {
        return col;
    }

}
