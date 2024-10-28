package com.github.xushifustudio.libduckeys.helper;

public final class Strings {

    private Strings() {
    }

    public static boolean equal(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

}
