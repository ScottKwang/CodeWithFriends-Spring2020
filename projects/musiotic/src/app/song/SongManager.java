package song;

import java.util.List;
import java.util.function.Consumer;

public class SongManager {
    public final StylePhase stylePhase;
    public final KeyPhase keyPhase;
    public Phase currentPhase;


    public SongManager(){
        stylePhase = new StylePhase(this);
        keyPhase = new KeyPhase(this);
        currentPhase = stylePhase;
    }

    private Consumer<List<Phase>> onStyleSelect;

    public void setOnStyleSelect(Consumer<List<Phase>> onStyleSelect){
        System.out.println("SongManager: setOnStyleSelect(onStyleSelect)");
        this.onStyleSelect = onStyleSelect;
    }

    public void populateStyle(){
        System.out.println("SongManager: populateStyle()");
        onStyleSelect.accept(stylePhase.getPhases());
    }



    //Key stuff
//    private Consumer<List<Phase>> onKeySelect;
//
//    public void setOnKeySelect(Consumer<List<Phase>> onKeySelect){
//        this.onKeySelect = onKeySelect;
//    }
//
//    public void populateKey(){
//        onKeySelect.accept(keyPhase.getPhases());
//    }

}
