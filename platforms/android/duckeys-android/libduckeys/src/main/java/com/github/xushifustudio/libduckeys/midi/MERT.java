package com.github.xushifustudio.libduckeys.midi;


// MERT : MIDI Event Rx/Tx
public interface MERT {

    MidiEventDispatcher getTx();

    void setRx(MidiEventHandler rx);

}