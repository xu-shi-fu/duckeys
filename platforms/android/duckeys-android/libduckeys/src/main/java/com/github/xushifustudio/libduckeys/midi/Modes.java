package com.github.xushifustudio.libduckeys.midi;

public final class Modes {

    private static final int W = 2; // whole
    private static final int H = 1; // half
    private static final int BASE = 0;

    private Modes() {
    }


    public static Mode none() {
        Note n1 = Note.forNote(0);
        ModePattern.Builder b = new ModePattern.Builder();
        b.name("NONE");
        return new Mode(n1, b.create());
    }

    public static Mode major(Note n1) {
        return inner_ionian(n1, "Major (Ionian)");
    }

    public static Mode minor(Note n1) {
        return inner_aeolian(n1, "Minor (Aeolian)");
    }

    public static Mode dorian(Note n1) {
        // todo : w-h-w-w-w-h-w
        ModePattern.Builder b = new ModePattern.Builder();
        b.name("Dorian");
        b.add(W).add(H).add(W).add(W).add(W).add(H).add(W);
        return new Mode(n1, b.create());
    }

    /*
    public static Mode hypodorian(Note n1) {
        // todo
        ModePattern.Builder b = new ModePattern.Builder();
        b.add().add().add().add().add().add().add();
        return new Mode(n1, b.create());
    }
    */

    public static Mode phrygian(Note n1) {
        // todo : h-w-w-w-h-w-w (e-f-g-a-b-c-d-e)
        ModePattern.Builder b = new ModePattern.Builder();
        b.name("Phrygian");
        b.add(H).add(W).add(W).add(W).add(H).add(W).add(W);
        return new Mode(n1, b.create());
    }

    /*
    public static Mode hypophrygian(Note n1) {
        // todo
        ModePattern.Builder b = new ModePattern.Builder();
        b.add().add().add().add().add().add().add();
        return new Mode(n1, b.create());
    }
    */


    public static Mode lydian(Note n1) {
        // todo : w-w-w-h-w-w-h (f-g-a-b-c-d-e-f)
        ModePattern.Builder b = new ModePattern.Builder();
        b.name("Lydian");
        b.add(W).add(W).add(W).add(H).add(W).add(W).add(H);
        return new Mode(n1, b.create());
    }

    /*
    public static Mode hypolydian(Note n1) {
        // todo
        ModePattern.Builder b = new ModePattern.Builder();
        b.add().add().add().add().add().add().add();
        return new Mode(n1, b.create());
    }
    */


    public static Mode mixolydian(Note n1) {
        // todo : w-w-h-w-w-h-w (g-a-b-c-d-e-f-g)
        ModePattern.Builder b = new ModePattern.Builder();
        b.name("Mixolydian");
        b.add(W).add(W).add(H).add(W).add(W).add(H).add(W);
        return new Mode(n1, b.create());
    }

    /*
    public static Mode hypomixolydian(Note n1) {
        // todo
        ModePattern.Builder b = new ModePattern.Builder();
        b.add().add().add().add().add().add().add();
        return new Mode(n1, b.create());
    }

     */

    public static Mode ionian(Note n1) {
        return inner_ionian(n1, "Ionian");
    }

    private static Mode inner_ionian(Note n1, String name) {
        // aka natural major
        // todo : w-w-h-w-w-w-h (c-d-e-f-g-a-b-c)
        ModePattern.Builder b = new ModePattern.Builder();
        b.name(name);
        b.add(W).add(W).add(H).add(W).add(W).add(W).add(H);
        return new Mode(n1, b.create());
    }

    public static Mode aeolian(Note n1) {
        return inner_aeolian(n1, "Aeolian");
    }

    private static Mode inner_aeolian(Note n1, String name) {
        // aka: natural minor
        // todo : w-h-w-w-h-w-w (a-b-c-d-e-f-g-a)
        ModePattern.Builder b = new ModePattern.Builder();
        b.name(name);
        b.add(W).add(H).add(W).add(W).add(H).add(W).add(W);
        return new Mode(n1, b.create());
    }

    public static Mode locrian(Note n1) {
        // todo : h-w-w-h-w-w-w (b-c-d-e-f-g-a-b)
        ModePattern.Builder b = new ModePattern.Builder();
        b.name("Locrian");
        b.add(H).add(W).add(W).add(H).add(W).add(W).add(W);
        return new Mode(n1, b.create());
    }
}
