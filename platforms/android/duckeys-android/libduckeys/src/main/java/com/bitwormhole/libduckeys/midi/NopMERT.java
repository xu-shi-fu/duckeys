package com.bitwormhole.libduckeys.midi;

public final class NopMERT implements MidiEventHandler , MidiEventDispatcher {

    @Override
    public void dispatch(MidiEvent me) {
    }

    @Override
    public void handle(MidiEvent me) {
    }
}
