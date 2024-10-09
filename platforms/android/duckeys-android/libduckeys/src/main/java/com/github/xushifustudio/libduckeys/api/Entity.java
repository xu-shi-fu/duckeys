package com.github.xushifustudio.libduckeys.api;

import com.github.xushifustudio.libduckeys.helper.JsonCodec;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Entity {

    public byte[] buffer;
    public int offset;
    public int length;
    public String type;


    public Entity() {
    }


    public static <T> T decodeJSON(Entity src, Class<T> clazz) {
        return JsonCodec.decode(src.buffer, src.offset, src.length, clazz);
    }

    public static Entity encodeJSON(Object o) {
        byte[] data = JsonCodec.encode(o);
        Entity e = new Entity();
        e.buffer = data;
        e.length = data.length;
        e.offset = 0;
        e.type = "application/json";
        return e;
    }


    public static String decodeString(Entity src) {
        if (src == null) {
            return "";
        }
        ByteBuffer bb = ByteBuffer.wrap(src.buffer, src.offset, src.length);
        CharBuffer cb = StandardCharsets.UTF_8.decode(bb);
        return cb.toString();
    }

    public static Entity encodeString(String str) {
        Entity e = new Entity();
        e.offset = 0;
        e.type = "text/plain";
        if (str == null) {
            e.buffer = new byte[0];
            e.length = 0;
        } else {
            ByteBuffer bb = StandardCharsets.UTF_8.encode(str);
            byte[] arr = bb.array();
            e.buffer = arr;
            e.length = arr.length;
        }
        return e;
    }

    public static byte[] decodeBytes(Entity src) {
        int from = src.offset;
        int to = src.offset + src.length;
        return Arrays.copyOfRange(src.buffer, from, to);
    }

    public static Entity encodeBytes(byte[] bin) {
        if (bin == null) {
            bin = new byte[0];
        }
        Entity e = new Entity();
        e.type = "application/octet-stream";
        e.length = bin.length;
        e.buffer = bin;
        e.offset = 0;
        return e;
    }

    public static Entity encodeBytes(byte[] buffer, int offset, int length) {
        if (buffer == null) {
            buffer = new byte[0];
            offset = 0;
            length = 0;
        }
        Entity e = new Entity();
        e.type = "application/octet-stream";
        e.length = length;
        e.buffer = buffer;
        e.offset = offset;
        return e;
    }
}
