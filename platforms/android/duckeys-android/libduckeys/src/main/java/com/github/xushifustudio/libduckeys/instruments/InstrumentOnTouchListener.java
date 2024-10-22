package com.github.xushifustudio.libduckeys.instruments;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchPointer;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Size;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;

import java.util.ArrayList;
import java.util.List;

public class InstrumentOnTouchListener implements View.OnTouchListener {

    private final InstrumentContext ic;
    private B2OnTouchContext current;

    public InstrumentOnTouchListener(InstrumentContext ctx) {
        this.ic = ctx;
    }

    @Override
    public boolean onTouch(View view, MotionEvent me) {

        view.performClick();

        boolean default_result = true;
        final int action = me.getActionMasked();
        final long now = System.currentTimeMillis();

        B2OnTouchContext on_touch_ctx = null;
        if (action == B2OnTouchContext.ACTION_DOWN) {
            this.current = null;
            on_touch_ctx = this.getOnTouchContext(now, true);
        } else {
            on_touch_ctx = this.getOnTouchContext(now, false);
        }
        if (on_touch_ctx == null) {
            return default_result;
        }

        B2OnTouchPointer ptr = this.prepareCurrentPointer(on_touch_ctx, view, me, now);
        on_touch_ctx.action = action;
        on_touch_ctx.pointer = ptr;

        switch (action) {
            case B2OnTouchContext.ACTION_MOVE:
                ptr.updatedAt = now;
                break;
            case B2OnTouchContext.ACTION_DOWN:
                on_touch_ctx.started = true;
                on_touch_ctx.startedAt = now;
                ptr.started = true;
                ptr.startedAt = now;
                break;
            case B2OnTouchContext.ACTION_POINTER_DOWN:
                ptr.started = true;
                ptr.stopped = false;
                ptr.startedAt = now;
                break;
            case B2OnTouchContext.ACTION_POINTER_UP:
                ptr.stopped = true;
                ptr.stoppedAt = now;
                break;
            case B2OnTouchContext.ACTION_UP:
                on_touch_ctx.stopped = true;
                on_touch_ctx.stoppedAt = now;
                ptr.stopped = true;
                ptr.stoppedAt = now;
                break;
            case B2OnTouchContext.ACTION_CANCEL:
                on_touch_ctx.cancelled = true;
                this.handleOnTouchCancel(on_touch_ctx, false);
                default_result = false;
                break;
            default:
                break;
        }

        if (action == B2OnTouchContext.ACTION_MOVE) {
            this.invokeWhileMove(on_touch_ctx, now, me);
        } else {
            on_touch_ctx.brake = false;
            B2View v2 = ic.getView2();
            B2OnTouchThis on_touch_th = new B2OnTouchThis(on_touch_ctx, v2);
            v2.onTouch(on_touch_th);
        }

        return default_result;
    }

    private B2OnTouchContext mCancelledOnTouchContext;

    private void handleOnTouchCancel(final B2OnTouchContext ctx, boolean immediate) {
        if (!immediate) {
            // close ctx later
            final B2OnTouchContext older = mCancelledOnTouchContext;
            mCancelledOnTouchContext = ctx;
            this.handleOnTouchCancel(older, true);
        }
        // close ctx right now
        if (ctx == null) {
            return;
        }
        long now = System.currentTimeMillis();
        for (B2OnTouchPointer ptr : ctx.pointers.values()) {
            ptr.bind(null);
            ptr.stopped = true;
            ptr.stoppedAt = now;
        }
        ctx.stoppedAt = now;
        ctx.stopped = true;
    }

    private void invokeWhileMove(B2OnTouchContext ctx, long now, MotionEvent me) {

        int count = me.getPointerCount();
        List<B2OnTouchPointer> updated_list = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            final float x = me.getX(i);
            final float y = me.getY(i);
            final int id = me.getPointerId(i);
            B2OnTouchPointer ptr = ctx.pointers.get(id);
            if (ptr == null) {
                continue;
            }

            //if (B2Size.equal(x, ptr.globalX) && B2Size.equal(y, ptr.globalY)) {
            //  continue;
            //}

            ptr.globalX = x;
            ptr.globalY = y;
            ptr.updatedAt = now;
            updated_list.add(ptr);
            //  Log.d(DuckLogger.TAG, "on_touch.move(" + x + "," + y + ")");
        }

        if (updated_list.size() == 0) {
            return;
        }

        B2View v2 = ic.getView2();
        B2OnTouchThis on_touch_th = new B2OnTouchThis(ctx, v2);

        for (B2OnTouchPointer ptr : updated_list) {
            ctx.brake = false;
            ctx.pointer = ptr;
            v2.onTouch(on_touch_th);
        }
    }


    private B2OnTouchPointer prepareCurrentPointer(B2OnTouchContext ctx, View view, MotionEvent me, long now) {

        int index = me.getActionIndex();
        int id = me.getPointerId(index);
        float x = me.getX(index);
        float y = me.getY(index);

        B2OnTouchPointer ptr = ctx.pointers.get(id);

        if (ptr == null) {
            ptr = new B2OnTouchPointer(id, x, y);
            ctx.pointers.put(id, ptr);
        }

        ptr.globalX = x;
        ptr.globalY = y;
        ptr.updatedAt = now;

        return ptr;
    }


    private void initOnTouchContext(B2OnTouchContext ctx, long now) {
        ctx.depthLimit = 28;
        ctx.started = false;
        ctx.startedAt = now;
    }

    private B2OnTouchContext getOnTouchContext(long now, boolean create) {
        B2OnTouchContext ot_ctx = this.current;
        if (ot_ctx == null && create) {
            ot_ctx = new B2OnTouchContext();
            initOnTouchContext(ot_ctx, now);
            this.current = ot_ctx;
        }
        return ot_ctx;
    }
}
