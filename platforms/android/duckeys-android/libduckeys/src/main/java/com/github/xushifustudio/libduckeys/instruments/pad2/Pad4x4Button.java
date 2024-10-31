package com.github.xushifustudio.libduckeys.instruments.pad2;

import android.graphics.Color;
import android.graphics.Paint;

import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.KeyButton;
import com.github.xushifustudio.libduckeys.instruments.KeyState;
import com.github.xushifustudio.libduckeys.midi.ChordNote;
import com.github.xushifustudio.libduckeys.midi.ModeNote;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;

public class Pad4x4Button extends KeyButton {

    public Pad4x4Button(InstrumentContext ctx) {
        super(ctx);
    }

    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        KeyState ks = this.getKeyState();
        if (ks != null) {
            this.setText(ks.note.name);
        }
    }

    @Override
    protected void onPaintAfter(B2RenderThis self) {
        super.onPaintAfter(self);
        KeyState ks = this.getKeyState();
        if (ks.chord != null && ks.chord != ChordNote.NONE) {
            this.paintChordDisplay(self, ks.chord == ChordNote.ROOT);
        }
    }

    private void paintChordDisplay(B2RenderThis self, boolean as_root) {

        int color = Color.rgb(10, 99, 198);
        Paint paint = new Paint();
        ICanvas can = self.getLocalCanvas();

        final float p_w = this.width;
        final float p_h = this.height;
        final float c_w = p_w / 8;
        final float c_h = p_h / 12;

        float top = p_h - (c_h * 2);
        float left = (p_w / 2) - c_w;
        float right = (p_w / 2) + c_w;
        float bottom = p_h - (c_h);

        paint.setColor(color);
        paint.setStyle(as_root ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        paint.setStrokeWidth(c_h / 3);

        can.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public B2State getState() {
        return super.getState();
    }
}
