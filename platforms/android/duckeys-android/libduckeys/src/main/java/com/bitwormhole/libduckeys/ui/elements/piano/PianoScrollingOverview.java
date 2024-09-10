package com.bitwormhole.libduckeys.ui.elements.piano;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bitwormhole.libduckeys.ui.boxes.Container;
import com.bitwormhole.libduckeys.ui.boxes.Element;
import com.bitwormhole.libduckeys.ui.boxes.Layout;
import com.bitwormhole.libduckeys.ui.boxes.LayoutContext;
import com.bitwormhole.libduckeys.ui.boxes.RenderingContext;
import com.bitwormhole.libduckeys.ui.boxes.RenderingItem;

public class PianoScrollingOverview extends Container {

    private final PianoKeyboard mKeyboard;
    private final MyMaskView mMask;

    private int mScrollingKeyMin;
    private int mScrollingKeyFrom;
    private int mScrollingKeyTo;
    private int mScrollingKeyMax;

    public PianoScrollingOverview() {

        mScrollingKeyMin = 0;
        mScrollingKeyFrom = 20 + 20;
        mScrollingKeyTo = 20 + 40;
        mScrollingKeyMax = 127;


        mKeyboard = new PianoKeyboard();
        mMask = new MyMaskView();


        mKeyboard.firstKeysInView = mScrollingKeyMin;
        mKeyboard.countKeysInView = mScrollingKeyMax - mScrollingKeyMin;
        mKeyboard.z = 0;
        mMask.z = 5;

        this.addChild(mKeyboard);
        this.addChild(mMask);
        this.setLayout(new MyLayout());
    }

    private class MyMaskView extends Element {

        private Rect getViewRangeRect(RenderingContext rc, RenderingItem item) {
            Rect rect = new Rect();
            rect.top = item.top();
            rect.left = item.left();
            rect.right = item.right();
            rect.bottom = item.bottom();
            final int x = rect.left;
            final int w = rect.width();
            float total = (mScrollingKeyMax - mScrollingKeyMin);
            if (total < 1) {
                total = 1;
            }
            float p1 = (mScrollingKeyFrom - mScrollingKeyMin) / total;
            float p2 = (mScrollingKeyTo - mScrollingKeyMin) / total;
            rect.left = x + (int) (w * p1);
            rect.right = x + (int) (w * p2);
            return rect;
        }

        @Override
        public void render(RenderingContext rc, RenderingItem item) {
            super.render(rc, item);

            Rect rect = getViewRangeRect(rc, item);
            Paint p = new Paint();
            Canvas can = rc.canvas;

            // 绘制遮罩
            p.setAlpha(50);
            p.setColor(Color.argb(199, 30, 30, 30));
            p.setStyle(Paint.Style.FILL);
            p.setStrokeWidth(0);
            can.drawRect(item.left(), rect.top, rect.left, rect.bottom, p); // left rect
            can.drawRect(rect.right, rect.top, item.right(), rect.bottom, p); // right rect

            // 绘制红框
            p.setColor(Color.RED);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(10);
            can.drawRect(rect, p);
        }
    }

    private class MyLayout implements Layout {
        @Override
        public void apply(LayoutContext lc, Container can) {

            int w = can.width;
            int h = can.height;

            mKeyboard.x = 0;
            mKeyboard.y = 0;
            mKeyboard.width = w;
            mKeyboard.height = h;

            mMask.x = 0;
            mMask.y = 0;
            mMask.width = w;
            mMask.height = h;
        }
    }
}
