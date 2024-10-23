package com.github.xushifustudio.libduckeys.instruments.pad2;

import android.graphics.Color;

import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutParams;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2StyleBuilder;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2RectView;
import com.github.xushifustudio.libduckeys.ui.layouts.B2LinearLayout;

public class SimplePadView extends B2Container {

    public SimplePadView(PadContext pc) {

        Pad4x4View view4x4 = new Pad4x4View(pc);
        B2RectView rect1 = new B2RectView();
        B2RectView rect2 = new B2RectView();

        rect1.setStyle(this.getStyleForRect1());
        rect1.setStyle(this.getStyleForRect2());
        view4x4.setStyle(this.getStyleForPad4x4());

        this.add(rect1);
        this.add(view4x4);
        this.add(rect2);

        B2LinearLayout la = B2LinearLayout.newInstance(B2LinearLayout.Direction.VERTICAL);
        this.setLayout(la);
    }

    private B2Style getStyleForRect1() {
        B2StyleBuilder b = new B2StyleBuilder();
        b.putColor(B2Style.background_color, Color.BLUE);
        b.putInt(B2Style.layout_weight, 1);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_WEIGHT);
        return b.create();
    }

    private B2Style getStyleForRect2() {
        B2StyleBuilder b = new B2StyleBuilder();
        b.putColor(B2Style.background_color, Color.GREEN);
        b.putInt(B2Style.layout_weight, 1);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_WEIGHT);
        return b.create();
    }

    private B2Style getStyleForPad4x4() {
        B2StyleBuilder b = new B2StyleBuilder();
        b.putInt(B2Style.layout_weight, 2);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_WEIGHT);
        return b.create();
    }

}
