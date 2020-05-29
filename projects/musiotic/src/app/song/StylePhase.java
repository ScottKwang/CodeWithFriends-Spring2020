package song;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import ui.StyleScreen;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class StylePhase extends Phase {
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
        System.out.println("StylePhase: getScreen()");
        return screen.getScreen();
    }

    public LinkedHashMap<Type, Phase> getPhases(){
        System.out.println("StylePhase: getPhases()");
        var phases = new LinkedHashMap<Type, Phase>(); // preserve insertion order (button order on UI depends on this)
        phases.put(Type.Style, this);
        for(PhaseDetail phase : style.getPhases()){
            Type type = phase.getKey();
            System.out.println("StylePhase: getPhases() phase into hashmap: " + type.name);
            phases.put(type, type.getPhase(manager));
            System.out.println("StylePhase: getPhases() HashMap State: " + phases.toString());
        }

        for(Phase phase : phases.values()){
            if(phase == this) continue;
            var prereqs = phase.getType().getPrereqs();
            var props = new ArrayList<BooleanProperty>();
            for(Type prereq : prereqs) {
                if (prereq == Type.Style) {
                    System.out.println("StylePhase: getPhases() prereq == Style");
                    props.add(completed);
                } else {
                    System.out.println("StylePhase: getPhases() prereq == " + prereq.name);
                    props.add(phases.get(prereq).completed);
                }
            }
            phase.disabled = Bindings.createBooleanBinding(() ->
                !props.stream().allMatch(BooleanProperty::getValue),
                props.toArray(Observable[]::new)
            );
        }

        return phases;
    }

    public void setStyle(Style style) {
        System.out.println("StylePhase: setStyle(style)");
        if(!completed.getValue()){
            this.style = style;
            completed.setValue(true);
            manager.populateStyle();
        }
        else if(this.style != style){
            throw new IllegalStateException("A style has already been selected for this song.");
        }
    }

    public Style getStyle(){
        return style;
    }
}
