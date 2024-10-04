package com.github.xushifustudio.libduckeys.ui.elements.piano;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.github.xushifustudio.libduckeys.ui.boxes.RenderingContext;
import com.github.xushifustudio.libduckeys.ui.boxes.RenderingItem;

public final class PianoKeyLEDRenderer {

    public static void renderLEDBar(RenderingContext rc, RenderingItem item1, PianoKeyLEDBar bar) {
        int count = bar.count();
        for (int i = 0; i < count; i++) {
            final int y0 = item1.y;
            final int h1 = item1.width;
            RenderingItem item2 = new RenderingItem(item1);
            item2.y = y0 + (i * h1);
            item2.height = h1;
            PianoKeyLED led = bar.getLEDAt(i, true);
            renderLED(rc, item2, led);
        }
    }

    public static void renderLED(RenderingContext rc, RenderingItem item, PianoKeyLED led) {

        Canvas can = rc.canvas;
        Paint p = new Paint();

        // draw circle
        p.setColor(led.colorCurrent);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        int w2 = item.width / 2;
        int x = item.x + w2;
        int y = item.y + w2;
        int r = w2 * 8 / 10;
        can.drawCircle(x, y, r, p);

        // draw text
        String text = led.text;
        if (text == null) {
            return;
        }
        p.setTextSize(w2);
        p.setColor(led.colorText);
        x = item.left();
        y = item.bottom();
        can.drawText(text, x, y, p);
    }

}
