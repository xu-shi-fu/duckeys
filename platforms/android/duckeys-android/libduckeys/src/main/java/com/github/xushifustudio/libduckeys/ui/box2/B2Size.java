package com.github.xushifustudio.libduckeys.ui.box2;

public final class B2Size {

    private B2Size() {
    }

    public static boolean isZero(float value) {
        return equal(value, 0);
    }

    public static boolean equal(float v1, float v2) {
        return Math.abs(v1 - v2) < 0.00001;
    }
}
