package com.github.xushifustudio.libduckeys.instruments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.Mode;
import com.github.xushifustudio.libduckeys.midi.ModePattern;
import com.github.xushifustudio.libduckeys.midi.Note;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2RectView;

public class SensorMonitorView extends B2RectView {

    private float[] mLinesBuffer;
    private SensorBuffer mSensorBuffer;
    private InstrumentContext mIC;

    public SensorMonitorView(InstrumentContext ic) {
        mLinesBuffer = new float[1024 * 2];
        mSensorBuffer = ic.getSensorBuffer();
        mIC = ic;
    }

    @Override
    protected void onPaintAfter(B2RenderThis self) {
        super.onPaintAfter(self);

        ICanvas can = self.getLocalCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        int padding = 10;
        RectF rect = new RectF(0, 0, this.width, this.height);
        rect.top += padding;
        rect.left += padding;
        rect.right -= padding;
        rect.bottom -= padding;

        paint.setColor(Color.RED);
        this.prepareLinesForX(mLinesBuffer, 0, 10);
        can.drawLines(mLinesBuffer, paint);

        paint.setColor(Color.GREEN);
        this.prepareLinesForY(mLinesBuffer, 0, 10);
        can.drawLines(mLinesBuffer, paint);

        paint.setColor(Color.rgb(50, 150, 250));
        this.prepareLinesForZ(mLinesBuffer, 0, 10);
        can.drawLines(mLinesBuffer, paint);

        paint.setColor(Color.GRAY);
        can.drawRect(rect, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(39);
        //    this.paintMode(can, paint);
        this.paintChord(can, paint);
    }


    private void paintMode(ICanvas can, Paint paint) {
        ModeManager mm = this.mIC.getModeManager();
        Mode mode = mm.getHave();
        if (mode == null) {
            return;
        }
        Note n1 = mode.getNote1();
        ModePattern pattern = mode.getPattern();
        StringBuilder b = new StringBuilder();
        b.append("mode: ");
        b.append(n1.key).append(n1.sharp ? "# " : " ");
        b.append(pattern.name());
        can.drawText(b.toString(), 20, 70, paint);
    }

    private void paintChord(ICanvas can, Paint paint) {
        Chord ch1 = this.mIC.getChordManager().input.getWant();
        if (ch1 == null) {
            return;
        }
        String text = ch1.toString();
        float txt_w = paint.measureText(text);
        float x1 = (this.width - txt_w) / 2;
        float y1 = this.height - 20;
        can.drawText(text, x1, y1, paint);
    }


    private void prepareLinesForX(float[] dst, float ref, float scale) {
        int j = 0;
        int size = mSensorBuffer.size();
        float y0 = this.height / 2;
        for (int i = 0; i < size; i++) {
            SensorBuffer.Sample sample = mSensorBuffer.getSample(i);
            float value = sample.x;
            float y = (value - ref) * scale;
            float x = i;
            if (j + 1 < dst.length) {
                dst[j] = x;
                dst[j + 1] = y + y0;
            }
            j += 2;
        }
    }

    private void prepareLinesForY(float[] dst, float ref, float scale) {
        int j = 0;
        int size = mSensorBuffer.size();
        float y0 = this.height / 2;
        for (int i = 0; i < size; i++) {
            SensorBuffer.Sample sample = mSensorBuffer.getSample(i);
            float value = sample.y;
            float y = (value - ref) * scale;
            float x = i;
            if (j + 1 < dst.length) {
                dst[j] = x;
                dst[j + 1] = y + y0;
            }
            j += 2;
        }
    }

    private void prepareLinesForZ(float[] dst, float ref, float scale) {
        int j = 0;
        int size = mSensorBuffer.size();
        float y0 = this.height / 2;
        for (int i = 0; i < size; i++) {
            SensorBuffer.Sample sample = mSensorBuffer.getSample(i);
            float value = sample.z;
            float y = (value - ref) * scale;
            float x = i;
            if (j + 1 < dst.length) {
                dst[j] = x;
                dst[j + 1] = y + y0;
            }
            j += 2;
        }
    }
}
