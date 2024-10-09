package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Entity;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;

public final class MidiWriterService {

    public final static String URI = "uri://local/midi/writer";

    public static class Request {
        public MidiEvent event;
    }

    public static class Response {
    }


    public static Want encode(Request req) {
        Want w = new Want();
        MidiEvent me = req.event;
        if (me != null) {
            w.entity = Entity.encodeBytes(me.data, me.offset, me.count);
        }
        return w;
    }

    public static Have encode(Response resp) {
        return new Have();
    }


    public static Request decode(Want req) {
        Entity ent = req.entity;
        MidiEvent evt = new MidiEvent();
        Request dst = new Request();
        if (ent != null) {
            evt.data = ent.buffer;
            evt.offset = ent.offset;
            evt.count = ent.length;
        }
        dst.event = evt;
        return dst;
    }

    public static Response decode(Have resp) {
        return new Response();
    }
}
