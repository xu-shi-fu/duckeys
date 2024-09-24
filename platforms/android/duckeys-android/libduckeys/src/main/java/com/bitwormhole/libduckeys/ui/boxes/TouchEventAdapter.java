package com.bitwormhole.libduckeys.ui.boxes;

public class TouchEventAdapter {

    public int depth;
    public float x; // 相对 target 的 x
    public float y; // 相对 target 的 y
    public Node target;
    public TouchContext context;
    public TouchEventAdapter parent;
    public TouchPoint point;

    // init
    public TouchEventAdapter() {
    }

    // copy
    public TouchEventAdapter(TouchEventAdapter src) {
        this.target = src.target;
        this.x = src.x;
        this.y = src.y;
        this.parent = src.parent;
        this.context = src.context;
        this.depth = src.depth;
        this.point = src.point;
    }

    // make child
    public TouchEventAdapter(Node aTarget, float xInNode, float yInNode, TouchEventAdapter aParent) {
        this.target = aTarget;
        this.x = xInNode;
        this.y = yInNode;
        this.parent = aParent;
        this.context = aParent.context;
        this.depth = aParent.depth + 1;
        this.point = aParent.point;
    }
}
