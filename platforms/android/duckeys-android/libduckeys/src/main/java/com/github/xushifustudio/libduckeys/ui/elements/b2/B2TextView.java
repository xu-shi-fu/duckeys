package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.styles.B2StyleReader;

public class B2TextView extends B2RectView {

    private String text;
    private Paint cachedTextPaint;

    public B2TextView() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private Paint getPaintForText(boolean reload) {
        Paint p = this.cachedTextPaint;
        if (reload) {
            p = null;
        }
        if (p == null) {
            B2Style sty = this.getStyle(true);
            B2StyleReader sr = new B2StyleReader(sty);
            p = new Paint();
            p.setTextSize(sr.readSize(B2Style.font_size));
            p.setColor(sr.readColor(B2Style.text_color));
            this.cachedTextPaint = p;
        }
        return p;
    }


    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        String str = this.text;
        if (str != null) {
            Paint p = this.getPaintForText(true);
            Rect rect = new Rect();
            p.getTextBounds(str, 0, str.length(), rect);
            this.contentHeight = rect.height();
            this.contentWidth = rect.width();
        } else {
            this.contentHeight = 0;
            this.contentWidth = 0;
        }
    }

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);
    }


    private PointF computeDrawTextAt() {

        B2Style sty = this.getStyle(true);
        B2StyleReader srd = new B2StyleReader(sty);
        B2Align align = srd.readAlign(B2Style.align);

        float box_w = this.width;
        float box_h = this.height;
        float str_w = this.contentWidth;
        float str_h = this.contentHeight;
        float offset_y = (box_h / 2) - (str_h / 2);
        float offset_x = (box_w / 2) - (str_w / 2);
        float padding_y, padding_x;

        final String[] padding_top = {B2Style.padding_top, B2Style.padding};
        final String[] padding_left = {B2Style.padding_left, B2Style.padding};
        final String[] padding_right = {B2Style.padding_right, B2Style.padding};
        final String[] padding_bottom = {B2Style.padding_bottom, B2Style.padding};

        PointF at = new PointF();
        at.x = offset_x;
        at.y = offset_y + str_h;

        switch (align) {
            case TOP:
                padding_y = srd.readSize(padding_top);
                at.y -= (offset_y - padding_y);
                break;
            case LEFT:
                padding_x = srd.readSize(padding_left);
                at.x -= (offset_x - padding_x);
                break;
            case RIGHT:
                padding_x = srd.readSize(padding_right);
                at.x += (offset_x - padding_x);
                break;
            case BOTTOM:
                padding_y = srd.readSize(padding_bottom);
                at.y += (offset_y - padding_y);
                break;
            case BOTTOM_LEFT:
                padding_y = srd.readSize(padding_bottom);
                padding_x = srd.readSize(padding_left);
                at.x -= (offset_x - padding_x);
                at.y += (offset_y - padding_y);
                break;
            case BOTTOM_RIGHT:
                padding_y = srd.readSize(padding_bottom);
                padding_x = srd.readSize(padding_right);
                at.x += (offset_x - padding_x);
                at.y += (offset_y - padding_y);
                break;
            case TOP_LEFT:
                padding_y = srd.readSize(padding_top);
                padding_x = srd.readSize(padding_left);
                at.x -= (offset_x - padding_x);
                at.y -= (offset_y - padding_y);
                break;
            case TOP_RIGHT:
                padding_y = srd.readSize(padding_top);
                padding_x = srd.readSize(padding_right);
                at.x += (offset_x - padding_x);
                at.y -= (offset_y - padding_y);
                break;
            case CENTER:
            default:
                break; // nop
        }
        return at;
    }


    @Override
    protected void onPaintChildren(B2RenderThis self) {
        String str = this.text;
        if (str != null) {
            ICanvas can = self.getLocalCanvas();
            Paint p = this.getPaintForText(false);
            PointF at = computeDrawTextAt();
            can.drawText(str, at.x, at.y, p);
        }
        super.onPaintChildren(self);
    }
}
