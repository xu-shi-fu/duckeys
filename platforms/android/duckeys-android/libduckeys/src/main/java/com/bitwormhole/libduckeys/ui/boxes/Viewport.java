package com.bitwormhole.libduckeys.ui.boxes;

import android.util.Log;
import android.view.MotionEvent;

import com.bitwormhole.libduckeys.helper.DuckLogger;

import java.util.List;

public class Viewport extends Box implements RenderAble, LayoutAble, TouchAble {

    public Element root;

    @Override
    public void updateLayout(LayoutContext lc) {
        if (root == null || lc == null) {
            return;
        }
        final Viewport vpt = Viewport.this;
        root.x = 0;
        root.y = 0;
        root.width = vpt.width;
        root.height = vpt.height;
        root.updateLayout(lc);
    }


    @Override
    public void render(RenderingContext rc, RenderingItem itemIgnored) {

        if (root == null || rc == null) {
            return;
        }

        final List<RenderingItem> list = rc.items;

        // walk
        NodeTreeWalker walker = new NodeTreeWalker();
        walker.walk(root, (item1) -> {
            RenderingItem item2 = new RenderingItem(rc, item1);
            list.add(item2);
        });

        // sort (根据z轴排序)
        list.sort((a, b) -> {
            return a.z - b.z;
        });

        // paint
        for (RenderingItem item : list) {
            item.target.render(rc, item);
        }
    }

    private void logOnTouch(TouchContext tc, TouchEventAdapter ada) {
        MotionEvent event = tc.event;
        int action = event.getAction();
        String msg = null;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                msg = "MotionEvent.ACTION_DOWN";
                break;

            case MotionEvent.ACTION_POINTER_UP:
                msg = "MotionEvent.ACTION_POINTER_UP";
                break;

            case MotionEvent.ACTION_UP:
                msg = "MotionEvent.ACTION_UP";
                break;

            default:
                // msg = "MotionEvent.default: " + action;
                msg = MotionEvent.actionToString(action);
                break;
        }
        Log.d(DuckLogger.TAG, msg);
    }


    @Override
    public void onTouch(TouchContext tc, TouchEventAdapter ada) {

        if (tc == null || root == null) {
            return;
        }

        ada = new TouchEventAdapter();
        ada.context = tc;
        ada.offsetX = 0;
        ada.offsetY = 0;
        ada.parent = null;
        ada.target = root;

        logOnTouch(tc, ada);
        root.onTouch(tc, ada);
    }
}
