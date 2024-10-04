package com.github.xushifustudio.libduckeys.helper;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;

public final class JsonCodec {

    private JsonCodec() {
    }

    public static <T> T decode(byte[] data, int offset, int length, Class<? extends T> t) {
        String str = "{}";
        if (data != null) {
            str = new String(data, offset, length, StandardCharsets.UTF_8);
        }
        Gson gs = new Gson();
        return gs.fromJson(str, t);
    }

    public static <T> T decode(byte[] data, Class<? extends T> t) {
        if (data == null) {
            return decode(null, 0, 0, t);
        }
        return decode(data, 0, data.length, t);
    }

    public static byte[] encode(Object o) {
        String str = "{}";
        if (o != null) {
            Gson gs = new Gson();
            str = gs.toJson(o);
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }
}
