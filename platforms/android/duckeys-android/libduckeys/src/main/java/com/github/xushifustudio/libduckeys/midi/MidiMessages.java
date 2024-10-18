package com.github.xushifustudio.libduckeys.midi;

public final class MidiMessages {

    private MidiMessages() {
    }


    public static MidiNoteMessage parseNoteMessage(MidiEvent event) {
        return MidiNoteMessage.parse(event);
    }

    public static MidiControlChangeMessage parseControlChangeMessage(MidiEvent event) {
        return MidiControlChangeMessage.parse(event);
    }

    public static MidiTimingClockMessage parseTimingClockMessage(MidiEvent event) {
        return MidiTimingClockMessage.parse(event);
    }

    public static MidiSongPositionPointerMessage parseSongPositionPointerMessage(MidiEvent event) {
        return MidiSongPositionPointerMessage.parse(event);
    }

    public static MidiTransportControlMessage parseTransportControlMessage(MidiEvent event) {
        return MidiTransportControlMessage.parse(event);
    }


    public static Object parseAny(MidiEvent event) {

        final byte[] data = event.data;
        final int offset = event.offset;
        final int b0 = 0xff & data[offset];
        final int b0m = b0 & 0xf0;

        if (b0m == MidiNoteMessage.BYTE0_ON || b0m == MidiNoteMessage.BYTE0_OFF) {
            return parseNoteMessage(event);
        } else if (b0m == MidiControlChangeMessage.BYTE0) {
            return parseControlChangeMessage(event);
        }

        switch (b0) {
            case MidiTimingClockMessage.BYTE0:
                return parseTimingClockMessage(event);

            case MidiSongPositionPointerMessage.BYTE0:
                return parseSongPositionPointerMessage(event);

            case MidiTransportControlMessage.BYTE0_START:
            case MidiTransportControlMessage.BYTE0_CONTINUE:
            case MidiTransportControlMessage.BYTE0_STOP:
                return parseTransportControlMessage(event);

            default:
                break;
        }
        throw new RuntimeException("unsupported MIDI message: " + event);
    }
}
