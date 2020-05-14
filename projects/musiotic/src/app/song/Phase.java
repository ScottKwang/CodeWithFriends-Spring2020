package song;

import java.util.List;

public abstract class Phase {
    protected SongManager manager;
    public Phase(SongManager manager){
        this.manager = manager;
    }

    public abstract Type getName();
    public abstract List<Phase> getPrereqs();

    public enum Type {
        Style("Style", StylePhase.class),
        Key("Key", null),
        Melody1("Melody 1", null),
        Melody2("Melody 2", null),
        Bass("Bass", null),
        Drums("Drums", null),
        Effects("Effects", null);

        public final String name;
        private final Class<? extends Phase> phaseClass;

        Type(String name, Class<? extends Phase> phaseClass){
            this.name = name;
            this.phaseClass = phaseClass;
        }

        public Phase getPhase(SongManager manager){
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

    }
}
