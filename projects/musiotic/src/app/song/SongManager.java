package song;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public class SongManager {
    public final StylePhase stylePhase;
    public Map<Phase.Type, Phase> phaseMap;
    protected Phase currentPhase;


    public SongManager(){
        stylePhase = new StylePhase(this);
        currentPhase = stylePhase;
    }

    private Consumer<Collection<Phase>> onStyleSelect;

    public void setOnStyleSelect(Consumer<Collection<Phase>> onStyleSelect){
        System.out.println("SongManager: setOnStyleSelect(onStyleSelect)");
        this.onStyleSelect = onStyleSelect;
    }

    public void populateStyle(){
        System.out.println("SongManager: populateStyle()");
        phaseMap = stylePhase.getPhases();
        onStyleSelect.accept(phaseMap.values());
    }

    public void goToPhase(Phase.Type type){

    }
}
