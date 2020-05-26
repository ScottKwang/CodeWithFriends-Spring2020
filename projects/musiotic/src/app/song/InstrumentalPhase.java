package song;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public abstract class InstrumentalPhase extends Phase {
    protected IntegerProperty instrument;
    protected Part part;
    private final List<Pair<Phrase, Phrase>> connectedMeasures = new ArrayList<>();

    public InstrumentalPhase(SongManager manager) {
        super(manager);

        var phases = manager.stylePhase.getStyle().getPhases();
        PhaseDetail detail = null;
        for(var phase : phases){
            if(phase.getType().equals(getType())){
                detail = phase;
                instrument = new SimpleIntegerProperty(phase.getInstruments().get(0)); // first instrument in list
                break;
            }
        }

        assert detail != null;
        part = new Part(getType().name, instrument.get(), detail.getChannel());
        instrument.addListener((observable, oldValue, newValue) -> part.setInstrument(newValue.intValue()));
        for(int i = 0; i < manager.numMeasures; i++)
            part.addNote(new Note(JMC.REST, JMC.WHOLE_NOTE), i * 4);
        manager.addPart(part);
    }

    public void addNote(String noteName, double noteLength, int notePosition) {
        noteLength /= 4;
        int note = KeyPhase.roots.get(noteName.substring(0, 1));
        String sign = noteName.length() > 1 ? noteName.substring(1) : "";
        switch (sign) {
            case "\uD834\uDD2A" -> note += 2; // Double Sharp
            case "♯" -> note++;
            case "\uD834\uDD2B" -> note -= 2;
            case "♭" -> note--;
        }

        Phrase measure = part.getPhrase(notePosition / 16);
        if (measure == null) {
            throw new IllegalStateException("This measure does not exist yet!");
        }
        double offset = (notePosition % 16) / 4.0;
        double position = 0;
        ListIterator iter = measure.getNoteList().listIterator();
        while (iter.hasNext()) {
            var existing = (Note) iter.next();
            if (position + existing.getRhythmValue() < offset) {
                position += existing.getRhythmValue();
                continue;
            }
            assert existing.isRest();
            double before = offset - position;
            double after = position + existing.getRhythmValue() - offset - noteLength;
            double measureTime = measure.getEndTime() - measure.getStartTime();
            assert measureTime == 4;

            if(before <= 0) iter.remove();
            else existing.setRhythmValue(before, true);

            if(offset + noteLength > measureTime){
                double thisMeasure = measureTime - offset;
                double nextMeasure = noteLength - thisMeasure;
                Note newNote = new Note(note, thisMeasure);
                newNote.setMyPhrase(measure);
                iter.add(newNote);
                int nextMeasureNum = notePosition / 16 + 1;
                addNote(noteName, nextMeasure, nextMeasureNum * 16);
                connectedMeasures.add(new Pair<>(measure, part.getPhrase(nextMeasureNum)));

            } else {
                Note newNote = new Note(note, noteLength);
                newNote.setMyPhrase(measure);
                iter.add(newNote);
                if(after > 0){
                    Note afterNote = new Note(JMC.REST, after);
                    afterNote.setMyPhrase(measure);
                    iter.add(afterNote);
                }
            }
        }
    }

    public void deleteNote(String noteName, int noteLength, int notePosition){

    }

    public void connectMeasures(){
        for(var pair : connectedMeasures){
            Phrase m1 = pair.getKey();
            Phrase m2 = pair.getValue();
            if(m2 == part.getPhrase(0)) continue;
            Note n1 = m1.getNote(m1.length() - 1);
            Note n2 = m2.getNote(0);
            m2.removeNote(n2);
            n1.setRhythmValue(n1.getDuration() + n2.getDuration(), true);
        }
    }
}
