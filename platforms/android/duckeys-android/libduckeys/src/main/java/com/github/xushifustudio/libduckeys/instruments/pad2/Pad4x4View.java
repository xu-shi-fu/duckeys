package com.github.xushifustudio.libduckeys.instruments.pad2;

import android.graphics.Color;

import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.KeyState;
import com.github.xushifustudio.libduckeys.instruments.Keyboard;
import com.github.xushifustudio.libduckeys.instruments.KeyboardView;
import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2StyleBuilder;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Containers;
import com.github.xushifustudio.libduckeys.ui.layouts.B2SimpleLayout;

public class Pad4x4View extends KeyboardView {

    private final PadContext context;


    public Pad4x4View(PadContext pc) {
        super(pc);
        this.context = pc;
        this.initLayout();
    }


    private void initLayout() {

        B2Container pad4x4 = B2Containers.createWithGridLayout(4, 4);
        Pad4x4Button[] buttons = new Pad4x4Button[16];
        B2Style btn_style = getButtonStyle();
        int note0 = this.context.note0;

        this.add(pad4x4);

        for (int i = 0; i < buttons.length; i++) {
            Pad4x4Button btn = new Pad4x4Button(context);
            btn.setText("btn_" + i);
            btn.setStyle(btn_style);
            buttons[i] = btn;
            pad4x4.add(btn);
            btn.setKeyState(this.getKeyStateByIndex(i + note0));
        }

        B2Layout la = B2SimpleLayout.getInstance();
        this.setLayout(la);
    }


    private KeyState getKeyStateByIndex(int i) {
        Keyboard kb = context.getKeyboard();
        return kb.getKeyState(i);
    }


    private B2Style getButtonStyle() {
        B2StyleBuilder stb = new B2StyleBuilder();

        /////////////////////////////////////
        stb.setState(B2State.NORMAL);

        stb.putColor(B2Style.text_color, Color.rgb(66, 66, 66));
        stb.putColor(B2Style.background_color, Color.rgb(233, 233, 233));
        stb.putSize(B2Style.font_size, 36);

        stb.putSize(B2Style.padding, 20);

        stb.putColor(B2Style.border_color, Color.DKGRAY);
        stb.putSize(B2Style.border_width, 20);

        stb.putSize(B2Style.border_radius_x, 20);
        stb.putSize(B2Style.border_radius_y, 20);

        /////////////////////////////////////
        stb.setState(B2State.PRESSED);

        stb.putSize(B2Style.font_size, 35);
        stb.putColor(B2Style.background_color, Color.rgb(250, 128, 128));
        // stb.putColor(B2Style.border_color, Color.BLUE);

        return stb.create();
    }

    @Override
    protected void onTouchAfter(B2OnTouchThis self) {
        super.onTouchAfter(self);
    }
}
