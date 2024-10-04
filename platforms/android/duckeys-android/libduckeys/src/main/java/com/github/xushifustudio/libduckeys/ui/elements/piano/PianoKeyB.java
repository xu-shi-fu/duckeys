package com.github.xushifustudio.libduckeys.ui.elements.piano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.midi.Note;
import com.github.xushifustudio.libduckeys.ui.boxes.Container;
import com.github.xushifustudio.libduckeys.ui.boxes.LayoutContext;
import com.github.xushifustudio.libduckeys.ui.boxes.RenderingContext;
import com.github.xushifustudio.libduckeys.ui.boxes.RenderingItem;

/****
 * PianoKeyB 表示一个黑键
 * */
public class PianoKeyB extends PianoKey {

    public PianoKeyB(Note n) {
        super(n);
    }

    @Override
    public void updateLayout(LayoutContext lc) {
        super.updateLayout(lc);

        Context ctx = lc.androidContext;
        int c1 = ctx.getColor(R.color.piano_black_key_normal);
        int c2 = ctx.getColor(R.color.piano_black_key_down);
        colorKeyDown = c2;
        colorNormal = c1;
        colorCurrent = c1;
    }

    @Override
    public void render(RenderingContext rc, RenderingItem item) {
        super.render(rc, item);

        Canvas can = rc.canvas;

        float top = item.top();
        float left = item.left();
        float right = item.right();
        float bottom = item.bottom();
        Paint p = new Paint();
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setColor(colorCurrent);
        can.drawRect(left, top, right, bottom, p);

        // draw led(s)
        PianoKeyLEDRenderer.renderLEDBar(rc, item, this.leds);
    }
}
