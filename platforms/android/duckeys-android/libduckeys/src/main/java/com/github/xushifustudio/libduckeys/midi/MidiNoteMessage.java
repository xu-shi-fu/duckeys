package com.github.xushifustudio.libduckeys.midi;

public class MidiNoteMessage extends MidiMessage {

    public static final int BYTE0_OFF = 0x80;
    public static final int BYTE0_ON = 0x90;


    public byte channel; // [0,15]
    public boolean on; // [on|off]
    public byte note; // [0,127]
    public byte velocity;// [0,127]

    public MidiNoteMessage() {
    }


    public static MidiNoteMessage parse(MidiEvent event) {
        int len = event.count;
        int off = event.offset;
        byte[] data = event.data;
        byte b0 = data[off];
        byte b1 = data[off + 1];
        byte b2 = 0;
        MidiNoteMessage msg = new MidiNoteMessage();
        msg.channel = (byte) (b0 & 0x0f);
        msg.note = (byte) (b1 & 0x7f);
        msg.timestamp = event.timestamp;
        switch (b0 & 0xf0) {
            case BYTE0_ON:
                msg.on = true;
                msg.velocity = 64;
                break;
            case BYTE0_OFF:
                msg.on = false;
                msg.velocity = 0;
                break;
            default:
                throw new RuntimeException("bad midi note event: " + event);
        }
        if ((msg.on) && (len >= 3)) {
            b2 = data[off + 2];
            msg.velocity = (byte) (b2 & 0x7f);
        }
        return msg;
    }


    public static MidiEvent toMidiEvent(MidiNoteMessage src) {

        int chl = 0x0f & src.channel;
        int action = src.on ? BYTE0_ON : BYTE0_OFF;

        byte[] data = new byte[3];
        data[0] = (byte) (action | chl);
        data[1] = src.note;
        data[2] = src.velocity;

        MidiEvent me = new MidiEvent();
        me.data = data;
        me.offset = 0;
        me.count = data.length;
        me.timestamp = src.timestamp;
        return me;
    }

    public interface Listener {
        void onMidiNoteEvent(MidiNoteMessage e);
    }
}
