package com.github.xushifustudio.libduckeys.instruments;

import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.NonNull;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;

public class InstrumentSurfaceHolderCallback implements SurfaceHolder.Callback {

    private final InstrumentContext ic;

    public InstrumentSurfaceHolderCallback(InstrumentContext ctx) {
        this.ic = ctx;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        View view = ic.getView();
        int w = view.getWidth();
        int h = view.getHeight();

        logMyself("surfaceCreated(w:" + w + ",h:" + h + ")");

        ic.setHolder(holder);
        ic.setWidth(w);
        ic.setHeight(h);
        ic.updateLayoutRevision(1);

        InstrumentRenderLooper looper = new InstrumentRenderLooper(ic);
        ic.setLooper(looper);
        looper.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        logMyself("surfaceChanged(w:" + width + ",h:" + height + ")");

        ic.setHolder(holder);
        ic.setWidth(width);
        ic.setHeight(height);
        ic.updateLayoutRevision(1);
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        logMyself("surfaceDestroyed");

        ic.setHolder(null);
        ic.setLooper(null);
    }

    private void logMyself(String msg) {
        String prefix = this.getClass().getSimpleName() + ": ";
        Log.i(DuckLogger.TAG, prefix + msg);
    }
}
