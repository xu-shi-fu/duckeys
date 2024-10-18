package com.github.xushifustudio.libduckeys.ui.box2;

import android.view.MotionEvent;

public final class B2OnTouchThis {


    public final B2OnTouchThis parent;

    public final B2OnTouchContext context;
    public final B2CoordinateSystem coordinates;
    public final int depth;
    public B2WalkingPhase phase;

    public B2OnTouchThis(B2OnTouchContext ctx, B2View v) {
        this.parent = null;
        this.context = ctx;
        this.coordinates = new B2CoordinateSystem(v);
        this.depth = 1;
    }

    public B2OnTouchThis(B2OnTouchThis aParent, B2View v) {
        this.parent = aParent;
        this.context = aParent.context;
        this.coordinates = aParent.coordinates.offset(v);
        this.depth = aParent.depth + 1;
    }

}
