package com.github.xushifustudio.libduckeys.ui.box2;

import android.view.MotionEvent;

import java.util.HashMap;
import java.util.Map;

public class B2OnTouchContext {

    public final static int ACTION_DOWN = MotionEvent.ACTION_DOWN;
    public final static int ACTION_POINTER_DOWN = MotionEvent.ACTION_POINTER_DOWN;

    public final static int ACTION_CANCEL = MotionEvent.ACTION_CANCEL;
    public final static int ACTION_MOVE = MotionEvent.ACTION_MOVE;

    public final static int ACTION_POINTER_UP = MotionEvent.ACTION_POINTER_UP;
    public final static int ACTION_UP = MotionEvent.ACTION_UP;


    public int action;
    public boolean brake; // 表示已经中断 on-touch walking 过程
    public boolean cancelled;
    public boolean started;
    public boolean stopped;
    public int depthLimit;

    public long startedAt; // timestamp
    public long stoppedAt; // timestamp


    public B2OnTouchPointer pointer;
    public final Map<Integer, B2OnTouchPointer> pointers; // map<id,pointer>

    public B2OnTouchContext() {
        this.pointers = new HashMap<>();
    }
}
