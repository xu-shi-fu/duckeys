package com.bitwormhole.libduckeys.ui.boxes;

public class Box {

    public int x;
    public int y;
    public int z;  // z 轴，数值越大离观众越近
    public int width;
    public int height;
    public int weight;


    public int top() {
        return y;
    }

    public int left() {
        return x;
    }

    public int right() {
        return (x + width);
    }

    public int bottom() {
        return (y + height);
    }

}
