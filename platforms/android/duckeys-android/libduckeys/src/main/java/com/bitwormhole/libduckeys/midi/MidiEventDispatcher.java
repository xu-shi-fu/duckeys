package com.bitwormhole.libduckeys.midi;

public interface MidiEventHandler {

    // void flush(); [替代：通过写入一个 null 表示 flush]

    void write(MidiEvent me);

}
