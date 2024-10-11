package com.github.xushifustudio.libduckeys.midi;

public class MidiCCEvent {

    public byte channel; // [0,15]

    public byte cc;
    public byte value;

    public MidiCCEvent() {
    }


    public static MidiEvent toMidiEvent(MidiCCEvent src) {

        final int cc_ = 0xb0;

        byte[] data = new byte[3];
        data[0] = (byte) (cc_ | (src.channel & 0x0f));
        data[1] = src.cc;
        data[2] = src.value;

        MidiEvent me = new MidiEvent();
        me.data = data;
        me.offset = 0;
        me.count = data.length;
        return me;
    }
}
