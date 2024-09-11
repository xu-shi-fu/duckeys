package com.bitwormhole.libduckeys.ui.boxes;

public class Box {

    public int x;
    public int y;
    public int z;  // z 轴，数值越大离观众越近
    public int width;
    public int height;
    public int weight;

    public Box() {
    }

    public Box(Box src) {
        x = src.x;
        y = src.y;
        z = src.z;
        weight = src.weight;
        width = src.width;
        height = src.height;
    }


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

    public boolean containPoint(int px, int py) {
        if (px < x) return false;
        if (py < y) return false;
        if (px > right()) return false;
        if (py > bottom()) return false;
        return true;
    }
}
