package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class FPSCounter {

    private long timestamp1;
    private long timestamp2;
    private long countFrame1;
    private long countFrame2;
    private int currentFps;
    private boolean withBackground;

    public FPSCounter() {
        this.withBackground = true;
    }

    private boolean isTimeToUpdate() {
        return Math.abs(timestamp1 - timestamp2) > 999;
    }

    private int computeFPS() {
        long dt = timestamp2 - timestamp1;
        long df = countFrame2 - countFrame1;
        if (dt < 1 || df < 1) {
            return 0;
        }
        return (int) (df * 1000 / dt);
    }

    public void addFrame() {
        timestamp2 = System.currentTimeMillis();
        countFrame2++;
        if (isTimeToUpdate()) {
            currentFps = computeFPS();
            timestamp1 = timestamp2;
            countFrame1 = countFrame2;
        }
    }

    public void draw(Canvas can) {
        int fgColor = Color.RED;
        int bgColor = Color.rgb(30, 30, 30);
        int w = 200;
        int h = 50;
        int padding = 5;
        String text = "fps:" + this.currentFps;
        Paint p = new Paint();

        if (withBackground) {
            p.setColor(bgColor);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            can.drawRect(0, 0, w, h, p);
        }

        p.setStyle(Paint.Style.FILL);
        p.setColor(fgColor);
        p.setTextSize(h - (padding * 3));
        can.drawText(text, padding, h - (padding * 2), p);
    }
}
