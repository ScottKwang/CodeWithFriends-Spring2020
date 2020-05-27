package song;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.tools.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public abstract class InstrumentalPhase extends Phase {
    protected IntegerProperty instrument;
    protected Part part;
    private Map<Integer, List<Phrase>> phraseMap = new HashMap<>();
    private Map<Phrase, Phrase> connectedMeasures = new HashMap<>();

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
        manager.addPart(part);
    }

    public void initialize(){
        for(var pitch : manager.getScale().getValues()){
            var phraseList = new ArrayList<Phrase>();
            phraseMap.put(pitch.get(), phraseList);
            pitch.addListener((e, oldV, newV) -> {
                phraseMap.put(newV.intValue(), phraseList);
            });
            for(int i = 0; i < manager.numMeasures; i++){
                var phrase = new Phrase();
                phrase.setStartTime(i * 4);
                phrase.setAppend(false);
                phrase.addNote(new Note(JMC.REST, JMC.WHOLE_NOTE));
                part.addPhrase(phrase);
                phraseList.add(phrase);
            }
        }
    }

    public void addNote(int note, double noteLength, int notePosition) {
        noteLength /= 4;

        var phraseList = phraseMap.get(note);
        Phrase measure = phraseList.get(notePosition / 16);
        if (measure == null) {
            throw new IllegalStateException("This measure does not exist yet!");
        }
        double offset = (notePosition % 16) / 4.0;
        double position = 0;
        ListIterator iter = measure.getNoteList().listIterator();
        while (iter.hasNext()) {
            var existing = (Note) iter.next();
            if (position + existing.getRhythmValue() <= offset) {
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
                addNote(note, nextMeasure * 4, nextMeasureNum * 16);
                connectedMeasures.put(measure, phraseList.get(notePosition / 16 + 1));

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
            return;
        }
    }

    public void deleteNote(int note, int noteLength, int notePosition){
        noteLength /= 4;
        var phraseList = phraseMap.get(note);
        Phrase measure = phraseList.get(notePosition / 16);
        double position = 0;
        double offset = (notePosition % 16) / 4.0;
        for(var existing : measure.getNoteArray()){
            if(position + existing.getRhythmValue() <= offset){
                position += existing.getRhythmValue();
                continue;
            }
            assert existing.getRhythmValue() == Math.min(noteLength,
                    measure.getEndTime() - measure.getStartTime() - offset); // for connected measures
            existing.setPitch(JMC.REST);
            if(connectedMeasures.containsKey(measure)){
                connectedMeasures.get(measure).getNoteArray()[0].setPitch(JMC.REST);
                connectedMeasures.remove(measure);
            }
            return;
        }
    }

    private void connectMeasures(){
        for(var pair : connectedMeasures.entrySet()){
            Phrase m1 = pair.getKey();
            Phrase m2 = pair.getValue();
            if(m2 == part.getPhrase(0)) continue;
            Note n1 = m1.getNote(m1.length() - 1);
            Note n2 = m2.getNote(0);
            m2.removeNote(n2);
            n1.setRhythmValue(n1.getRhythmValue() + n2.getRhythmValue(), true);
        }
    }

    public void addMeasure(boolean right){
        if(right){
            var end = part.getEndTime();
            for(var phrases : phraseMap.values()){
                var phrase = new Phrase();
                phrase.setStartTime(end);
                phrase.setAppend(false);
                phrase.addNote(new Note(JMC.REST, JMC.WHOLE_NOTE));
                part.addPhrase(phrase);
                phrases.add(phrase);
            }
        } else {
            for(var phrases : phraseMap.values()){
                for(var phrase : phrases)
                    phrase.setStartTime(phrase.getStartTime() + 4);
                var measure = new Phrase();
                measure.setStartTime(0);
                measure.setAppend(false);
                measure.addNote(new Note(JMC.REST, JMC.WHOLE_NOTE));
                part.addPhrase(measure);
                phrases.add(0, measure);
            }
        }
    }

    public Part consolidatePart() {
        // Deep copy fields for restoration
        Part oldPart = part.copy();
        oldPart.removeAllPhrases();
        var phraseMapCopy = new HashMap<Integer, List<Phrase>>();
        var connectedMap = new HashMap<Phrase, Phrase>();
        var connectedCopy = new HashMap<Phrase, Phrase>();
        for(var pair : phraseMap.entrySet()){
            var listCopy = new ArrayList<Phrase>();
            for(Phrase phrase : pair.getValue()){
                var phraseCopy = phrase.copy();
                listCopy.add(phraseCopy);
                if(connectedMeasures.containsKey(phrase)){
                    var valueCopy = connectedMap.get(connectedMeasures.get(phrase));
                    if(valueCopy != null) connectedCopy.put(phraseCopy, valueCopy);
                    else connectedMap.put(phrase, phraseCopy);
                }
                if(connectedMeasures.containsValue(phrase)){
                    Phrase keyCopy = null;
                    for(var entry : connectedMeasures.entrySet()){
                        if(entry.getValue() == phrase){
                            keyCopy = connectedMap.get(entry.getKey());
                            break;
                        }
                    }
                    if(keyCopy != null) connectedCopy.put(keyCopy, phraseCopy);
                    else connectedMap.put(phrase, phraseCopy);
                }
                oldPart.addPhrase(phraseCopy);
            }
            phraseMapCopy.put(pair.getKey(), listCopy);
        }
        // Prepare phrases for being played
        connectMeasures();
        for(var phrases : phraseMap.values()){
            var first = phrases.remove(0);
            for(var after : phrases){
                Mod.append(first, after);
                part.removePhrase(after);
            }
        }
        phraseMap = phraseMapCopy;
        connectedMeasures = connectedCopy;
        return oldPart;
    }
}
