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
 * PianoKeyB 表示一个白键
 */
public class PianoKeyW extends PianoKey {

    public final PianoKeyW1 upper;
    public final PianoKeyW2 lower;


    public PianoKeyW(Note n) {
        super(n);

        upper = new PianoKeyW1(this);
        lower = new PianoKeyW2(this);
    }

    @Override
    public void updateLayout(LayoutContext lc) {
        super.updateLayout(lc);

        Context ctx = lc.androidContext;
        int c1 = ctx.getColor(R.color.piano_white_key_normal);
        int c2 = ctx.getColor(R.color.piano_white_key_down);
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
        p.setStrokeWidth(2);

        p.setStyle(Paint.Style.FILL);
        p.setColor(colorCurrent);
        can.drawRect(left, top, right, bottom, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.BLACK);
        can.drawRect(left, top, right, bottom, p);
    }
}
