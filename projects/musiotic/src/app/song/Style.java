package song;

public enum Style {
    PIANO("Classical Piano", Phase.Type.Key, Phase.Type.Melody1),
    GUITAR("Acoustic Guitar"),
    SYNTH("Synthesizer and Drums");

    public final String name;
    public final Phase.Type[] phases;

    Style(String name, Phase.Type... phases){
        this.name = name;
        this.phases = phases;
    }

}
