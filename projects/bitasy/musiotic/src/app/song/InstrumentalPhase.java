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
    private HashMap<Integer, List<Phrase>> backupPhraseMap;
    private HashMap<Phrase, Phrase> backupConnectedMeasures;
    private Part backupPart;

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

    @Override
    public void initialize(){
        if(manager.getScale() == null) return;
        for(var pitch : manager.getScale().getValues()){
            var phraseList = new ArrayList<Phrase>();
            phraseMap.put(pitch.get(), phraseList);
            var newPhraseMap = new HashMap<Integer, List<Phrase>>();
            pitch.addListener((e, oldV, newV) -> {
                newPhraseMap.put(newV.intValue(), newPhraseMap.get(oldV.intValue()));
                if(newPhraseMap.size() == phraseMap.size()){
                    phraseMap.clear();
                    phraseMap.putAll(newPhraseMap);
                    newPhraseMap.clear();
                }
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
            if(connectedMeasures.containsKey(measure)) {
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
            boolean containsM1 = false;
            boolean containsM2 = false;
            for(var phrase : part.getPhraseList()){
                if(!containsM1 && phrase == m1) containsM1 = true;
                if(!containsM2 && phrase == m2) containsM2 = true;
            }
            if(!containsM1 && containsM2) continue;
            Note n1 = m1.getNote(m1.length() - 1);
            Note n2 = m2.getNote(0);
            m2.removeNote(n2);
            n1.setRhythmValue(n1.getRhythmValue() + n2.getRhythmValue(), true);
        }
    }

    public void addMeasure(){
            var end = part.getEndTime();
            for(var phrases : phraseMap.values()){
                var phrase = new Phrase();
                phrase.setStartTime(end);
                phrase.setAppend(false);
                phrase.addNote(new Note(JMC.REST, JMC.WHOLE_NOTE));
                part.addPhrase(phrase);
                phrases.add(phrase);
            }
    }

    public void backupPart(){
        Part oldPart = part.copy();
        oldPart.setTitle(part.getTitle());
        oldPart.removeAllPhrases();
        var phraseMapCopy = new HashMap<Integer, List<Phrase>>();
        var connectedMap = new HashMap<Phrase, Phrase>();
        var connectedCopy = new HashMap<Phrase, Phrase>();
        for(var pair : phraseMap.entrySet()){
            var listCopy = new ArrayList<Phrase>();
            for(Phrase phrase : pair.getValue()){
                var phraseCopy = phrase.copy();
                phraseCopy.setTitle(phrase.getTitle());
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

        backupPhraseMap = phraseMapCopy;
        backupConnectedMeasures = connectedCopy;
        backupPart = oldPart;
    }

    public Part consolidatePart() {
        // Deep copy fields for restoration

        // Prepare phrases for being played
        connectMeasures();
        for(var phrases : phraseMap.values()){
            var first = phrases.remove(0);
            while(!part.getPhraseList().contains(first)) {
                first = phrases.remove(0);
                first.setStartTime(0);
            }
            for(var after : phrases){
                Mod.append(first, after);
                part.removePhrase(after);
            }
        }
        phraseMap = backupPhraseMap;
        connectedMeasures = backupConnectedMeasures;
        part = backupPart;
        return part;
    }

    public void setMute(boolean muted) {
        for(var phrase : part.getPhraseArray()) phrase.setDynamic(muted ? 0 : 100);
    }

    abstract protected void setPlayButton(boolean playing);

    public void setInstrumentProperty(IntegerProperty instrumentProperty){
        instrumentProperty.addListener((e, oldV, newV) -> part.setInstrument(newV.intValue()));
    }
}
