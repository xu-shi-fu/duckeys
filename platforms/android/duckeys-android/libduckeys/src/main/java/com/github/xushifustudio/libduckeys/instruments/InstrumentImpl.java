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
        NotePatternManager npm = new NotePatternManager(chord);
        npm.applyTo(mIC.getKeyboard());
    }

    @Override
    public void apply(Mode mode) {
        NotePatternManager npm = new NotePatternManager(mode);
        npm.applyTo(mIC.getKeyboard());
    }
}
