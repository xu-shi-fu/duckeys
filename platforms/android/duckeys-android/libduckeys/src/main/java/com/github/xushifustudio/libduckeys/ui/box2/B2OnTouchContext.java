package com.github.xushifustudio.libduckeys.ui.box2;

import android.view.MotionEvent;

public class B2OnTouchContext {

    public final static int ACTION_DOWN = MotionEvent.ACTION_DOWN;
    public final static int ACTION_POINTER_DOWN = MotionEvent.ACTION_POINTER_DOWN;

    public final static int ACTION_CANCEL = MotionEvent.ACTION_CANCEL;
    public final static int ACTION_MOVE = MotionEvent.ACTION_MOVE;

    public final static int ACTION_POINTER_UP = MotionEvent.ACTION_POINTER_UP;
    public final static int ACTION_UP = MotionEvent.ACTION_UP;


    public int action;
    public boolean done;
    public int depthLimit;

}
