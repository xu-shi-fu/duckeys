package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.Canvas;

public final class B2RenderThis {

    public final B2RenderThis parent;
    public final B2View view;
    public final B2RenderContext context;
    public final B2CoordinateSystem coordinates;
    public final int depth;
    public B2WalkingPhase phase;

    public B2RenderThis(B2RenderContext ctx, B2View v) {
        this.view = v;
        this.parent = null;
        this.depth = 1;
        this.context = ctx;
        this.coordinates = new B2CoordinateSystem(v);
    }

    public B2RenderThis(B2RenderThis aParent, B2View v) {
        this.view = v;
        this.parent = aParent;
        this.depth = aParent.depth + 1;
        this.context = aParent.context;
        this.coordinates = aParent.coordinates.offset(v);
    }

    public ICanvas getLocalCanvas() {
        return new LocalCanvas(context.canvas, coordinates);
    }
}
