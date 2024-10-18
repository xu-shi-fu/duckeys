package com.github.xushifustudio.libduckeys.ui.box2;

public class B2Box {

    public float x;
    public float y;
    public float z;

    public float width;
    public float height;

    public B2Box() {
    }

    public float top() {
        return Math.min(y, y + height);
    }

    public float left() {
        return Math.min(x, x + width);
    }

    public float right() {
        return Math.max(x, x + width);
    }

    public float bottom() {
        return Math.max(y, y + height);
    }

}
