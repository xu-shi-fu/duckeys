package com.github.xushifustudio.libduckeys.midi;

public class MidiTimingClockMessage extends MidiMessage {

    public static final int BYTE0 = 0xf8;


    public static MidiTimingClockMessage parse(MidiEvent event) {

        MidiTimingClockMessage msg = new MidiTimingClockMessage();
        return msg;
    }

    public static MidiEvent toMidiEvent(MidiTimingClockMessage src) {

        MidiEvent dst = new MidiEvent();
        return dst;
    }

}
