package com.github.xushifustudio.libduckeys.midi;

public class MidiControlChangeMessage extends MidiMessage {

    public static final int BYTE0 = 0xb0;

    public byte channel; // [0,15]
    public byte cc;
    public byte value;

    public MidiControlChangeMessage() {
    }


    public static MidiControlChangeMessage parse(MidiEvent event) {
        //     int len = event.count;
        int off = event.offset;
        byte[] data = event.data;
        byte b0 = data[off];
        byte b1 = data[off + 1];
        byte b2 = data[off + 2];
        int first = 0xf0 & b0;
        if (first != BYTE0) {
            throw new RuntimeException("bad MIDI CC message: " + event);
        }
        MidiControlChangeMessage msg = new MidiControlChangeMessage();
        msg.channel = (byte) (b0 & 0x0f);
        msg.cc = (byte) (b1 & 0x7f);
        msg.value = (byte) (b2 & 0x7f);
        return msg;
    }


    public static MidiEvent toMidiEvent(MidiControlChangeMessage src) {

        byte[] data = new byte[3];
        data[0] = (byte) (BYTE0 | (src.channel & 0x0f));
        data[1] = src.cc;
        data[2] = src.value;

        MidiEvent me = new MidiEvent();
        me.data = data;
        me.offset = 0;
        me.count = data.length;
        return me;
    }
}
