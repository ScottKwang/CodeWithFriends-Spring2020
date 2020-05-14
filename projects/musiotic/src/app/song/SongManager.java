package song;

import java.util.List;
import java.util.function.Consumer;

public class SongManager {
    public final StylePhase stylePhase;

    public SongManager(){
        stylePhase = new StylePhase(this);
    }

    private Consumer<List<Phase>> onStyleSelect;

    public void setOnStyleSelect(Consumer<List<Phase>> onStyleSelect){
        this.onStyleSelect = onStyleSelect;
    }

    public void populate(){
        onStyleSelect.accept(stylePhase.getPhases());
    }
}
