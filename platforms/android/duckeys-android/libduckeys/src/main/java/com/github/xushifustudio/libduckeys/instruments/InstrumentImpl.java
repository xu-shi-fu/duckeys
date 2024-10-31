package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.Mode;

public class InstrumentImpl implements Instrument {

    private final InstrumentContext mIC;

    public InstrumentImpl(InstrumentContext ic) {
        this.mIC = ic;
    }

    @Override
    public void apply(Chord chord) {
        ChordManager cm = mIC.getChordManager();
        cm.output.setWant(chord);
        cm.apply(mIC, cm.output, false);
    }

    @Override
    public void apply(Mode mode) {
        ModeManager mm = mIC.getModeManager();
        mm.setWant(mode);
        mm.apply(mIC, false);
    }
}
