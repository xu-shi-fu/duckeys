package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;

public final class MidiWriterService {

    public final static String URI = "uri://local/midi/writer";

    public static class Request {
    }

    public static class Response {
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
