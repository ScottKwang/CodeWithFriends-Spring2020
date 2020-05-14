package song;

import java.util.ArrayList;
import java.util.List;

public class StylePhase extends Phase{
    private Style currentStyle;

    public StylePhase(SongManager manager) {
        super(manager);
    }

    @Override
    public Type getName() {
        return Type.Style;
    }

    @Override
    public List<Phase> getPrereqs() {
        return List.of();
    }

    public Style getCurrentStyle() {
        return currentStyle;
    }

    public void setCurrentStyle(Style currentStyle) {
        this.currentStyle = currentStyle;
    }

    public List<Phase> getPhases(){
        List<Phase> phases = new ArrayList<>();
        for(Type phase : currentStyle.phases){
            phases.add(phase.getPhase(manager));
        }

        return phases;
    }
}
