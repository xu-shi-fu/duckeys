package com.github.xushifustudio.libduckeys.helper;

public class Sleeper {

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
