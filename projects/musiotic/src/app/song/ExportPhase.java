package song;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ExportPhase extends Phase{
    Label title = new Label("Title");
    TextField titleArea = new TextField();
    Button export = new Button("Export");
    HBox row = new HBox(title, titleArea, export);

    public ExportPhase(SongManager manager) {
        super(manager);
        export.disableProperty().bind(titleArea.textProperty().isEmpty());
        export.setOnMouseClicked(e -> {
            manager.writeScore(titleArea.getText());
        });
    }

    @Override
    public Type getType() {
        return Type.Export;
    }

    @Override
    public Node getScreen() {
        return row;
    }
}
