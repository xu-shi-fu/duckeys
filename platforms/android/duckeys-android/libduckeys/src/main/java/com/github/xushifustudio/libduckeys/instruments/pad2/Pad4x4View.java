package com.github.xushifustudio.libduckeys.instruments.pad2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.KeyState;
import com.github.xushifustudio.libduckeys.instruments.Keyboard;
import com.github.xushifustudio.libduckeys.instruments.KeyboardView;
import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2Layout;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2StyleBuilder;
import com.github.xushifustudio.libduckeys.ui.box2.ICanvas;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Containers;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2RectView;
import com.github.xushifustudio.libduckeys.ui.layouts.B2GridLayout;
import com.github.xushifustudio.libduckeys.ui.layouts.B2SimpleLayout;

public class Pad4x4View extends KeyboardView {

    private final PadContext context;
    private final B2RectView mBackground;
    private Pad4x4Button[] mButtons;
    private MyLoadedKeyStateInfo mLoadedKeyStateInfo;

    public Pad4x4View(PadContext pc) {
        super(pc);
        this.mBackground = new B2RectView();
        this.context = pc;
        this.initLayout();
        this.initMyBackground();
    }

    private static class MyLoadedKeyStateInfo {
        int note0;
    }


    private void initMyBackground() {
        this.mBackground.setStyle(this.getStyle());
    }

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        super.onPaintBefore(self);
        this.checkKeyStateInfo();
    }


    private void initLayout() {

        B2Container pad4x4 = B2Containers.createWithGridLayout(4, 4);
        B2GridLayout grid_la = (B2GridLayout) pad4x4.getLayout();
        grid_la.setOrder(B2GridLayout.ORDER_LEFT_TO_RIGHT | B2GridLayout.ORDER_BOTTOM_TO_TOP | B2GridLayout.ORDER_BOTTOM_OR_TOP_FIRST);


        Pad4x4Button[] buttons = new Pad4x4Button[16];
        B2Style btn_style = getButtonStyle();
        B2Style pad_style = getPadStyle();
        int note0 = this.context.note0;

        for (int i = 0; i < buttons.length; i++) {
            Pad4x4Button btn = new Pad4x4Button(context);
            btn.setText("btn_" + i);
            btn.setStyle(btn_style);
            buttons[i] = btn;
            pad4x4.add(btn);
            btn.setKeyState(this.getKeyStateByIndex(i + note0));
        }
        mButtons = buttons;

        pad4x4.setStyle(pad_style);

        B2Layout la = B2SimpleLayout.getInstance();
        this.setLayout(la);
        this.add(pad4x4);
    }


    private void reloadKeyState(int note0) {
        Pad4x4Button[] buttons = mButtons;
        if (buttons == null) {
            return;
        }
        for (int i = 0; i < buttons.length; i++) {
            KeyState ks = this.getKeyStateByIndex(i + note0);
            Log.i(DuckLogger.TAG, "load_pad_key_note:" + ks.note.name);
            Pad4x4Button btn = buttons[i];
            btn.setText(ks.note.name);
            btn.setKeyState(ks);
        }
    }

    private void checkKeyStateInfo() {
        MyLoadedKeyStateInfo older = this.mLoadedKeyStateInfo;
        if (older != null) {
            if (older.note0 == this.context.note0) {
                return;
            }
        }
        // reload
        MyLoadedKeyStateInfo info = new MyLoadedKeyStateInfo();
        info.note0 = this.context.note0;
        this.reloadKeyState(info.note0);
        this.mLoadedKeyStateInfo = info;
    }


    private KeyState getKeyStateByIndex(int i) {
        Keyboard kb = context.getKeyboard();
        return kb.getKeyState(i);
    }


    private B2Style getPadStyle() {
        B2StyleBuilder stb = new B2StyleBuilder();
        stb.putSize(B2Style.padding, 10);
        stb.putColor(B2Style.background_color, Color.rgb(33, 33, 33));
        return stb.create();
    }


    private B2Style getButtonStyle() {
        B2StyleBuilder stb = new B2StyleBuilder();

        int text_color = Color.rgb(66, 66, 66);
        int btn_bg_color = Color.rgb(233, 233, 233);
        int btn_bg_color_2 = Color.rgb(233, 133, 133);

        /////////////////////////////////////
        stb.setState(B2State.NORMAL);

        stb.putColor(B2Style.text_color, text_color);
        stb.putColor(B2Style.background_color, btn_bg_color);
        stb.putColor(B2Style.border_color, btn_bg_color);

        stb.putSize(B2Style.font_size, 36);
        stb.putSize(B2Style.padding, 20);
        stb.putSize(B2Style.border_width, 5);
        stb.putSize(B2Style.margin, 20);
        stb.putSize(B2Style.border_radius_x, 20);
        stb.putSize(B2Style.border_radius_y, 20);

        stb.putAlign(B2Style.align, B2Align.TOP_LEFT);

        /////////////////////////////////////
        stb.setState(B2State.PRESSED);

        stb.putSize(B2Style.font_size, 33);
        stb.putSize(B2Style.border_width, 2);

        stb.putColor(B2Style.border_color, btn_bg_color_2);
        stb.putColor(B2Style.background_color, btn_bg_color_2);

        return stb.create();
    }

    @Override
    protected void onTouchAfter(B2OnTouchThis self) {
        super.onTouchAfter(self);
    }
}
