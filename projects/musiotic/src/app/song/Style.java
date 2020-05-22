package song;

public enum Style {
    PIANO("Classical Piano", Phase.Type.Key, Phase.Type.Melody1, Phase.Type.Bass),
    GUITAR("Acoustic Guitar", Phase.Type.Key),
    SYNTH("Synthesizer and Drums", Phase.Type.Key);

    public final String name;
    public final Phase.Type[] phases;

    Style(String name, Phase.Type... phases){
        this.name = name;
        this.phases = phases;
    }

    public static Style getStyle(String name){
        for(var style : values()){
            if(style.name.equals(name)) return style;
        }
        return null;
    }

}
