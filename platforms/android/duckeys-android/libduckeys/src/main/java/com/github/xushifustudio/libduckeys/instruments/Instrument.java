package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.Mode;

public interface Instrument {

    void apply(Chord chord);

    void apply(Mode mode);

}
