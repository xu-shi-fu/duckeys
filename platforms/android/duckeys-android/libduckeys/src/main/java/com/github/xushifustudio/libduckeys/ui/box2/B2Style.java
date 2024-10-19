package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.Paint;

public class B2Style {

    // colors
    public int color; // default, main, foreground
    public int textColor;
    public int backgroundColor;
    public int borderColor;
    public int colorHover;
    public int colorDisabled;
    public int colorPressed;

    // borders
    public int border;
    public int borderTop;
    public int borderLeft;
    public int borderRight;
    public int borderBottom;

    // margins
    public int margin;
    public int marginTop;
    public int marginLeft;
    public int marginRight;
    public int marginBottom;

    // paddings
    public int padding;
    public int paddingTop;
    public int paddingLeft;
    public int paddingRight;
    public int paddingBottom;

    // font
    public float fontSize;

    // align
    public B2Align align;

    public B2Style() {
    }

    public B2Style(B2Style src) {

        if (src == null) {
            return;
        }

        this.align = src.align;
        this.fontSize = src.fontSize;

        this.color = src.color;
        this.textColor = src.textColor;
        this.borderColor = src.borderColor;
        this.backgroundColor = src.backgroundColor;
        this.colorDisabled = src.colorDisabled;
        this.colorPressed = src.colorPressed;
        this.colorHover = src.colorHover;

        this.border = src.border;
        this.borderTop = src.borderTop;
        this.borderLeft = src.borderLeft;
        this.borderRight = src.borderRight;
        this.borderBottom = src.borderBottom;

        this.margin = src.margin;
        this.marginTop = src.marginTop;
        this.marginLeft = src.marginLeft;
        this.marginRight = src.marginRight;
        this.marginBottom = src.marginBottom;

        this.padding = src.padding;
        this.paddingTop = src.paddingTop;
        this.paddingLeft = src.paddingLeft;
        this.paddingRight = src.paddingRight;
        this.paddingBottom = src.paddingBottom;

    }

    public B2Align getAlign(boolean not_null) {
        B2Align value = this.align;
        if (value == null && not_null) {
            value = B2Align.TOP_LEFT;
        }
        return value;
    }
}
