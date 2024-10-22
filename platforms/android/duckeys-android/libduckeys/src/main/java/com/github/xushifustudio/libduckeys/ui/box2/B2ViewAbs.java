package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.RectF;
import android.util.SizeF;

public abstract class B2ViewAbs extends B2Box {


    public boolean enabled;
    public boolean exists;
    public boolean visible;
    public boolean pressed;
    public boolean selected;
    public boolean focused;
    public boolean interactive; // 表示该视图可以响应 touch 操作

    public int layoutWidth;  // 用于排版：[SIZE_AS_WEIGHT|SIZE_AS_PARENT|SIZE_AS_CONTENT|+NUM]
    public int layoutHeight; // 用于排版：[SIZE_AS_WEIGHT|SIZE_AS_PARENT|SIZE_AS_CONTENT|+NUM]
    public int contentWidth; // 用于排版：内容的宽度
    public int contentHeight;// 用于排版：内容的高度


    public B2ViewAbs() {
        this.enabled = true;
        this.visible = true;
        this.exists = true;
    }


    public abstract void computeContentSize(RectF size);


    protected abstract void onLayoutBefore(B2LayoutThis self);

    protected abstract void onLayoutChildren(B2LayoutThis self);

    protected abstract void onLayoutAfter(B2LayoutThis self);


    protected abstract void onPaintBefore(B2RenderThis self);

    protected abstract void onPaintChildren(B2RenderThis self);

    protected abstract void onPaintAfter(B2RenderThis self);


    protected abstract void onTouchBefore(B2OnTouchThis self);

    protected abstract void onTouchChildren(B2OnTouchThis self);

    protected abstract void onTouchAfter(B2OnTouchThis self);

}
