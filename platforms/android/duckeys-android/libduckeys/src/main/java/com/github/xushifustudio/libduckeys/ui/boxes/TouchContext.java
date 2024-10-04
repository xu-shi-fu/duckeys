package com.github.xushifustudio.libduckeys.ui.boxes;

import android.view.MotionEvent;
import android.view.View;

public class TouchContext {

    public View view;
    public final MotionEvent event;
    public final TouchPoint[] points; // 列出所有待处理的触摸点
    public int depthLimit;


    public TouchContext(MotionEvent me) {
        this.depthLimit = 32;
        this.event = me;
        this.points = makePoints(me);
    }

    private static TouchPoint[] makePoints(MotionEvent me) {
        final int count = me.getPointerCount();
        TouchPoint[] list = new TouchPoint[count];
        for (int i = 0; i < count; i++) {
            TouchPoint tp = new TouchPoint();
            tp.index = i;
            tp.id = me.getPointerId(i);
            list[i] = tp;
        }
        return list;
    }
}
