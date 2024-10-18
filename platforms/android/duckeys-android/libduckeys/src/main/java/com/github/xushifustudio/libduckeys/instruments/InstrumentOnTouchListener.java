package com.github.xushifustudio.libduckeys.instruments;

import android.view.MotionEvent;
import android.view.View;

public class InstrumentOnTouchListener implements View.OnTouchListener {

    public InstrumentOnTouchListener(InstrumentContext ctx) {
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        view.performClick();

        return false;
    }
}
