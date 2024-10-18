package com.github.xushifustudio.libduckeys.instruments.pad2;


import android.graphics.Color;

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

        //   B2Layout la = B2LinearLayout.newInstance(B2LinearLayout.Direction.VERTICAL);
        B2Layout la = B2SimpleLayout.getInstance();
        this.setLayout(la);
    }

    private B2Style getButtonStyle() {
        B2Style st = new B2Style();
        st.textColor = Color.WHITE;
        st.backgroundColor = Color.GRAY;
        st.fontSize = 50;
        return st;
    }

}
