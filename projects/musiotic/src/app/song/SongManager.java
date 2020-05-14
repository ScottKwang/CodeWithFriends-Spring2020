package song;

public class SongManager {
    private Phase currentPhase;

    public SongManager(){
        currentPhase = new StylePhase(this);
    }
}
