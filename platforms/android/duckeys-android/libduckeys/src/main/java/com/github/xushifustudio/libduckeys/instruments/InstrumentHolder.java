package com.github.xushifustudio.libduckeys.instruments;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.context.BaseLife;

public class InstrumentHolder extends BaseLife {

    private final InstrumentContext instrumentContext;
    private InstrumentContext instrumentContextToInit;

    public InstrumentHolder() {
        this.instrumentContext = new InstrumentContext();
    }

    public InstrumentHolder(InstrumentContext ic) {
        this.instrumentContext = ic;
    }


    private void init(InstrumentContext ctx) {

        if (ctx == null) {
            return;
        }

        SurfaceView view = ctx.getView();
        if (view == null) {
            throw new RuntimeException("InstrumentContext.view == null");
        }

        SurfaceHolder sh = view.getHolder();
        InstrumentSurfaceHolderCallback callback = new InstrumentSurfaceHolderCallback(ctx);
        InstrumentOnTouchListener listener = new InstrumentOnTouchListener(ctx);

        ctx.setCallback(callback);
        ctx.setOnTouchListener(listener);
        // ctx.setHolder(sh); // set by callback
        view.setOnTouchListener(listener);

        sh.addCallback(callback);
    }


    public InstrumentContext getInstrumentContext() {
        return instrumentContext;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.instrumentContextToInit = this.instrumentContext;
    }

    @Override
    public void onStart() {
        super.onStart();

        InstrumentContext ic = this.instrumentContextToInit;
        this.instrumentContextToInit = null;
        this.init(ic);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.instrumentContext.setActive(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.instrumentContext.setLooper(null);
        this.instrumentContext.setActive(false);
    }
}
