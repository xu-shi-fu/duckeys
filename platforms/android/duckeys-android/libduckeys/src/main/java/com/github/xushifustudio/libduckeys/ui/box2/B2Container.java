package com.github.xushifustudio.libduckeys.ui.box2;

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

    public void remove(B2View child) {
        children.remove(child);
    }


    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        final B2Layout l = this.layout;
        if (l != null) {
            self.phase = B2WalkingPhase.BEFORE;
            l.apply(self, this);
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
            l.apply(self, this);
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
            B2OnTouchThis th2 = new B2OnTouchThis(th1, child);
            child.onTouch(th2);
            return !ctx.done;
        });
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
