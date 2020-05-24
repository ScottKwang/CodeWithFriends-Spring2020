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

    //TODO: For Brian here! noteName should be giving you the left side of the midi grids text.
    // noteLength: the length of the note in sixteenth note increments. So example: a quarter note would have length 4.
    // notePosition: position note starts at, if 0 is the first sixteenth note, then 4 would be where the second quarter note starts.
    public abstract void addNote(String noteName, int noteLength, int notePosition);
    public abstract void deleteNote(String noteName, int noteLength, int notePosition);


    public enum Type {
        Style("Style", StylePhase.class),
        Key("Key", KeyPhase.class, Style),
        Melody1("Melody", Melody1Phase.class, Style, Key),
        Melody2("Countermelody", null, Style, Key),
        Bass("Bass", BassPhase.class, Style, Key),
        Drums("Drums", null, Style, Key),
        Effects("Effects", null, Style, Key, Melody1, Bass, Drums);

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
