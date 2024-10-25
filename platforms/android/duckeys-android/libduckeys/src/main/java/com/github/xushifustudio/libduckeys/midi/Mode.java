package com.github.xushifustudio.libduckeys.midi;

public class Mode {

    private final Note note1;
    private final ModePattern pattern;

    public Mode(Note n1, ModePattern mp) {
        if (n1 == null) {
            n1 = Note.forNote(12 * 5);
        }
        if (mp == null) {
            throw new RuntimeException("param: ModePattern is null");
        }
        this.note1 = n1;
        this.pattern = mp;
    }

    public Note getNote1() {
        return note1;
    }

    public ModePattern getPattern() {
        return pattern;
    }
}
