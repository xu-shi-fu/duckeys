package com.github.xushifustudio.libduckeys.ui.elements.b2;


import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.SizeF;

import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.styles.B2StyleReader;

public class B2TextView extends B2RectView {

    private String text;
    private MyStyleCache cachedStyle;

    public B2TextView() {
    }


    private static class MyStyleState {

        Paint paint;
        B2Align align;
        final RectF padding;

        public MyStyleState() {
            this.align = B2Align.LEFT;
            this.padding = new RectF();
        }
    }


    private static class MyStyleCache {

        final long revision;

        final MyStyleState normal;
        final MyStyleState selected;
        final MyStyleState pressed;

        public MyStyleCache(long rev) {
            this.revision = rev;
            this.normal = new MyStyleState();
            this.pressed = new MyStyleState();
            this.selected = new MyStyleState();
        }
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    private MyStyleCache getStyleCache() {
        MyStyleCache older = this.cachedStyle;
        B2Style sty = this.getStyle(true);
        if (older != null && sty != null) {
            if (older.revision == sty.revision()) {
                return older;
            }
        }
        MyStyleCache sc = this.loadStyleCache();
        this.cachedStyle = sc;
        return sc;
    }

    private MyStyleCache loadStyleCache() {
        B2Style sty = this.getStyle(true);
        MyStyleCache sc = new MyStyleCache(sty.revision());
        B2StyleReader srd = new B2StyleReader(sty);
        this.loadStyleWithState(sc.normal, srd, B2State.NORMAL);
        this.loadStyleWithState(sc.pressed, srd, B2State.PRESSED);
        this.loadStyleWithState(sc.selected, srd, B2State.SELECTED);
        return sc;
    }

    private void loadStyleWithState(MyStyleState ss, B2StyleReader srd, B2State state) {

        srd.setState(state);

        // padding

        final String[] keys_padding_top = {B2Style.padding_top, B2Style.padding};
        final String[] keys_padding_left = {B2Style.padding_left, B2Style.padding};
        final String[] keys_padding_right = {B2Style.padding_right, B2Style.padding};
        final String[] keys_padding_bottom = {B2Style.padding_bottom, B2Style.padding};

        ss.padding.top = srd.readSize(keys_padding_top);
        ss.padding.left = srd.readSize(keys_padding_left);
        ss.padding.right = srd.readSize(keys_padding_right);
        ss.padding.bottom = srd.readSize(keys_padding_bottom);

        // color, size, align

        int text_color = srd.readColor(B2Style.text_color);
        float font_size = srd.readSize(B2Style.font_size);
        B2Align align = srd.readAlign(B2Style.align);

        Paint paint = new Paint();
        paint.setColor(text_color);
        paint.setTextSize(font_size);
        paint.setStyle(Paint.Style.FILL);
        // paint.setStrokeWidth(1);

        ss.align = align;
        ss.paint = paint;
    }


    private MyStyleState getCurrentStyleByState(MyStyleCache sc) {
        if (sc == null) {
            return null;
        }
        if (this.pressed) {
            return sc.pressed;
        }
        if (this.selected) {
            return sc.selected;
        }
        return sc.normal;
    }


    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        SizeF size = this.computeContentSize();
        this.contentHeight = (int) size.getHeight();
        this.contentWidth = (int) size.getWidth();
    }

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);
    }

    private SizeF computeContentSize() {
        String str = this.text;
        if (str == null) {
            return new SizeF(0, 0);
        }
        try {
            MyStyleCache style_cache = this.getStyleCache();
            MyStyleState ss = style_cache.normal;
            Paint paint = ss.paint;
            RectF padding = ss.padding;
            Rect rect = new Rect();
            paint.getTextBounds(str, 0, str.length(), rect);
            float h = rect.height() + padding.top + padding.bottom;
            float w = rect.width() + padding.left + padding.right;
            return new SizeF(w, h);
        } catch (Exception e) {
            return new SizeF(0, 0);
        }
    }


    private PointF computeDrawTextAt() {
        String str = this.text;
        if (str == null) {
            return null;
        }
        MyStyleCache style_cache = this.getStyleCache();
        MyStyleState ss = this.getCurrentStyleByState(style_cache);
        Paint paint = ss.paint;
        B2Align align = ss.align;
        RectF padding = ss.padding;

        Rect text_size = new Rect();
        paint.getTextBounds(str, 0, str.length(), text_size);

        float box_w = this.width;
        float box_h = this.height;
        float str_w = text_size.width();
        float str_h = text_size.height();
        float offset_y = (box_h / 2) - (str_h / 2);
        float offset_x = (box_w / 2) - (str_w / 2);

        PointF at = new PointF();
        at.x = offset_x;
        at.y = offset_y + str_h;

        switch (align) {
            case TOP:
                at.y -= (offset_y - padding.top);
                break;
            case LEFT:
                at.x -= (offset_x - padding.left);
                break;
            case RIGHT:
                at.x += (offset_x - padding.right);
                break;
            case BOTTOM:
                at.y += (offset_y - padding.bottom);
                break;
            case BOTTOM_LEFT:
                at.x -= (offset_x - padding.left);
                at.y += (offset_y - padding.bottom);
                break;
            case BOTTOM_RIGHT:
                at.x += (offset_x - padding.right);
                at.y += (offset_y - padding.bottom);
                break;
            case TOP_LEFT:
                at.x -= (offset_x - padding.left);
                at.y -= (offset_y - padding.top);
                break;
            case TOP_RIGHT:
                at.x += (offset_x - padding.right);
                at.y -= (offset_y - padding.top);
                break;
            case CENTER:
            default:
                break; // nop
        }
        return at;
    }


    @Override
    protected void onPaintChildren(B2RenderThis self) {
        super.onPaintChildren(self);
        String str = this.text;
        if (str == null) {
            return;
        }
        PointF at = computeDrawTextAt();
        if (at == null) {
            return;
        }
        ICanvas can = self.getLocalCanvas();
        MyStyleCache style_cache = this.getStyleCache();
        MyStyleState ss = this.getCurrentStyleByState(style_cache);
        can.drawText(str, at.x, at.y, ss.paint);
    }
}
