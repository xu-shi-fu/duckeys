package com.bitwormhole.libduckeys.ui.boxes;

import android.view.MotionEvent;
import android.view.View;

public class TouchContext {

    public View view;
    public final MotionEvent event;
    public final TouchPoint[] points; // 列出所有待处理的触摸点

    public int depthLimit;

    // done 如果等于 true，表示所有的触点已被处理
    public boolean done;

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

    private void forPoint(int index, TouchPoint.Handler h) {
        TouchPoint[] all = points;
        if (0 <= index && index < all.length) {
            TouchPoint tp = all[index];
            h.handlePoint(tp);
        }
    }

    private void updateDone() {
        int todo = 0;
        for (TouchPoint item : points) {
            if (!item.done) {
                todo++;
            }
        }
        done = (todo <= 0);
    }

    public void setPointHandled(int index) {
        forPoint(index, (tp) -> {
            tp.handled = true;
            tp.done = true;
            updateDone();
        });
    }

    public void setPointCancelled(int index) {
        forPoint(index, (tp) -> {
            tp.cancelld = true;
            tp.done = true;
            updateDone();
        });
    }
}
