package com.github.xushifustudio.libduckeys.instruments;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.helper.Sleeper;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Size;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;

public class InstrumentRenderLooper implements Runnable {

    private final InstrumentContext ic;
    private int layoutRevision;

    public InstrumentRenderLooper(InstrumentContext ctx) {
        this.ic = ctx;
    }


    @Override
    public void run() {
        try {
            logMyself("run-begin");
            B2LayoutContext lc = new B2LayoutContext();
            B2RenderContext rc = new B2RenderContext();
            lc.depthLimit = 32;
            rc.depthLimit = 32;
            while (isAlive()) {
                if (isWantLayout()) {
                    this.doLayout(lc);
                }
                if (isActive()) {
                    this.doPaint(rc);
                } else {
                    Sleeper.sleep(100);
                }
            }
        } finally {
            logMyself("run-end");
        }
    }

    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    ////////////////////////////////////////////////////////////////////////
    //private

    private void doLayout(B2LayoutContext lc) {

        logMyself("doLayout");

        this.layoutRevision = ic.getLayoutRevision();
        B2View v2 = ic.getView2();
        if (v2 == null) {
            return;
        }
        B2LayoutThis th = new B2LayoutThis(lc, v2);
        v2.x = 0;
        v2.y = 0;
        v2.width = ic.getWidth();
        v2.height = ic.getHeight();
        v2.updateLayout(th);
    }

    private void doPaint(B2RenderContext rc) {
        SurfaceHolder holder = ic.getHolder();
        B2View v2 = ic.getView2();
        if (holder == null || v2 == null) {
            return;
        }
        Canvas can = holder.lockCanvas();
        try {
            rc.canvas = can;
            B2RenderThis th = new B2RenderThis(rc, v2);
            v2.paint(th);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            holder.unlockCanvasAndPost(can);
        }
    }

    private boolean isWantLayout() {
        this.checkView2Size();
        return this.layoutRevision != ic.getLayoutRevision();
    }

    private void checkView2Size() {
        B2View v2 = ic.getView2();
        if (v2 == null) {
            return;
        }
        int w = ic.getWidth();
        int h = ic.getHeight();
        if (B2Size.equal(w, v2.width) && B2Size.equal(h, v2.height)) {
            return;
        }
        v2.height = h;
        v2.width = w;
        this.layoutRevision = -1;
    }


    private boolean isAlive() {
        final Runnable r1 = this;
        final Runnable r2 = ic.getLooper();
        if (r2 == null) {
            return false;
        }
        return r1.equals(r2);
    }

    private boolean isActive() {
        return ic.isFinallyActive();
    }

    private void logMyself(String msg) {
        String msg2 = this.getClass().getSimpleName() + ": " + msg;
        Log.i(DuckLogger.TAG, msg2);
    }
}
