package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Chord;

public class ChordManager {

    private Chord want;
    private Chord have;

    public ChordManager() {
    }

    public void apply(InstrumentContext ic, boolean reload) {
        if (!reload) {
            if (Chord.equal(want, have)) {
                return;
            }
        }
        NotePatternManager npm = new NotePatternManager(want);
        npm.applyTo(ic.getKeyboard());
        have = want;
    }

    public Chord getWant() {
        return want;
    }

    public void setWant(Chord want) {
        this.want = want;
    }

    public Chord getHave() {
        return have;
    }

    public void setHave(Chord have) {
        this.have = have;
    }
}
