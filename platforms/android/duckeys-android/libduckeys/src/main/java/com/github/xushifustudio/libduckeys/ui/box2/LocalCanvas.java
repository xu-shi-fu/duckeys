package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public class LocalCanvas implements ICanvas {

    private final Canvas mCanvas;
    private final B2CoordinateSystem mCoordinates;
    private float[] mLinesBuffer;

    public LocalCanvas(Canvas global, B2CoordinateSystem cs) {
        mCanvas = global;
        mCoordinates = cs;
    }

    private float[] getLinesBuffer(int count) {
        float[] older = this.mLinesBuffer;
        if (older != null) {
            if (older.length >= count) {
                return older;
            }
        }
        float[] buf = new float[count * 2];
        this.mLinesBuffer = buf;
        return buf;
    }


    private void prepareDrawLinesBuffer(float[] src, int offset, float[] dst, int count) {
        PointF p1 = new PointF();
        final int end = offset + count;
        int j = 0;
        for (int i = offset; (i + 1) < end; i += 2) {
            p1.x = src[i];
            p1.y = src[i + 1];
            PointF p2 = mCoordinates.local2global(p1);
            dst[j] = p2.x;
            dst[j + 1] = p2.y;
            j += 2;
        }
    }


    @Override
    public void drawRect(@NonNull RectF rect, @NonNull Paint paint) {
        RectF rect2 = mCoordinates.local2global(rect);
        mCanvas.drawRect(rect2, paint);
    }

    @Override
    public void drawRect(@NonNull Rect r, @NonNull Paint paint) {
        Rect rect2 = mCoordinates.local2global(r);
        mCanvas.drawRect(rect2, paint);
    }

    @Override
    public void drawRect(float left, float top, float right, float bottom, @NonNull Paint paint) {
        RectF rect = mCoordinates.local2global(new RectF(left, top, right, bottom));
        mCanvas.drawRect(rect, paint);
    }

    @Override
    public void drawRoundRect(RectF rect, float rx, float ry, Paint paint) {
        RectF rect2 = mCoordinates.local2global(rect);
        mCanvas.drawRoundRect(rect2, rx, ry, paint);
    }

    @Override
    public void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint) {
        RectF rect2 = mCoordinates.local2global(new RectF(left, top, right, bottom));
        mCanvas.drawRoundRect(rect2, rx, ry, paint);
    }

    @Override
    public void drawText(@NonNull char[] text, int index, int count, float x, float y, @NonNull Paint paint) {
        PointF p2 = mCoordinates.local2global(new PointF(x, y));
        mCanvas.drawText(text, index, count, p2.x, p2.y, paint);
    }

    @Override
    public void drawText(String text, float x, float y, Paint paint) {
        PointF p2 = mCoordinates.local2global(new PointF(x, y));
        mCanvas.drawText(text, p2.x, p2.y, paint);
    }

    @Override
    public void drawText(String text, int start, int end, float x, float y, Paint paint) {
        PointF p2 = mCoordinates.local2global(new PointF(x, y));
        mCanvas.drawText(text, start, end, p2.x, p2.y, paint);
    }

    @Override
    public void drawText(CharSequence text, int start, int end, float x, float y, Paint paint) {
        PointF p2 = mCoordinates.local2global(new PointF(x, y));
        mCanvas.drawText(text, start, end, p2.x, p2.y, paint);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, Paint paint) {
        PointF p1 = mCoordinates.local2global(new PointF(x1, y1));
        PointF p2 = mCoordinates.local2global(new PointF(x2, y2));
        mCanvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
    }

    @Override
    public void drawLines(float[] pts, int offset, int count, Paint paint) {
        float[] dst = this.getLinesBuffer(count);
        this.prepareDrawLinesBuffer(pts, offset, dst, count);
        mCanvas.drawLines(dst, 0, count, paint);
    }

    @Override
    public void drawLines(float[] pts, Paint paint) {
        float[] dst = this.getLinesBuffer(pts.length);
        this.prepareDrawLinesBuffer(pts, 0, dst, pts.length);
        mCanvas.drawLines(dst, 0, pts.length, paint);
    }

    @Override
    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        PointF pc = mCoordinates.local2global(new PointF(cx, cy));
        mCanvas.drawCircle(pc.x, pc.y, radius, paint);
    }


    @Override
    public Canvas getGlobalCanvas() {
        return mCanvas;
    }
}
