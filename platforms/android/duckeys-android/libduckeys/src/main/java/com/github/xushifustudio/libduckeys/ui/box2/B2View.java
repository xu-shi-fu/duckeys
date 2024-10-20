package com.github.xushifustudio.libduckeys.ui.box2;

import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.ui.styles.BaseStyle;
import com.github.xushifustudio.libduckeys.ui.styles.SimpleStyle;

public class B2View extends B2Box implements B2RenderAble, B2LayoutAble, B2OnTouchListener {


    public static final int SIZE_AS_WEIGHT = 0;
    public static final int SIZE_AS_PARENT = -1;
    public static final int SIZE_AS_CONTENT = -2;


    public boolean enabled;
    public boolean exists;
    public boolean visible;
    public boolean pressed;
    public boolean selected;
    public boolean focused;
    public boolean interactive; // 表示该视图可以响应 touch 操作
    private B2Style style;

    public int layoutWidth;  // 用于排版：[SIZE_AS_WEIGHT|SIZE_AS_PARENT|SIZE_AS_CONTENT|+NUM]
    public int layoutHeight; // 用于排版：[SIZE_AS_WEIGHT|SIZE_AS_PARENT|SIZE_AS_CONTENT|+NUM]
    public int contentWidth; // 用于排版：内容的宽度
    public int contentHeight;// 用于排版：内容的高度

    private B2Container parent;

    public B2View() {
        this.enabled = true;
        this.visible = true;
        this.exists = true;
    }

    public B2Container getParent() {
        return parent;
    }

    public void setParent(B2Container pNew, boolean doAddRemove) {
        B2Container pOld = this.parent;
        this.parent = pNew;
        if (!doAddRemove) {
            return;
        }
        if (pOld != null) {
            pOld.remove(this);
        }
        if (pNew != null) {
            pNew.add(this);
        }
    }

    public void setParent(B2Container pNew) {
        setParent(pNew, true);
    }


    @Override
    public final void updateLayout(B2LayoutThis self) {

        if (!this.exists) {
            return;
        }
        if (self.context.depthLimit < self.depth) {
            Log.w(DuckLogger.TAG, "B2Container: the view tree is too deep, depth=" + self.depth);
            return;
        }

        // do layout //////////

        self.phase = B2WalkingPhase.BEFORE;
        this.onLayoutBefore(self);

        self.phase = B2WalkingPhase.INTO;
        this.onLayoutChildren(self);

        self.phase = B2WalkingPhase.AFTER;
        this.onLayoutAfter(self);
    }

    @Override
    public final void onTouch(B2OnTouchThis self) {

        if (!exists || !visible || !enabled) {
            return;
        }
        if (self.context.brake) {
            return;
        }
        if (self.context.depthLimit < self.depth) {
            Log.w(DuckLogger.TAG, "B2Container: the view tree is too deep, depth=" + self.depth);
            return;
        }

        // do touch //////////

        self.phase = B2WalkingPhase.BEFORE;
        this.onTouchBefore(self);

        self.phase = B2WalkingPhase.INTO;
        this.onTouchChildren(self);

        self.phase = B2WalkingPhase.AFTER;
        this.onTouchAfter(self);
    }

    @Override
    public final void paint(B2RenderThis self) {

        if (!exists || !visible) {
            return;
        }

        if (self.context.depthLimit < self.depth) {
            Log.w(DuckLogger.TAG, "B2Container: the view tree is too deep, depth=" + self.depth);
            return;
        }

        // do paint //////////

        self.phase = B2WalkingPhase.BEFORE;
        this.onPaintBefore(self);

        self.phase = B2WalkingPhase.INTO;
        this.onPaintChildren(self);

        self.phase = B2WalkingPhase.AFTER;
        this.onPaintAfter(self);
    }

    protected void onPaintBefore(B2RenderThis self) {
    }

    protected void onPaintChildren(B2RenderThis self) {
    }

    protected void onPaintAfter(B2RenderThis self) {
    }


    protected void onTouchBefore(B2OnTouchThis self) {
        if (this.interactive) {
            switch (self.context.action) {
                case B2OnTouchContext.ACTION_DOWN:
                case B2OnTouchContext.ACTION_POINTER_DOWN:
                case B2OnTouchContext.ACTION_MOVE:
                    this.pressed = true;
                    break;
                case B2OnTouchContext.ACTION_UP:
                case B2OnTouchContext.ACTION_POINTER_UP:
                    this.pressed = false;
                    break;
                default:
                    break;
            }
        }
    }


    protected void onTouchChildren(B2OnTouchThis self) {
    }

    protected void onTouchAfter(B2OnTouchThis self) {
    }

    protected void onLayoutBefore(B2LayoutThis self) {
    }

    protected void onLayoutChildren(B2LayoutThis self) {
    }

    protected void onLayoutAfter(B2LayoutThis self) {
    }

    public B2Style getStyle() {
        return style;
    }

    public B2Style getStyle(boolean create) {
        B2Style st = this.style;
        if (st == null && create) {
            st = new BaseStyle();
            this.style = st;
        }
        return st;
    }

    public void setStyle(B2Style style) {
        this.style = style;
    }
}
