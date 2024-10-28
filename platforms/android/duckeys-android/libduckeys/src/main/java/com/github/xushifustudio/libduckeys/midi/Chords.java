package com.github.xushifustudio.libduckeys.midi;

public final class Chords {

    private Chords() {
    }

    public static Chord none() {
        Note root = Note.empty();
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("NONE");
        return new Chord(root, b.create());
    }

    public static Chord major(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord major7(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M7");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord major9(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M9");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord major11(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M11");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord major13(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M13");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }


    public static Chord minor(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("m");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord minor7(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("m7");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord minor9(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("m9");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }
}
