package com.github.xushifustudio.libduckeys.helper;

import java.io.Closeable;

public final class IO {

    private IO() {
    }

    public static void close(Closeable cl) {
        if (cl == null) return;
        try {
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
