package com.github.xushifustudio.libduckeys.ui.box2;

import android.graphics.RectF;

public class B2LayoutParams {


    public static final int SIZE_AS_WEIGHT = 0;
    public static final int SIZE_AS_PARENT = -1;
    public static final int SIZE_AS_CONTENT = -2;
    public static final int SIZE_AS_AUTO = -3;


    public int layoutWidth;  // 用于排版：[SIZE_AS_WEIGHT|SIZE_AS_PARENT|SIZE_AS_CONTENT|+NUM]
    public int layoutHeight; // 用于排版：[SIZE_AS_WEIGHT|SIZE_AS_PARENT|SIZE_AS_CONTENT|+NUM]
    public int layoutWeight; // 用于排版：大小的权重


    public final RectF border;
    public final RectF margin;
    public final RectF padding;

    public B2LayoutParams() {
        this.border = new RectF();
        this.margin = new RectF();
        this.padding = new RectF();
    }

}
