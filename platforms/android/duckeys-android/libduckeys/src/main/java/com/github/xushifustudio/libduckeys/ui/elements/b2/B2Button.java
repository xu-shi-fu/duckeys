package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;

import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;

public class B2Button extends B2TextView {

    public B2Button() {
    }

    private Paint getBgPaint() {
        B2Style st = getStyle(true);
        Paint p = new Paint();
        p.setColor(st.backgroundColor);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        return p;
    }

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);
        // draw background

        ICanvas can = self.getLocalCanvas();
        Paint paint = getBgPaint();

        final float r = 3;
        final float zero = 0;

        float left = zero + r;
        float top = zero + r;
        float right = this.width - r;
        float bottom = this.height - r;

        can.drawRoundRect(left, top, right, bottom, r, r, paint);
    }

    @Override
    protected void onPaintAfter(B2RenderThis self) {
        super.onPaintAfter(self);
        // draw foreground


    }
}
