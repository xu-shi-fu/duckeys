package com.github.xushifustudio.libduckeys.midi;

public final class Chords {

    private Chords() {
    }


    public static Chord Major(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.offset(0).offset(4).offset(7);
        return new Chord(n1, b.create());
    }

    public static Chord Major7(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }

    public static Chord Major9(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }

    public static Chord Major11(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }

    public static Chord Major13(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }


    public static Chord minor(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }

    public static Chord minor7(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }

    public static Chord minor9(Note n1) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        return new Chord(n1, b.create());
    }

}
