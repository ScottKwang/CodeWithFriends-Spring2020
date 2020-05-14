package song;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import ui.StyleScreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class StylePhase extends Phase{
    private final SongManager manager;
    private Style style;
    private final StyleScreen screen;

    public StylePhase(SongManager manager) {
        super(manager);
        this.manager = manager;
        disabled = Bindings.createBooleanBinding(() -> false);
        screen = new StyleScreen(this);
    }

    @Override
    public Type getType() {
        return Type.Style;
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }

    public List<Phase> getPhases(){
        var phases = new LinkedHashMap<Type, Phase>(); // preserve insertion order (button order on UI depends on this)
        for(Type phase : style.phases){
            phases.put(phase, phase.getPhase(manager));
        }

        for(Phase phase : phases.values()){
            var prereqs = phase.getType().getPrereqs();
            var props = new ArrayList<BooleanProperty>();
            for(Type prereq : prereqs){
                if(prereq == Type.Style){
                    props.add(completed);
                } else
                    props.add(phases.get(prereq).completed);
            }
            phase.disabled = Bindings.createBooleanBinding(() ->
                !props.stream().allMatch(BooleanProperty::getValue)
            );
        }

        return new ArrayList<>(phases.values());
    }

    public void setStyle(Style style) {
        if(this.style == null){
            this.style = style;
            completed.setValue(true);
            manager.populate();
        }
        else throw new IllegalStateException("A style has already been selected for this song.");
    }
}
