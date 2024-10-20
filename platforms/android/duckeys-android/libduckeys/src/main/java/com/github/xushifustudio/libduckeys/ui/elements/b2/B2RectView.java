package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Size;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.styles.B2StyleReader;

public class B2RectView extends B2View {

    private MyLayoutCache cache;

    public B2RectView() {
    }

    private static class MyStateStyle {
        PointF borderRadius;  // maybe null
        Paint borderPaint;
        Paint bgPaint;
    }


    private static class MyLayoutCache {

        final long revision;
        final RectF rect;
        final MyStateStyle styleNormal;
        final MyStateStyle stylePressed;
        final MyStateStyle styleSelected;

        public MyLayoutCache(long rev) {
            this.revision = rev;
            this.rect = new RectF();
            this.styleNormal = new MyStateStyle();
            this.stylePressed = new MyStateStyle();
            this.styleSelected = new MyStateStyle();
        }
    }


    private MyLayoutCache getLayoutCache(B2Style style1, boolean checkRev) {
        if (style1 == null) {
            style1 = this.getStyle(true);
        }
        MyLayoutCache older = this.cache;
        if (older != null && style1 != null) {
            if (!checkRev) {
                return older;
            }
            if (older.revision == style1.revision()) {
                return older;
            }
        }
        MyLayoutCache newer = this.loadLayoutCache(style1);
        this.cache = newer;
        return newer;
    }

    private MyLayoutCache loadLayoutCache(B2Style style1) {
        if (style1 == null) {
            style1 = this.getStyle(true);
        }
        final long rev = style1.revision();
        MyLayoutCache lc = new MyLayoutCache(rev);
        lc.rect.set(0, 0, this.width, this.height);
        loadStateStyle(lc, lc.styleNormal, style1, B2State.NORMAL);
        loadStateStyle(lc, lc.stylePressed, style1, B2State.PRESSED);
        loadStateStyle(lc, lc.styleSelected, style1, B2State.SELECTED);

        Log.d(DuckLogger.TAG, "B2RectView.loadLayoutCache()");

        return lc;
    }


    private void loadStateStyle(MyLayoutCache lc, MyStateStyle ss, B2Style style1, B2State state) {
        B2StyleReader srd = new B2StyleReader(style1);
        srd.setState(state);
        ss.borderRadius = readBorderRadius(srd, style1);
        ss.borderPaint = readBorderPaint(srd, style1);
        ss.bgPaint = readBgPaint(srd, style1);
    }


    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);

        MyLayoutCache lc = this.getLayoutCache(null, true);
        MyStateStyle ss = this.getStyleByState(lc);
        this.drawBackground(self, lc, ss);
    }

    @Override
    protected void onPaintChildren(B2RenderThis self) {
        super.onPaintChildren(self);
    }

    @Override
    protected void onPaintAfter(B2RenderThis self) {
        super.onPaintAfter(self);

        MyLayoutCache lc = this.getLayoutCache(null, false);
        MyStateStyle ss = this.getStyleByState(lc);
        this.drawBorder(self, lc, ss);
    }


    private Paint readBgPaint(B2StyleReader srd, B2Style sty) {

        String[] key_bg_color = {B2Style.background_color};
        int val_bg_color = srd.readColor(key_bg_color);
        if (val_bg_color == 0) {
            return null;
        }

        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setStrokeWidth(1);
        p.setColor(val_bg_color);
        return p;
    }


    private PointF readBorderRadius(B2StyleReader srd, B2Style sty) {
        String[] key_border_radius_x = {B2Style.border_radius_x, B2Style.border_radius};
        String[] key_border_radius_y = {B2Style.border_radius_y, B2Style.border_radius};
        float val_border_radius_x = srd.readSize(key_border_radius_x);
        float val_border_radius_y = srd.readSize(key_border_radius_y);
        if (B2Size.isZero(val_border_radius_x) && B2Size.isZero(val_border_radius_y)) {
            return null;
        }
        return new PointF(val_border_radius_x, val_border_radius_y);
    }


    private Paint readBorderPaint(B2StyleReader srd, B2Style sty) {

        String[] key_border_width = {B2Style.border_width, B2Style.border};
        String[] key_border_color = {B2Style.border_color, B2Style.border};

        float val_border_width = srd.readSize(key_border_width);
        int val_border_color = srd.readColor(key_border_color);

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(val_border_width);
        p.setColor(val_border_color);
        return p;
    }

    private MyStateStyle getStyleByState(MyLayoutCache ca) {
        if (ca == null) {
            return null;
        }
        if (this.pressed) {
            return ca.stylePressed;
        }
        if (this.selected) {
            return ca.styleSelected;
        }
        return ca.styleNormal;
    }


    private void drawBorder(B2RenderThis self, MyLayoutCache lc, MyStateStyle ss) {
        if (self == null || lc == null || ss == null) {
            return;
        }
        ICanvas can = self.getLocalCanvas();
        PointF round = ss.borderRadius;  // maybe null
        Paint paint = ss.borderPaint;
        RectF rect = lc.rect;
        if (rect == null || paint == null) {
            return;
        }
        if (round == null) {
            can.drawRect(rect, paint);
        } else {
            can.drawRoundRect(rect, round.x, round.y, paint);
        }
    }

    private void drawBackground(B2RenderThis self, MyLayoutCache lc, MyStateStyle ss) {
        if (self == null || lc == null || ss == null) {
            return;
        }
        ICanvas can = self.getLocalCanvas();
        PointF round = ss.borderRadius; // maybe null
        Paint paint = ss.bgPaint;
        RectF rect = lc.rect;
        if (rect == null || paint == null) {
            return;
        }
        if (round == null) {
            can.drawRect(rect, paint);
        } else {
            can.drawRoundRect(rect, round.x, round.y, paint);
        }
    }
}
