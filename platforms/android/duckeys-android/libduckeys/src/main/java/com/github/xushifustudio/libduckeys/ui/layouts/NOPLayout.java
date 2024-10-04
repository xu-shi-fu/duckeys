package com.github.xushifustudio.libduckeys.ui.layouts;

import com.github.xushifustudio.libduckeys.ui.boxes.Container;
import com.github.xushifustudio.libduckeys.ui.boxes.Layout;
import com.github.xushifustudio.libduckeys.ui.boxes.LayoutContext;

public class NOPLayout implements Layout {
    @Override
    public void apply(LayoutContext lc, Container can) {

    }

    private static final NOPLayout theInst = new NOPLayout();

    public static NOPLayout getInstance() {
        return theInst;
    }
}
