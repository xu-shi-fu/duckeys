package com.github.xushifustudio.libduckeys.ui.layouts;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.boxes.Container;
import com.github.xushifustudio.libduckeys.ui.boxes.Layout;
import com.github.xushifustudio.libduckeys.ui.boxes.LayoutContext;

public class B2NopLayout implements B2Layout {


    private static final B2NopLayout theInst = new B2NopLayout();

    public static B2NopLayout getInstance() {
        return theInst;
    }

    @Override
    public void apply(B2LayoutThis self, B2Container container) {

    }
}
