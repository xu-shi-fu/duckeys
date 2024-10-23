package com.github.xushifustudio.libduckeys.ui.box2;

public final class B2LayoutThis {

    public final B2LayoutThis parent;
    public final B2View view;

    public final B2LayoutContext context;
    public final B2CoordinateSystem coordinates;
    public final int depth;
    public B2WalkingPhase phase;

    public B2LayoutThis(B2LayoutThis aParent, B2View v) {
        this.parent = aParent;
        this.context = aParent.context;
        this.coordinates = aParent.coordinates.offset(v);
        this.depth = aParent.depth + 1;
        this.view = v;
    }

    public B2LayoutThis(B2LayoutContext ctx, B2View v) {
        this.parent = null;
        this.context = ctx;
        this.coordinates = new B2CoordinateSystem(v);
        this.depth = 1;
        this.view = v;
    }

}
