package song;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.util.Pair;
import ui.InstrumentationScreen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstrumentationPhase extends Phase {
    InstrumentationScreen screen;
    public InstrumentationPhase(SongManager manager) {
        super(manager);
    }

    @Override
    public void initialize() {
        Map<String, Pair<IntegerProperty, List<Integer>>> map = new HashMap<>();
        for(var phaseType : manager.stylePhase.getStyle().getPhases()){
            try {
                String group = phaseType.getInstrumentGroup();
                IntegerProperty ip;
                if (!map.containsKey(group)) {
                    ip = new SimpleIntegerProperty(0);
                    map.put(group, new Pair<>(ip, phaseType.getInstruments()));
                } else ip = map.get(group).getKey();
                ((InstrumentalPhase)manager.phaseMap.get(phaseType.getType())).setInstrumentProperty(ip);
            } catch (IllegalStateException ignored) {}
        }

        screen = new InstrumentationScreen(this, map);
    }

    @Override
    public Type getType() {
        return Type.Instrumentation;
    }

    @Override
    public Node getScreen() {
        return screen.getScreen();
    }
}
