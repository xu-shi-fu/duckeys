package com.bitwormhole.libduckeys.ui.boxes;

public class TouchEventAdapter {

    public int depth;
    public float offsetX;
    public float offsetY;
    public Node target;
    public TouchContext context;
    public TouchEventAdapter parent;

    // init
    public TouchEventAdapter() {
    }

    // copy
    public TouchEventAdapter(TouchEventAdapter src) {
        this.target = src.target;
        this.offsetX = src.offsetX;
        this.offsetY = src.offsetY;
        this.parent = src.parent;
        this.context = src.context;
        this.depth = src.depth;
    }

    // make child
    public TouchEventAdapter(Node aTarget, float offx, float offy, TouchEventAdapter aParent) {
        this.target = aTarget;
        this.offsetX = offx;
        this.offsetY = offy;
        this.parent = aParent;
        this.context = aParent.context;
        this.depth = aParent.depth + 1;
    }


    public float getX() {
        float x = context.event.getX();
        return x + offsetX;
    }

    public float getX(int indexPtr) {
        float x = context.event.getX(indexPtr);
        return x + offsetX;
    }

    public float getY() {
        float y = context.event.getY();
        return y + offsetY;
    }

    public float getY(int indexPtr) {
        float y = context.event.getY(indexPtr);
        return y + offsetY;
    }

}
