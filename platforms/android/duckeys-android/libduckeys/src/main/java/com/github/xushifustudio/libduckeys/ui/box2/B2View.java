package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.RectF;
import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.instruments.pad2.SP2View;
import com.github.xushifustudio.libduckeys.ui.styles.B2StyleReader;
import com.github.xushifustudio.libduckeys.ui.styles.BaseStyle;

public class B2View extends B2ViewAbs implements B2RenderAble, B2LayoutAble, B2OnTouchListener {


    // 注意：内容大小不包含 (margin,border,padding 这些附加的尺寸)
    public float contentWidth; // 用于排版：内容的宽度
    public float contentHeight;// 用于排版：内容的高度


    // private fields

    private OnClickDispatcher mOnClickDispatcher;
    private B2OnTouchPointer.Binding mPointerBinding;
    private B2Style style;
    private B2Container parent;
    private OnClickListener onClickListener;

    public B2View() {
    }

    private OnClickDispatcher getOnClickDispatcher() {
        OnClickDispatcher disp = mOnClickDispatcher;
        if (disp == null) {
            disp = new OnClickDispatcher();
            mOnClickDispatcher = disp;
        }
        return disp;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
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

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        if (this.interactive) {
            this.checkPointerBinding();
        }
    }

    @Override
    protected void onPaintChildren(B2RenderThis self) {
    }

    @Override
    protected void onPaintAfter(B2RenderThis self) {
    }


    @Override
    protected void onTouchBefore(B2OnTouchThis self) {
        if (this.interactive) {
            switch (self.context.action) {
                case B2OnTouchContext.ACTION_DOWN:
                case B2OnTouchContext.ACTION_POINTER_DOWN:
                case B2OnTouchContext.ACTION_MOVE:
                    this.pressed = true;
                    this.bindWithPointer(self);
                    break;
                case B2OnTouchContext.ACTION_UP:
                case B2OnTouchContext.ACTION_POINTER_UP:
                    this.pressed = false;
                    this.mPointerBinding = null;
                    break;
                default:
                    break;
            }
        }
    }

    private void bindWithPointer(B2OnTouchThis self) {
        B2OnTouchPointer ptr = self.context.pointer;
        if (ptr == null) {
            return;
        }
        mPointerBinding = ptr.bind(mPointerBinding);
    }

    private void checkPointerBinding() {
        B2OnTouchPointer.Binding b2 = mPointerBinding;
        if (b2 != null) {
            if (!b2.isAlive()) {
                mPointerBinding = null;
                this.pressed = false;
            }
        }
    }

    @Override
    protected void onTouchChildren(B2OnTouchThis self) {
    }

    @Override
    protected void onTouchAfter(B2OnTouchThis self) {
        this.getOnClickDispatcher().handleOnTouch(self, this.onClickListener);
    }

    @Override
    protected void computeContentSize() {
        // NOP: 由派生类实现
    }

    @Override
    protected void computeBoxSize() {

        B2LayoutParams lp = this.getLayoutParams();
        B2View my_parent = this.parent;
        if (my_parent == null) {
            my_parent = new SP2View();
        }

        if (lp.layoutHeight > 0) {
            this.height = lp.layoutHeight;
        } else if (lp.layoutHeight == B2LayoutParams.SIZE_AS_PARENT) {
            this.height = my_parent.height;
        } else if (lp.layoutHeight == B2LayoutParams.SIZE_AS_CONTENT) {
            this.height = this.contentHeight;
        }

        if (lp.layoutWidth > 0) {
            this.width = lp.layoutWidth;
        } else if (lp.layoutWidth == B2LayoutParams.SIZE_AS_PARENT) {
            this.width = my_parent.width;
        } else if (lp.layoutWidth == B2LayoutParams.SIZE_AS_CONTENT) {
            this.width = this.contentWidth;
        }
    }

    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        // 第一次调用 computeBoxSize，为 children 提供 parent 的大小
        this.computeBoxSize();
    }

    @Override
    protected void onLayoutChildren(B2LayoutThis self) {
    }

    @Override
    protected void onLayoutAfter(B2LayoutThis self) {
        // 第二次调用 computeBoxSize，根据 content 计算 box 的大小
        this.computeBoxSize();
    }


    @Override
    public B2LayoutParams getLayoutParams() {
        B2Style sty = this.getStyle(true);
        B2LayoutParams older = super.getLayoutParams();
        MyLayoutParams lp = null;
        if (older instanceof MyLayoutParams) {
            lp = (MyLayoutParams) older;
            if (lp.rev == sty.revision()) {
                return lp;
            }
        }
        lp = loadLayoutParams(sty);
        super.setLayoutParams(lp);
        return lp;
    }

    private MyLayoutParams loadLayoutParams(B2Style sty) {

        B2StyleReader r = new B2StyleReader(sty);
        RectF border = new RectF();
        RectF margin = new RectF();
        RectF padding = new RectF();

        border.top = r.readSize(new String[]{B2Style.border_top_width, B2Style.border_top, B2Style.border_width, B2Style.border});
        border.left = r.readSize(new String[]{B2Style.border_left_width, B2Style.border_left, B2Style.border_width, B2Style.border});
        border.right = r.readSize(new String[]{B2Style.border_right_width, B2Style.border_right, B2Style.border_width, B2Style.border});
        border.bottom = r.readSize(new String[]{B2Style.border_bottom_width, B2Style.border_bottom, B2Style.border_width, B2Style.border});

        margin.top = r.readSize(new String[]{B2Style.margin_top, B2Style.margin});
        margin.left = r.readSize(new String[]{B2Style.margin_left, B2Style.margin});
        margin.right = r.readSize(new String[]{B2Style.margin_right, B2Style.margin});
        margin.bottom = r.readSize(new String[]{B2Style.margin_bottom, B2Style.margin});

        padding.top = r.readSize(new String[]{B2Style.padding_top, B2Style.padding});
        padding.left = r.readSize(new String[]{B2Style.padding_left, B2Style.padding});
        padding.right = r.readSize(new String[]{B2Style.padding_right, B2Style.padding});
        padding.bottom = r.readSize(new String[]{B2Style.padding_bottom, B2Style.padding});

        MyLayoutParams lp = new MyLayoutParams();
        lp.rev = sty.revision();
        lp.layoutWeight = r.readInt(B2Style.layout_weight);
        lp.layoutWidth = r.readInt(B2Style.layout_width);
        lp.layoutHeight = r.readInt(B2Style.layout_height);

        lp.padding.set(padding);
        lp.margin.set(margin);
        lp.border.set(border);

        return lp;
    }

    private static class MyLayoutParams extends B2LayoutParams {
        long rev;
    }

    public interface OnClickListener {
        void onClick(B2View view);
    }

    private class OnClickDispatcher {

        private B2OnTouchPointer ptr;

        void handleOnTouch(B2OnTouchThis self, OnClickListener l) {
            if (self == null || l == null) {
                return;
            }
            switch (self.context.action) {
                case B2OnTouchContext.ACTION_DOWN:
                    this.handleActionDown(self, l);
                    break;
                case B2OnTouchContext.ACTION_UP:
                    this.handleActionUp(self, l);
                    break;
                default:
                    break;
            }
        }

        private void handleActionDown(B2OnTouchThis self, OnClickListener l) {
            this.ptr = self.context.pointer;
        }

        private void handleActionUp(B2OnTouchThis self, OnClickListener l) {
            B2OnTouchPointer p1 = this.ptr;
            B2OnTouchPointer p2 = self.context.pointer;
            if (p1 == null || p2 == null) {
                return;
            }
            if (!p1.equals(p2)) {
                return;
            }
            this.ptr = null;
            l.onClick(B2View.this);
        }
    }

    @Override
    public B2State getState() {
        B2State st = super.getState();
        if (st == B2State.AUTO) {
            if (!this.enabled) {
                return B2State.DISABLED;
            } else if (this.pressed) {
                return B2State.PRESSED;
            } else if (this.focused) {
                return B2State.FOCUSED;
            } else if (this.selected) {
                return B2State.SELECTED;
            }
            return B2State.NORMAL;
        }
        return st;
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
