package com.github.xushifustudio.libduckeys.midi;

public class MidiTransportControlMessage extends MidiMessage {

    public static final int BYTE0_START = 0xfa;
    public static final int BYTE0_CONTINUE = 0xfb;
    public static final int BYTE0_STOP = 0xfc;


    public int action; // [start|continue|stop]


    public static MidiTransportControlMessage parse(MidiEvent event) {
        byte[] data = event.data;
        MidiTransportControlMessage msg = new MidiTransportControlMessage();
        msg.timestamp = event.timestamp;
        msg.action = data[0];
        msg.checkAction();
        return msg;
    }


    public static MidiEvent toMidiEvent(MidiTransportControlMessage src) {

        src.checkAction();
        int action = src.action;
        byte[] data = {(byte) action};

        MidiEvent dst = new MidiEvent();
        dst.timestamp = src.timestamp;
        dst.data = data;
        dst.count = data.length;
        dst.offset = 0;

        return dst;
    }


    private void checkAction() {
        switch (action) {
            case BYTE0_START:
            case BYTE0_CONTINUE:
            case BYTE0_STOP:
                return; // ok
            default:
                break;
        }
        throw new RuntimeException("bad MidiTransportControlMessage.action: " + action);
    }
}
