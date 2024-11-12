package com.github.xushifustudio.duckeys.demo;


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

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.helper.Sleeper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DemoDebugMultiTouchActivity extends Activity {

    private SurfaceView mSurfaceView;
    private final MyCallback mCallback = new MyCallback();
    private final MyTouchListener mTouchListener = new MyTouchListener();
    private final MyTrackerManager mTrackers = new MyTrackerManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_debug_multi_touch);
        mSurfaceView = findViewById(R.id.surface_view_1);
        mSurfaceView.setOnTouchListener(mTouchListener);
    }


    private static class MyTracker {

        private final int id;
        private final List<Float> pts;
        int color;
        float prev_x;
        float prev_y;

        MyTracker(int id, Random rand) {
            this.id = id;
            this.pts = new ArrayList<>();
            this.color = initColor(rand);
        }

        private int initColor(Random rand) {
            int r = rand.nextInt() & 0xff;
            int g = rand.nextInt() & 0xff;
            int b = rand.nextInt() & 0xff;
            return Color.rgb(r, g, b);
        }

        boolean isMoved(float x1, float y1, float x2, float y2) {
            float dx = Math.abs(x1 - x2);
            float dy = Math.abs(y1 - y2);
            final float limit = 0x01;
            return ((dx > limit) || (dy > limit));
        }

        public void onDown(float x, float y) {
            this.pts.clear();
            this.pts.add(x);
            this.pts.add(y);
        }

        public void onMove(float x, float y) {
            if (!isMoved(x, y, prev_x, prev_y)) {
                return;
            }
            this.pts.add(x);
            this.pts.add(y);
            this.prev_x = x;
            this.prev_y = y;
        }

        public void onUp(float x, float y) {
            this.pts.add(x);
            this.pts.add(y);
        }

        float[] pts2array() {
            List<Float> src = this.pts;
            float[] dst = new float[src.size()];
            for (int i = 0; i < dst.length; i++) {
                dst[i] = src.get(i);
            }
            return dst;
        }
    }

    private interface MyTrackerManagerCallback {
        void onTrackerManager(MyTrackerManager tm);
    }

    private static class MyTrackerManager {

        private final Map<Integer, MyTracker> table;
        private final Random mRand;

        MyTrackerManager() {
            long now = System.currentTimeMillis();
            mRand = new Random(now);
            this.table = new HashMap<>();
        }

        List<MyTracker> all() {
            Collection<MyTracker> src = this.table.values();
            List<MyTracker> dst = new ArrayList<>(src);
            return dst;
        }

        void reset() {
            this.table.clear();
        }

        MyTracker findTracker(int id, boolean create) {
            MyTracker tr = table.get(id);
            if (tr == null && create) {
                tr = new MyTracker(id, mRand);
                this.table.put(id, tr);
            }
            return tr;
        }
    }


    private class MyTouchListener implements View.OnTouchListener {

        int count_move;

        @Override
        public boolean onTouch(View view, MotionEvent me) {
            int action = me.getActionMasked();
            int idx = me.getActionIndex();
            int id = me.getPointerId(idx);
            String ptr = "pointer[" + id + "]: ";
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.i(DuckLogger.TAG, ptr + "ACTION_DOWN");
                    count_move = 0;
                    resetAll();
                    handleDown(view, me, idx, id);
                    break;
                case MotionEvent.ACTION_UP:
                    Log.i(DuckLogger.TAG, ptr + "ACTION_UP");
                    handleUp(me, idx, id);
                    resetAll();
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    Log.i(DuckLogger.TAG, ptr + "ACTION_POINTER_DOWN");
                    //   count_move = 0;
                    handleDown(view, me, idx, id);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    Log.i(DuckLogger.TAG, ptr + "ACTION_POINTER_UP");
                    handleUp(me, idx, id);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Log.i(DuckLogger.TAG, ptr + "ACTION_CANCEL");
                    handleUp(me, idx, id);
                    break;
                case MotionEvent.ACTION_MOVE:
                    count_move++;
                    if ((count_move % 30) == 1) {
                        Log.i(DuckLogger.TAG, ptr + "ACTION_MOVE: x" + count_move);
                    }
                    handleMove(me);
                    break;
                default:
                    // break;
                    return false;
            }
            return true;
        }

        void resetAll() {
            forTrackerManager((tm) -> {
                tm.reset();
            });
        }

        void handleDown(View view, MotionEvent me, int index, int id) {
            float x = me.getX(index);
            float y = me.getY(index);
            forTrackerManager((tm) -> {
                MyTracker tr = tm.findTracker(id, true);
                tr.onDown(x, y);
            });
        }

        void handleUp(MotionEvent me, int index, int id) {
            float x = me.getX(index);
            float y = me.getY(index);
            forTrackerManager((tm) -> {
                MyTracker tr = tm.findTracker(id, false);
                if (tr == null) {
                    return;
                }
                tr.onUp(x, y);
            });
        }

        void handleMove(MotionEvent me) {
            final int count = me.getPointerCount();
            forTrackerManager((tm) -> {
                for (int index = 0; index < count; index++) {
                    float x = me.getX(index);
                    float y = me.getY(index);
                    int id = me.getPointerId(index);
                    MyTracker tr = tm.findTracker(id, false);
                    if (tr == null) {
                        continue;
                    }
                    tr.onMove(x, y);
                }
            });
        }
    }


    private class MyCallback implements SurfaceHolder.Callback, Runnable {

        private SurfaceHolder holder = null;
        private boolean running;

        @Override
        public void run() {
            running = true;
            for (; ; ) {
                SurfaceHolder sh = this.holder;
                if (sh == null) {
                    break;
                }
                Rect frame = sh.getSurfaceFrame();
                int w = frame.width();
                int h = frame.height();
                Canvas canvas = null;
                try {
                    canvas = sh.lockCanvas();
                    paint(canvas, w, h);
                } finally {
                    if (canvas != null) {
                        sh.unlockCanvasAndPost(canvas);
                    }
                }
            }
            running = false;
        }


        void waitUntilExit() {
            while (this.running) {
                Sleeper.sleep(100);
            }
        }

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
            this.holder = surfaceHolder;
            Thread th = new Thread(this);
            th.start();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            this.holder = null;
        }
    }


    private synchronized void forTrackerManager(MyTrackerManagerCallback callback) {
        callback.onTrackerManager(mTrackers);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mSurfaceView.getHolder().addCallback(mCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSurfaceView.getHolder().removeCallback(mCallback);
        mCallback.waitUntilExit();
    }

    private void paint(Canvas canvas, int w, int h) {

        Paint paint = new Paint();
        paint.setStrokeWidth(10);

        int gray = 252;
        paint.setColor(Color.rgb(gray, gray, gray));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, w, h, paint);


        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        this.forTrackerManager((tm) -> {
            for (MyTracker tr : tm.all()) {
                paint.setColor(tr.color);
                float[] pts = tr.pts2array();
                canvas.drawLines(pts, paint);
            }
        });


        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, w, h, paint);
    }
}
