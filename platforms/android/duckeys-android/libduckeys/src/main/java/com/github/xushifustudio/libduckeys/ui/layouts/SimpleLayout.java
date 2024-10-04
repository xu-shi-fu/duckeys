package com.github.xushifustudio.libduckeys.ui.layouts;

import com.github.xushifustudio.libduckeys.ui.boxes.Container;
import com.github.xushifustudio.libduckeys.ui.boxes.Layout;
import com.github.xushifustudio.libduckeys.ui.boxes.LayoutContext;

public class SimpleLayout implements Layout {
    @Override
    public void apply(LayoutContext lc, Container can) {
        int w = can.width;
        int h = can.height;
        can.forChildren((child) -> {
            child.x = 0;
            child.y = 0;
            child.width = w;
            child.height = h;
        });
    }

    private static final SimpleLayout theInst = new SimpleLayout();

    public static SimpleLayout getInstance() {
        return theInst;
    }
}
