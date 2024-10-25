package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.ChordNote;
import com.github.xushifustudio.libduckeys.midi.ChordPattern;
import com.github.xushifustudio.libduckeys.midi.Mode;
import com.github.xushifustudio.libduckeys.midi.ModeNote;
import com.github.xushifustudio.libduckeys.midi.ModePattern;
import com.github.xushifustudio.libduckeys.midi.Note;

public class NotePatternManager {

    private final MyHandler[] handlers = new MyHandler[12];
    private final MyHandler handlerDefault;

    public NotePatternManager(Chord chord) {
        this.handlerDefault = new MyChordHandler(ChordNote.NONE);
        this.initWithChord(chord);
    }

    public NotePatternManager(Mode mode) {
        this.handlerDefault = new MyModeHandler(ModeNote.NONE);
        this.initWithMode(mode);
    }

    private interface MyHandler {

        void handle(KeyState ks);

    }

    private static class MyModeHandler implements MyHandler {
        final ModeNote value;

        MyModeHandler(ModeNote v) {
            this.value = v;
        }

        @Override
        public void handle(KeyState ks) {
            ks.mode = this.value;
        }
    }

    private static class MyChordHandler implements MyHandler {
        final ChordNote value;

        MyChordHandler(ChordNote v) {
            this.value = v;
        }

        @Override
        public void handle(KeyState ks) {
            ks.chord = this.value;
        }
    }

    private void initWithChord(Chord chord) {
        Note root = chord.getRoot();
        ChordPattern pattern = chord.getPattern();
        int count = pattern.count();
        for (int i = 0; i < count; i++) {
            Note note = pattern.getNote(i, root);
            ChordNote chord_note = getChordNoteByPatternIndex(i);
            MyHandler h = new MyChordHandler(chord_note);
            handlers[note.index % handlers.length] = h;
        }
    }

    private void initWithMode(Mode mode) {
        Note n1 = mode.getNote1();
        ModePattern pattern = mode.getPattern();
        int count = pattern.count();
        for (int i = 0; i < count; i++) {
            Note note = pattern.getNote(i, n1);
            ModeNote mode_note = getModeNoteByPatternIndex(i);
            MyHandler h = new MyModeHandler(mode_note);
            handlers[note.index % handlers.length] = h;
        }
    }

    private ModeNote getModeNoteByPatternIndex(int i) {
        switch (i) {
            case 0:
                return ModeNote.NOTE1;
            case 1:
                return ModeNote.NOTE2;
            case 2:
                return ModeNote.NOTE3;
            case 3:
                return ModeNote.NOTE4;
            case 4:
                return ModeNote.NOTE5;
            case 5:
                return ModeNote.NOTE6;
            case 6:
                return ModeNote.NOTE7;
            case 7:
                return ModeNote.NOTE8;
            case 8:
                return ModeNote.NOTE9;
            case 9:
                return ModeNote.NOTE10;
            default:
                break;
        }
        return ModeNote.NONE;
    }

    private ChordNote getChordNoteByPatternIndex(int i) {
        switch (i) {
            case 0:
                return ChordNote.ROOT;
            case 1:
                return ChordNote.NOTE3;
            case 2:
                return ChordNote.NOTE5;
            case 3:
                return ChordNote.NOTE7;
            case 4:
                return ChordNote.NOTE9;
            case 5:
                return ChordNote.NOTE11;
            case 6:
                return ChordNote.NOTE13;
            default:
                break;
        }
        return ChordNote.NONE;
    }

    private MyHandler getHandlerFor(KeyState ks) {
        Note note = ks.note;
        if (note == null) {
            return this.handlerDefault;
        }
        MyHandler[] all = this.handlers;
        int index = note.midi;
        if (index < 0) {
            return this.handlerDefault;
        }
        MyHandler h = all[index % all.length];
        if (h == null) {
            return this.handlerDefault;
        }
        return h;
    }

    public void applyTo(Keyboard keyboard) {
        for (int i = 0; i < 128; i++) {
            KeyState ks = keyboard.getKeyState(i);
            MyHandler h = getHandlerFor(ks);
            h.handle(ks);
        }
    }
}
