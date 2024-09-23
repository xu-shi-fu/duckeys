package com.bitwormhole.libduckeys.midi;

public interface MidiEventDispatcher {

    // void flush(); [替代：通过写入一个 null 表示 flush]

    void write(MidiEvent me);

}
