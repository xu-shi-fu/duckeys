package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Size;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.styles.B2StyleReader;

public class B2RectView extends B2View {

    private RectF mRect;
    private PointF mRoundParams;
    private Paint mBorderPaint;
    private Paint mBgPaint;

    public B2RectView() {
    }


    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);
        this.prepareDraw(self);
        this.drawBackground(self);
    }

    @Override
    protected void onPaintChildren(B2RenderThis self) {
        super.onPaintChildren(self);
    }

    @Override
    protected void onPaintAfter(B2RenderThis self) {
        super.onPaintAfter(self);
        this.drawBorder(self);
    }


    private void prepareDraw(B2RenderThis self) {
        float w = this.width;
        float h = this.height;
        RectF rect = new RectF(0, 0, w, h);
        B2Style sty = this.getStyle(true);
        B2StyleReader srd = new B2StyleReader(sty);
        Paint bg = this.readBgPaint(srd, sty);
        Paint border = this.readBorderPaint(srd, sty);
        PointF round = this.readBorderRadius(srd, sty);

        mRoundParams = round;
        mBgPaint = bg;
        mBorderPaint = border;
        mRect = rect;
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


    private void drawBorder(B2RenderThis self) {
        ICanvas can = self.getLocalCanvas();
        PointF round = mRoundParams; // maybe null
        Paint paint = mBorderPaint;
        RectF rect = mRect;
        if (rect == null || paint == null) {
            return;
        }
        if (round == null) {
            can.drawRect(rect, paint);
        } else {
            can.drawRoundRect(rect, round.x, round.y, paint);
        }
    }

    private void drawBackground(B2RenderThis self) {
        ICanvas can = self.getLocalCanvas();
        PointF round = mRoundParams; // maybe null
        Paint paint = mBgPaint;
        RectF rect = mRect;
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
