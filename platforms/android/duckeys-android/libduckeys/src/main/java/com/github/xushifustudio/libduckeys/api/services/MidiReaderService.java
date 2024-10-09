package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;

public final class MidiReaderService {

    public final static String URI = "uri://local/midi/reader";

    public static class Request {
        public int timeout; // in milliseconds
    }

    public static class Response {
        public MidiEvent event;
    }


    public static Want encode(Request req) {
        return new Want();
    }

    public static Have encode(Response resp) {
        return new Have();
    }


    public static Request decode(Want req) {
        return new Request();
    }

    public static Response decode(Have resp) {
        return new Response();
    }
}
