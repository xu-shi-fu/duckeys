package com.github.xushifustudio.libduckeys.midi;


import androidx.annotation.NonNull;

/**
 * Mode 表示曲调，例如 “C# major”
 */
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

    public static boolean equal(Mode a, Mode b) {
        if (a == null || b == null) {
            return false;
        }
        boolean b1 = Note.equal(a.note1, b.note1);
        boolean b2 = ModePattern.equal(a.pattern, b.pattern);
        return (b1 && b2);
    }


    @NonNull
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(note1.key).append(note1.sharp ? "# " : " ");
        b.append(pattern.name());
        return b.toString();
    }

    public Note getNote1() {
        return note1;
    }

    public ModePattern getPattern() {
        return pattern;
    }
}
