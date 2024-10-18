package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;
import android.graphics.Rect;

import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;

public class B2TextView extends B2View {

    private String text;

    public B2TextView() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private Paint getPaintForText() {
        B2Style st = this.getStyle(true);
        Paint p = new Paint();
        p.setTextSize(st.fontSize);
        p.setColor(st.textColor);
        return p;
    }


    @Override
    protected void onLayoutChildren(B2LayoutThis self) {
        super.onLayoutChildren(self);
        String str = this.text;
        if (str != null) {
            Paint p = this.getPaintForText();
            Rect rect = new Rect();
            p.getTextBounds(str, 0, str.length(), rect);
            this.contentHeight = rect.height();
            this.contentWidth = rect.width();
        }
    }

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);
    }

    @Override
    protected void onPaintChildren(B2RenderThis self) {
        String str = this.text;
        if (str != null) {

            //        Canvas can = self.context.canvas;
            // B2CoordinateSystem coor = self.coordinates;
            // PointF global = coor.local2global(new PointF(x, y));
            // can.drawText(str, global.x, global.y, p);

            ICanvas iCan = self.getLocalCanvas();
            Paint p = this.getPaintForText();
            float x = 0;
            float y = this.contentHeight;
            iCan.drawText(str, x, y, p);
        }
        super.onPaintChildren(self);
    }
}
