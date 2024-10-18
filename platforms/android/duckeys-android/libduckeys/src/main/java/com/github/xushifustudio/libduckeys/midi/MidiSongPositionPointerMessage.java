package com.github.xushifustudio.libduckeys.midi;

public class MidiSongPositionPointerMessage extends MidiMessage {

    public static final int BYTE0 = 0xf2;


    public static MidiSongPositionPointerMessage parse(MidiEvent event) {

        MidiSongPositionPointerMessage msg = new MidiSongPositionPointerMessage();
        return msg;
    }

    public static MidiEvent toMidiEvent(MidiSongPositionPointerMessage src) {

        MidiEvent dst = new MidiEvent();

        return dst;
    }
}
