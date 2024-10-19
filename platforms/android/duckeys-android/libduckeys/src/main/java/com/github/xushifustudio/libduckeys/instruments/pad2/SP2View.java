package com.github.xushifustudio.libduckeys.instruments.pad2;


import android.graphics.Color;

import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Button;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Containers;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Space;
import com.github.xushifustudio.libduckeys.ui.layouts.B2LinearLayout;
import com.github.xushifustudio.libduckeys.ui.layouts.B2SimpleLayout;

public class SP2View extends B2Container {

    public SP2View() {

        B2Space space1 = new B2Space();
        B2Space space2 = new B2Space();
        B2Container pad4x4 = B2Containers.createWithGridLayout(4, 4);
        B2Button[] buttons = new B2Button[16];
        B2Style btn_style = getButtonStyle();

        this.add(space1);
        this.add(pad4x4);
        this.add(space2);

        for (int i = 0; i < buttons.length; i++) {
            B2Button btn = new B2Button();
            btn.setText("btn_" + i);
            btn.setStyle(btn_style);
            buttons[i] = btn;
            pad4x4.add(btn);
        }

        this.initButtonsForAlignTest(buttons);


        //   B2Layout la = B2LinearLayout.newInstance(B2LinearLayout.Direction.VERTICAL);
        B2Layout la = B2SimpleLayout.getInstance();
        this.setLayout(la);
    }

    private void initButtonsForAlignTest(B2Button[] buttons) {

        int i = 0;

        initButtonForAlignTest(buttons[i++], B2Align.CENTER, "");

        initButtonForAlignTest(buttons[i++], B2Align.TOP, "");
        initButtonForAlignTest(buttons[i++], B2Align.LEFT, "");
        initButtonForAlignTest(buttons[i++], B2Align.RIGHT, "");
        initButtonForAlignTest(buttons[i++], B2Align.BOTTOM, "");


        initButtonForAlignTest(buttons[i++], B2Align.BOTTOM_LEFT, "BL");
        initButtonForAlignTest(buttons[i++], B2Align.BOTTOM_RIGHT, "BR");
        initButtonForAlignTest(buttons[i++], B2Align.TOP_LEFT, "TL");
        initButtonForAlignTest(buttons[i++], B2Align.TOP_RIGHT, "TR");

    }

    private void initButtonForAlignTest(B2Button btn, B2Align align, String text) {

        int len = 0;
        if (text != null) {
            len = text.length();
        }
        if (len == 0) {
            text = align.toString();
        }


        B2Style st1 = btn.getStyle(true);
        B2Style st2 = new B2Style(st1);

        st2.align = align;
        btn.setText(text);
        btn.setStyle(st2);
    }


    private B2Style getButtonStyle() {
        B2Style st = new B2Style();
        st.textColor = Color.WHITE;
        st.backgroundColor = Color.GRAY;
        st.fontSize = 35;
        return st;
    }

}
