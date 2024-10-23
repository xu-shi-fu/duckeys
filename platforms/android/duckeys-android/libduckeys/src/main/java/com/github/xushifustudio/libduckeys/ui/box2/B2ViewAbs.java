package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.RectF;
import android.util.SizeF;

public abstract class B2ViewAbs extends B2Box {

    // states

    public boolean enabled;
    public boolean exists;
    public boolean visible;
    public boolean pressed;
    public boolean selected;
    public boolean focused;
    public boolean interactive; // 表示该视图可以响应 touch 操作

    // layout

    private B2LayoutParams layoutParams;

    public B2ViewAbs() {
        this.enabled = true;
        this.exists = true;
        this.focused = false;
        this.interactive = false;
        this.pressed = false;
        this.selected = false;
        this.visible = true;
    }

    // abstract methods

    protected abstract void onLayoutBefore(B2LayoutThis self);

    protected abstract void onLayoutChildren(B2LayoutThis self);

    protected abstract void onLayoutAfter(B2LayoutThis self);


    protected abstract void onPaintBefore(B2RenderThis self);

    protected abstract void onPaintChildren(B2RenderThis self);

    protected abstract void onPaintAfter(B2RenderThis self);


    protected abstract void onTouchBefore(B2OnTouchThis self);

    protected abstract void onTouchChildren(B2OnTouchThis self);

    protected abstract void onTouchAfter(B2OnTouchThis self);

    protected abstract void computeContentSize();

    protected abstract void computeBoxSize();

    // getters & setters


    public B2LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(B2LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }
}
