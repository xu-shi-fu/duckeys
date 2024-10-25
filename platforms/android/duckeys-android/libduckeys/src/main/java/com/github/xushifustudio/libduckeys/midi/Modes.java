package com.github.xushifustudio.libduckeys.midi;

public final class Modes {

    private Modes() {
    }

    public static Mode major(Note n1) {
        ModePattern.Builder b = new ModePattern.Builder();
        //       1         2         3         4         5         6          7
        b.offset(0).offset(2).offset(4).offset(5).offset(7).offset(9).offset(11);
        return new Mode(n1, b.create());
    }

    public static Mode minor(Note n1) {
        ModePattern.Builder b = new ModePattern.Builder();
        b.offset(0).offset(4).offset(7);
        return new Mode(n1, b.create());
    }

}
