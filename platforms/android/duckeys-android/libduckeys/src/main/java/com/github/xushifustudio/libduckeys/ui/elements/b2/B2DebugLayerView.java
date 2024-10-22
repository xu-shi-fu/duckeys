package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchPointer;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.box2.FPSCounter;
import com.github.xushifustudio.libduckeys.ui.layouts.B2SimpleLayout;

import java.util.Map;

public class B2DebugLayerView extends B2Container {

    private boolean displayFPS;
    private boolean displayTouchAtGrid;

    private final FPSCounter fps;
    private B2OnTouchThis currentOnTouchThis;
    private final int[] mTouchPointerColors;

    public B2DebugLayerView() {
        B2Layout la = B2SimpleLayout.getInstance();
        this.setLayout(la);
        this.fps = new FPSCounter();
        this.mTouchPointerColors = initTouchPointerColors();
    }

    public static B2DebugLayerView wrap(B2View child) {
        B2DebugLayerView parent = new B2DebugLayerView();
        parent.add(child);
        return parent;
    }


    @Override
    protected void onTouchChildren(B2OnTouchThis self) {
        super.onTouchChildren(self);
        this.currentOnTouchThis = self;
    }


    @Override
    protected void onPaintAfter(B2RenderThis self) {
        super.onPaintAfter(self);
        fps.addFrame();
        Canvas can = self.context.canvas;

        if (this.displayTouchAtGrid) {
            this.drawOnTouchGrid(can, this.currentOnTouchThis);
        }

        if (this.displayFPS) {
            fps.draw(can);
        }
    }

    private int[] initTouchPointerColors() {
        final int orange = Color.rgb(255, 165, 0);
        final int cyan = Color.CYAN;
        final int purple = Color.rgb(147, 112, 219);
        int[] all = {Color.RED, orange, Color.YELLOW, Color.GREEN, cyan, Color.BLUE, purple};
        return all;
    }

    private int getTouchPointerColorById(int id) {
        int[] all = mTouchPointerColors;
        if (0 <= id && id < all.length) {
            return all[id];
        }
        return Color.WHITE;
    }


    private void drawOnTouchGrid(Canvas can, B2OnTouchThis touch) {
        if (can == null || touch == null) {
            return;
        }

        B2OnTouchContext ctx = touch.context;
        Map<Integer, B2OnTouchPointer> all = ctx.pointers;
        final float w = this.width;
        final float h = this.height;

        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setTextSize(36);

        all.values().forEach((ptr) -> {
            float x1, y1, x2, y2;
            String strX = "x:" + (int) ptr.globalX;
            String strY = "y:" + (int) ptr.globalY;
            float str_x_w = paint.measureText(strX);
            float str_x_offset = (ptr.globalX < (w / 2)) ? 0 : (0 - str_x_w - 5);
            int color = getTouchPointerColorById(ptr.id);

            if (ptr.stopped) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(color);
            }

            // ----
            x1 = 0;
            y1 = ptr.globalY;
            x2 = w;
            y2 = ptr.globalY;
            can.drawLine(x1, y1, x2, y2, paint);
            can.drawText(strY, 0, y1, paint);

            // |
            x1 = ptr.globalX;
            y1 = 0;
            x2 = ptr.globalX;
            y2 = h;
            can.drawLine(x1, y1, x2, y2, paint);
            can.drawText(strX, x1 + str_x_offset, h, paint);
        });
    }


    public boolean isDisplayFPS() {
        return displayFPS;
    }

    public void setDisplayFPS(boolean displayFPS) {
        this.displayFPS = displayFPS;
    }

    public boolean isDisplayTouchAtGrid() {
        return displayTouchAtGrid;
    }

    public void setDisplayTouchAtGrid(boolean displayTouchAtGrid) {
        this.displayTouchAtGrid = displayTouchAtGrid;
    }
}
