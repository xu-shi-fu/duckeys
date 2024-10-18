package com.github.xushifustudio.libduckeys.ui.layouts;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;

public class B2LinearLayout implements B2Layout {

    private Direction direction;

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
    public void apply(B2LayoutThis self, B2Container container) {

    }
}
