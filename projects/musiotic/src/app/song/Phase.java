package song;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

import java.util.List;

public abstract class Phase {
    public final SongManager manager;
    public BooleanBinding disabled;
    public final BooleanProperty completed;
    public Phase(SongManager manager){
        this.manager = manager;
        completed = new SimpleBooleanProperty();
        completed.setValue(false);
    }

    public abstract Type getType();
    public abstract Node getScreen();

    public enum Type {
        Style("Style", StylePhase.class),
        Key("Key &\nTempo", KeyPhase.class, Style),
        Melody1("Melody", Melody1Phase.class, Key),
        Melody2("Countermelody", null,Key),
        Bass("Bass", BassPhase.class, Key),
        Drums("Drums", null, Key),
        Effects("Effects", null, Melody1, Bass, Drums),
        Export("Export", ExportPhase.class, Melody1),
        UnimplementedStyle("Unimplemented", UnimplementedStylePhase.class, Style);

        public final String name;
        private final Class<? extends Phase> phaseClass;
        private final List<Type> prereqs;

        Type(String name, Class<? extends Phase> phaseClass, Type... prereqs){
            this.name = name;
            this.phaseClass = phaseClass;
            this.prereqs = List.of(prereqs);
        }

        public Phase getPhase(SongManager manager){
            System.out.println("Phase: getPhase(manager)");
            if(phaseClass == null){
                throw new IllegalStateException(name + " hasn't been implemented yet!");
            }
            try {
                return phaseClass.getDeclaredConstructor(SongManager.class).newInstance(manager);
            } catch (Exception e) {
                // This should never happen.
                throw new RuntimeException(e);
            }
        }

        public List<Type> getPrereqs(){
            return prereqs;
        }

    }
}
