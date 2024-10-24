package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;

public interface ICanvas {


    void drawRect(@NonNull RectF rect, @NonNull Paint paint);

    void drawRect(@NonNull Rect r, @NonNull Paint paint);

    void drawRect(float left, float top, float right, float bottom, @NonNull Paint paint);


    void drawRoundRect(RectF rect, float rx, float ry, Paint paint);

    void drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint);


    void drawText(char[] text, int index, int count, float x, float y, Paint paint);

    void drawText(String text, float x, float y, Paint paint);

    void drawText(String text, int start, int end, float x, float y, Paint paint);

    void drawText(CharSequence text, int start, int end, float x, float y, Paint paint);


    void drawLine(float x1, float y1, float x2, float y2, Paint paint);

    void drawLines(float[] pts, int offset, int count, Paint paint);

    void drawLines(float[] pts, Paint paint);


    Canvas getGlobalCanvas();

}
