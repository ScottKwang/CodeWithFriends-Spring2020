package song;

import javafx.util.Pair;
import jm.JMC;

import java.util.List;

// A pair that provides the instruments available for a particular phase in a Style (if applicable).
class PhaseDetail extends Pair<Phase.Type, List<Integer>> {
    private final Integer channel;
    public PhaseDetail(Phase.Type phaseType){
        this(phaseType, null, null);
    }

    public PhaseDetail(Phase.Type phaseType, Integer channel, List<Integer> instruments) {
        super(phaseType, instruments);
        this.channel = channel;
    }

    public Phase.Type getType(){
        return getKey();
    }

    public List<Integer> getInstruments(){
        return getValue();
    }

    public int getChannel(){
        if(channel == null){
            throw new IllegalStateException("This phase is not instrumental.");
        }
        return channel;
    }
}

public enum Style {
    PIANO("Classical Piano",
            new PhaseDetail(Phase.Type.Key),
            new PhaseDetail(Phase.Type.Melody1, 0,
                    List.of(
                            JMC.PIANO,
                            JMC.BRIGHT_ACOUSTIC,
                            JMC.HONKYTONK_PIANO,
                            JMC.ELECTRIC_PIANO,
                            JMC.THUMB_PIANO)),
            new PhaseDetail(Phase.Type.Bass, 1,
                    List.of(JMC.PIANO, JMC.BRIGHT_ACOUSTIC, JMC.HONKYTONK_PIANO, JMC.ELECTRIC_PIANO))
    ),
    GUITAR("Acoustic Guitar", new PhaseDetail(Phase.Type.Key)
    ),
    SYNTH("Synthesizer and Drums", new PhaseDetail(Phase.Type.Key)
    );

    public final String name;
    private final PhaseDetail[] phases;

    Style(String name, PhaseDetail... phases){
        this.name = name;
        this.phases = phases;
    }

    public static Style getStyle(String name){
        for(var style : values()){
            if(style.name.equals(name)) return style;
        }
        return null;
    }

    public PhaseDetail[] getPhases(){
        return phases.clone();
    }

}
