package com.github.xushifustudio.libduckeys.api.services;

import com.github.xushifustudio.libduckeys.api.Entity;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.settings.apps.DeviceInfo;

import java.util.List;

public final class MidiConnectionService {

    public final static String URI = "uri:///midi/connections";

    public final static String URI_CURRENT_STATE = "uri:///midi/connections/current/state";
    public final static String URI_CURRENT_CONTROL = "uri:///midi/connections/current/control";
    public final static String URI_CONNECT = "uri:///midi/connections/connect";
    public final static String URI_DISCONNECT = "uri:///midi/connections/disconnect";
    public final static String URI_HISTORY_LIST = "uri:///midi/connections/history/list";

    public static class Request {
        public DeviceInfo device;
    }

    public static class Response {
        public DeviceInfo current;
        public List<DeviceInfo> history;
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

    public static Request decode(Want w) {
        return Entity.decodeJSON(w.entity, Request.class);
    }

    public static Response decode(Have h) {
        return Entity.decodeJSON(h.entity, Response.class);
    }
}
