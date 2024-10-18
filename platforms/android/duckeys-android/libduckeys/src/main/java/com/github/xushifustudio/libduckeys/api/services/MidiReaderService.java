package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Entity;
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
        Want dst = new Want();
        dst.entity = Entity.encodeJSON(req);
        return dst;
    }

    public static Have encode(Response resp) {
        Have dst = new Have();
        dst.entity = Entity.encodeJSON(resp);
        return dst;
    }


    public static Request decode(Want req) {
        return Entity.decodeJSON(req.entity, Request.class);
    }

    public static Response decode(Have resp) {
        return Entity.decodeJSON(resp.entity, Response.class);
    }
}
