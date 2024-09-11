package com.bitwormhole.libduckeys.ui.views;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.context.Life;
import com.bitwormhole.libduckeys.helper.DuckLogger;
import com.bitwormhole.libduckeys.ui.boxes.Element;
import com.bitwormhole.libduckeys.ui.boxes.LayoutContext;
import com.bitwormhole.libduckeys.ui.boxes.RenderingContext;
import com.bitwormhole.libduckeys.ui.boxes.TouchContext;
import com.bitwormhole.libduckeys.ui.boxes.Viewport;

import java.util.ArrayList;
import java.util.List;

public class SurfaceViewController implements Life {

    private Activity mActivity;
    private Viewport mViewport;

    private SurfaceView mSurfaceView;
    private MySurfaceCallback mSurfaceCallback;
    private SurfaceHolder mSurfaceHolder;

    private MyOnTouchListener mOnTouchListener;

    private int mFrameCount;
    private long mRevision; // 表示渲染或布局的版本


    public SurfaceViewController(Activity parent) {
        mActivity = parent;
        mRevision = System.currentTimeMillis();
    }


    private class MySurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder sh) {
            Log.i(DuckLogger.TAG, "SurfaceHolder.Callback.surfaceCreated()");

            Rect frame = sh.getSurfaceFrame();
            Viewport vpt = mViewport;
            vpt.x = 0;
            vpt.y = 0;
            vpt.width = frame.width();
            vpt.height = frame.height();
            rebuildLayout(vpt);

            mSurfaceHolder = sh;
            MyRunner runner = new MyRunner(sh);
            runner.start();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int format, int width, int height) {
            Log.i(DuckLogger.TAG, "SurfaceHolder.Callback.surfaceChanged()");
            Viewport vpt = mViewport;
            vpt.x = 0;
            vpt.y = 0;
            vpt.width = width;
            vpt.height = height;
            rebuildLayout(vpt);
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder sh) {
            Log.i(DuckLogger.TAG, "SurfaceHolder.Callback.surfaceDestroyed()");
            mSurfaceHolder = null;
        }
    }

    private class MyOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            dispatchOnTouchEvent(view, motionEvent);
            return true;
        }
    }

    private class MyRunner implements Runnable {

        public MyRunner(SurfaceHolder sh) {
        }


        @Override
        public void run() {
            SurfaceHolder sh = null;
            Canvas can = null;
            for (; ; ) {
                sh = mSurfaceHolder;
                can = null;
                if (sh == null) {
                    return;
                }
                try {
                    // Thread.sleep(10);
                    can = sh.lockCanvas();
                    //paint ...
                    paint(can);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (can != null && sh != null) {
                    sh.unlockCanvasAndPost(can);
                }
            }
        }

        public void start() {
            (new Thread(this)).start();
        }
    }

    private void rebuildLayout(Viewport vpt) {

        if (vpt == null) return;
        Element root = vpt.root;
        if (root == null) return;

        LayoutContext lc = new LayoutContext();
        lc.viewport = vpt;
        invoke(() -> {
            lc.revision = mRevision;
            lc.androidContext = mActivity;
            vpt.updateLayout(lc);
        });
    }

    private void paint(Canvas can) {
        mFrameCount++;

        Viewport vpt = mViewport;
        if (vpt == null) return;

        Element root = vpt.root;
        if (root == null) return;

        RenderingContext rc = new RenderingContext();
        rc.viewport = vpt;
        rc.canvas = can;
        invoke(() -> {
            rc.revision = mRevision;
            vpt.render(rc, null);
        });
    }

    private void dispatchOnTouchEvent(View view, MotionEvent motionEvent) {

        Viewport vpt = mViewport;
        if (vpt == null) return;

        Element root = vpt.root;
        if (root == null) return;

        TouchContext tc = new TouchContext(motionEvent);
        tc.view = view;

        invoke(() -> {
            vpt.onTouch(tc, null);
        });
    }

    public void init(SurfaceView view, Viewport vpt) {
        mSurfaceView = view;
        mViewport = vpt;
        mSurfaceCallback = new MySurfaceCallback();
        mOnTouchListener = new MyOnTouchListener();

        mSurfaceView.setOnTouchListener(mOnTouchListener);
        mSurfaceView.getHolder().addCallback(mSurfaceCallback);

        checkRequiredComponents();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // use: init
    }


    private void checkRequiredComponents() {
        String tag = "SurfaceViewController: ";
        if (mActivity == null) {
            throw new RuntimeException(tag + "mActivity is null");
        }
        if (mSurfaceView == null) {
            throw new RuntimeException(tag + "mSurfaceView is null");
        }
        if (mViewport == null) {
            throw new RuntimeException(tag + "mViewport is null");
        }
    }

    /**
     * invoke 调用 Runnable，带同步锁
     */
    public synchronized void invoke(Runnable r) {
        mRevision++;
        r.run();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onPause() {
        Log.i(DuckLogger.TAG, "SurfaceViewController.onPause()");
        // mSurfaceView.getHolder().removeCallback(mSurfaceCallback);
    }

    @Override
    public void onResume() {
        Log.i(DuckLogger.TAG, "SurfaceViewController.onResume()");
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }
}
