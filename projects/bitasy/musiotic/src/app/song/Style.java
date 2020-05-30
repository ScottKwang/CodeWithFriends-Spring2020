package song;

import javafx.util.Pair;
import jm.JMC;

import java.util.List;

// A pair that provides the instruments available for a particular phase in a Style (if applicable).
// If two or more phases share the same instrument bind index, the instrument used to render their part should be bound.
class PhaseDetail extends Pair<Phase.Type, List<Integer>> {
    private final Integer channel;
    private final String instrumentGroup;

    public PhaseDetail(Phase.Type phaseType){
        this(phaseType, null, null, null);
    }

    public PhaseDetail(Phase.Type phaseType, Integer channel, String instrumentGroup, List<Integer> instruments) {
        super(phaseType, instruments);
        this.instrumentGroup = instrumentGroup;
        if((channel == null) != (instrumentGroup == null)) throw new IllegalArgumentException(
                "Both channel and instrumentGroup must be set."
        );
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

    public String getInstrumentGroup() {
        if(instrumentGroup == null){
            throw new IllegalStateException("This phase is not instrumental.");
        }
        return instrumentGroup;
    }
}

public enum Style {
    PIANO("Piano",
            new PhaseDetail(Phase.Type.Key),
            new PhaseDetail(Phase.Type.Instrumentation),
            new PhaseDetail(Phase.Type.Melody1, 0, "Piano",
                    List.of(
                            JMC.PIANO,
                            JMC.BRIGHT_ACOUSTIC,
                            JMC.HONKYTONK_PIANO,
                            JMC.ELECTRIC_PIANO,
                            JMC.THUMB_PIANO)),
            new PhaseDetail(Phase.Type.Bass, 1, "Piano",
                    List.of(
                            JMC.PIANO,
                            JMC.BRIGHT_ACOUSTIC,
                            JMC.HONKYTONK_PIANO,
                            JMC.ELECTRIC_PIANO,
                            JMC.THUMB_PIANO)),
            new PhaseDetail(Phase.Type.Export)
    ),
    GUITAR("Acoustic Guitar", new PhaseDetail(Phase.Type.UnimplementedStyle)
    ),
    SYNTH("Synthesizer and Drums", new PhaseDetail(Phase.Type.UnimplementedStyle)
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
