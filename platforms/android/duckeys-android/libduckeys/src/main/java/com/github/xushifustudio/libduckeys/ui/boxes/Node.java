package com.github.xushifustudio.libduckeys.ui.boxes;

public class Node extends Box implements RenderAble, LayoutAble, TouchAble {

    private Node parent;

    public boolean enabled;
    public boolean visible;
    public boolean existed;

    public Node() {
        enabled = true;
        existed = true;
        visible = true;
    }

    @Override
    public void render(RenderingContext rc, RenderingItem item) {
    }

    @Override
    public void updateLayout(LayoutContext lc) {
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isChildOf(Node parent) {
        return this.parent == parent;
    }

    @Override
    public void onTouch(TouchContext ctx, TouchEventAdapter ada) {
    }
}
