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

    public LocalCanvas(Canvas global, B2CoordinateSystem cs) {
        mCanvas = global;
        mCoordinates = cs;
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
    public Canvas getGlobalCanvas() {
        return mCanvas;
    }
}
