package song;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ExportPhase extends Phase {
    Label title = new Label("Enter a title for your song");
    TextField titleArea = new TextField();
    Button export = new Button("Export");
    HBox titleBox = new HBox(title, titleArea);
    VBox exportBox = new VBox(titleBox, export);

    public ExportPhase(SongManager manager) {
        super(manager);
        export.disableProperty().bind(titleArea.textProperty().isEmpty());
        export.setOnMouseClicked(e -> {
            manager.writeScore(titleArea.getText());
        });

        titleBox.setStyle("-fx-spacing: 10;");
        exportBox.setStyle("-fx-padding: 25 0 0 25; -fx-spacing: 10;");
        export.setStyle("-fx-font-size: 18;" +
                "-fx-text-fill: black;" +
                "-fx-background-color: C9F0FF;" +
                "-fx-alignment: center;" +
                "-fx-pref-height: 75;" +
                "-fx-max-height: 75;" +
                "-fx-font-family: Garamond;");
        title.setStyle("-fx-font-size: 24;\n" +
                "-fx-font-family: Garamond;\n" +
                "-fx-font-weight: 700;");
    }

    @Override
    public Type getType() {
        return Type.Export;
    }

    @Override
    public Node getScreen() {
        return exportBox;
    }
}
