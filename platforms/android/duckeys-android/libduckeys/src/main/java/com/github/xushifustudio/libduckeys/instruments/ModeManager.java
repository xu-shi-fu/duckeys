package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.Mode;

public class ModeManager {

    private Mode want;
    private Mode have;

    public ModeManager() {
    }

    public void apply(InstrumentContext ic, boolean reload) {
        if (!reload) {
            if (Mode.equal(want, have)) {
                return;
            }
        }
        NotePatternManager npm = new NotePatternManager(want);
        npm.applyTo(ic.getKeyboard());
        have = want;
    }


    public Mode getWant() {
        return want;
    }

    public void setWant(Mode want) {
        this.want = want;
    }

    public Mode getHave() {
        return have;
    }

    public void setHave(Mode have) {
        this.have = have;
    }
}
