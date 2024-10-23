package com.github.xushifustudio.libduckeys.ui.layouts;

import android.graphics.RectF;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;

import java.util.List;

public class B2SimpleLayout implements B2Layout {


    private static final B2SimpleLayout theInst = new B2SimpleLayout();

    public static B2SimpleLayout getInstance() {
        return theInst;
    }

    @Override
    public void apply( B2Container container, B2LayoutThis self ) {
        List<B2View> clist = container.listChildren();
        for (B2View child : clist) {
            child.x = 0;
            child.y = 0;
            child.width = container.width;
            child.height = container.height;
        }
    }

}
