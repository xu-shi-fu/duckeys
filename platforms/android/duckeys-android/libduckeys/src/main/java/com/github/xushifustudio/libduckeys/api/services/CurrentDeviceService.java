package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Entity;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.settings.DeviceInfo;

public final class CurrentDeviceService {

    // use: MidiConnectionService

    /*
    public final static String URI = "uri:///midi/connections";

    public final static String URI_CURRENT_STATE = "uri:///midi/connections/current/state";
    public final static String URI_CURRENT_CONTROL = "uri:///midi/connections/current/control";
    public final static String URI_CURRENT_CONNECT = "uri:///midi/connections/current/connect";
    public final static String URI_CURRENT_DISCONNECT = "uri:///midi/connections/current/disconnect";


    public static class Request {
        public DeviceInfo device;
    }

    public static class Response {
        public DeviceInfo device;
    }

    public static class Holder {
        public Request request;
        public Response response;
    }

    public static Want encode(Request req) {
        Want w = new Want();
        w.entity = Entity.encodeJSON(req);
        return w;
    }

    public static Have encode(Response resp) {
        Have h = new Have();
        h.entity = Entity.encodeJSON(resp);
        return h;
    }

    public static Request decode(Want req) {
        return Entity.decodeJSON(req.entity, Request.class);
    }

    public static Response decode(Have resp) {
        return Entity.decodeJSON(resp.entity, Response.class);
    }
    */
}
