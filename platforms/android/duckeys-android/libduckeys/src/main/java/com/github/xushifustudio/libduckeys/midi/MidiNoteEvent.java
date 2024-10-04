package com.github.xushifustudio.libduckeys.midi;

public class MidiNoteEvent {

    public byte channel; // [0,15]
    public boolean on; // [on|off]
    public byte note; // [0,127]
    public byte velocity;// [0,127]

    public MidiNoteEvent() {
    }

    public static MidiEvent toMidiEvent(MidiNoteEvent src) {

        int chl = 0x0f & src.channel;
        int action = src.on ? 0x90 : 0x80;

        byte[] data = new byte[3];
        data[0] = (byte) (action | chl);
        data[1] = src.note;
        data[2] = src.velocity;

        MidiEvent me = new MidiEvent();
        me.data = data;
        me.offset = 0;
        me.count = data.length;
        me.timestamp = 0;
        return me;
    }

    public interface Listener {
        void onMidiNoteEvent(MidiNoteEvent e);
    }
}
