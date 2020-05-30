package ui;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import song.Phase;

public abstract class OptionScreen implements Screen {

    private final Phase phase;
    private final BorderPane screen;

    protected OptionScreen(Phase phase) {
        this.phase = phase;
        screen = new BorderPane();
        String styleSheet = getClass().getResource("/css/option_screen.css").toExternalForm();
        screen.getStylesheets().add(styleSheet);
    }

    protected void setTop(Node top){
        screen.setTop(top);
    }

    protected void setBottom(Node bottom){
        screen.setBottom(bottom);
    }

    protected void setLeft(Node left){
        screen.setLeft(left);
    }

    protected void setRight(Node right){
        screen.setRight(right);
    }

    protected void setCenter(Node center) {
        screen.setCenter(center);
    }

    // Allow each phase to be styled independently
    protected void setStyleSheet(String cssURI){
        String styleSheet = getClass().getResource(cssURI).toExternalForm();
        screen.getStylesheets().add(styleSheet);
    }

    @Override
    public Node getScreen() {
        return screen;
    }

    //TODO make function class
    public void makeToolTip(Node node, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.seconds(0.05));
        Tooltip.install(node, tooltip);
    }

}
