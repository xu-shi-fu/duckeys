package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class B2Container extends B2View {

    private final B2Children children;
    private B2Layout layout;

    public B2Container() {
        this.children = new B2Children(this);
    }

    public void add(B2View child) {
        children.add(child);
    }

    public void add(B2View child, int id) {
        children.add(child, id);
    }

    public void remove(B2View child) {
        children.remove(child);
    }


    @Override
    public void computeContentSize(RectF size) {
        super.computeContentSize(size);
        B2Layout la = this.layout;
        if (la != null) {
            la.computeContentSize(this, size);
        }
    }


    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        final B2Layout l = this.layout;
        if (l != null) {
            self.phase = B2WalkingPhase.BEFORE;
            l.apply(this, self);
        }
    }

    @Override
    protected void onLayoutChildren(final B2LayoutThis th1) {
        super.onLayoutChildren(th1);
        this.children.forItems((child, holder) -> {
            B2LayoutThis th2 = new B2LayoutThis(th1, child);
            child.updateLayout(th2);
            return true;
        });
    }

    @Override
    protected void onLayoutAfter(B2LayoutThis self) {
        super.onLayoutAfter(self);
        final B2Layout l = this.layout;
        if (l != null) {
            self.phase = B2WalkingPhase.AFTER;
            l.apply(this, self);
        }
    }

    @Override
    protected void onPaintChildren(B2RenderThis th1) {
        super.onPaintChildren(th1);
        this.children.forItems((child, holder) -> {
            B2RenderThis th2 = new B2RenderThis(th1, child);
            child.paint(th2);
            return true;
        });
    }

    @Override
    protected void onTouchChildren(B2OnTouchThis th1) {
        super.onTouchChildren(th1);
        final B2OnTouchContext ctx = th1.context;
        this.children.forItemsReverse((child, holder) -> {
            if (isHitChild(th1, child)) {
                B2OnTouchThis th2 = new B2OnTouchThis(th1, child);
                child.onTouch(th2);
            }
            return !ctx.brake;
        });
    }

    private boolean isHitChild(B2OnTouchThis th1, B2View child) {
        B2OnTouchPointer ptr = th1.context.pointer;
        if (ptr == null) {
            return false;
        }
        PointF local = th1.coordinates.global2local(new PointF(ptr.globalX, ptr.globalY));
        float t = child.top();
        float l = child.left();
        float r = child.right();
        float b = child.bottom();
        return (l <= local.x) && (local.x <= r) && (t <= local.y) && (local.y <= b);
    }

    public List<B2View> listChildren() {
        final List<B2View> dst = new ArrayList<>();
        this.children.forItems((child, holder) -> {
            dst.add(child);
            return true;
        });
        return dst;
    }

    public B2Layout getLayout() {
        return layout;
    }

    public void setLayout(B2Layout layout) {
        this.layout = layout;
    }
}
