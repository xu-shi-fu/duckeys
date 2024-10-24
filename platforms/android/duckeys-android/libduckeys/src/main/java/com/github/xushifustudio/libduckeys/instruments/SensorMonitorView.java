package com.github.xushifustudio.libduckeys.instruments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2RectView;

public class SensorMonitorView extends B2RectView {

    private float[] mLinesBuffer;
    private SensorBuffer mSensorBuffer;

    public SensorMonitorView(InstrumentContext ic) {
        mLinesBuffer = new float[1024 * 2];
        mSensorBuffer = ic.getSensorBuffer();
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
