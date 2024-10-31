package com.github.xushifustudio.libduckeys.midi;

import java.util.ArrayList;
import java.util.List;

public final class Chords {

    private static SimpleChordDetector detector;

    private Chords() {
    }

    public static Chord detect(List<Note> notes) {
        SimpleChordDetector d = detector;
        if (d == null) {
            d = new SimpleChordDetector();
            detector = d;
        }
        return d.detect(notes);
    }

    public static Chord none() {
        Note root = Note.empty();
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("NONE");
        return new Chord(root, b.create());
    }

    // 三和弦 ///////////////////////////////////////////////////////////////////////////////////////

    public static Chord major(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("Major");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord minor(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("Minor");
        b.offset(0).offset(3).offset(7);
        return new Chord(root, b.create());
    }

    public static Chord dim(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("dim");
        b.offset(0).offset(3).offset(6);
        return new Chord(root, b.create());
    }

    public static Chord aug(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("aug");
        b.offset(0).offset(4).offset(8);
        return new Chord(root, b.create());
    }

    // 七和弦 ///////////////////////////////////////////////////////////////////////////////////////

    public static Chord dominant7th(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("Dominant 7th");
        b.offset(0).offset(4).offset(7).offset(10);
        return new Chord(root, b.create());
    }

    public static Chord major7th(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("Major 7th");
        b.offset(0).offset(4).offset(7).offset(11);
        return new Chord(root, b.create());
    }

    public static Chord minor7th(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("Minor 7th");
        b.offset(0).offset(3).offset(7).offset(10);
        return new Chord(root, b.create());
    }

    public static Chord theMinorMajor7th(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("Minor Major 7th");
        b.offset(0).offset(3).offset(7).offset(11);
        return new Chord(root, b.create());
    }

    public static Chord dim7(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("dim 7");
        b.offset(0).offset(3).offset(6).offset(9);
        return new Chord(root, b.create());
    }

    public static Chord m7b5(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("m7b5");
        b.offset(0).offset(3).offset(6).offset(10);
        return new Chord(root, b.create());
    }

    public static Chord thePlusM7(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("+M7");
        b.offset(0).offset(4).offset(8).offset(11);
        return new Chord(root, b.create());
    }

    public static Chord the7sharp5(Note root) {
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("7#5");
        b.offset(0).offset(4).offset(8).offset(10);
        return new Chord(root, b.create());
    }


    // 9和弦 ////////////////////////////////////////////////////////////////////////////////////////

    public static Chord major9(Note root) {
        // todo ...
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M9");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    // 11和弦 ///////////////////////////////////////////////////////////////////////////////////////


    public static Chord major11(Note root) {
        // todo ...
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M11");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }

    // 13和弦 ///////////////////////////////////////////////////////////////////////////////////////

    public static Chord major13(Note root) {
        // todo ...
        ChordPattern.Builder b = new ChordPattern.Builder();
        b.name("M13");
        b.offset(0).offset(4).offset(7);
        return new Chord(root, b.create());
    }


    // all /////////////////////////////////////////////////////////////////////////////////////////

    public static List<Chord> listAll(Note root, List<Chord> dst) {
        if (dst == null) {
            dst = new ArrayList<>();
        }
        if (root == null) {
            root = Note.forNote(12 * 5);
        }

        dst.add(major(root));
        dst.add(minor(root));
        dst.add(dim(root));
        dst.add(aug(root));

        dst.add(dim7(root));
        dst.add(dominant7th(root));
        dst.add(m7b5(root));
        dst.add(major7th(root));
        dst.add(minor7th(root));
        dst.add(the7sharp5(root));
        dst.add(theMinorMajor7th(root));
        dst.add(thePlusM7(root));

        return dst;
    }
}
