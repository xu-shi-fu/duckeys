package com.github.xushifustudio.libduckeys.ui.layouts;

import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutParams;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;

import java.util.ArrayList;
import java.util.List;

public class B2LinearLayout implements B2Layout {

    private final Direction direction;

    private B2LinearLayout(Direction dir) {
        this.direction = dir;
    }


    public enum Direction {
        VERTICAL, HORIZONTAL
    }


    public static B2LinearLayout newInstance(Direction dir) {
        return new B2LinearLayout(dir);
    }


    @Override
    public void apply(B2Container container, B2LayoutThis self) {

        // no impl : use as simple
        List<B2View> all = container.listChildren();
        for (B2View child : all) {
            child.x = 0;
            child.y = 0;
            child.width = container.width;
            child.height = container.height;
        }
    }
}
