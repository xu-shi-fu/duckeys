package com.github.xushifustudio.libduckeys.instruments.pad2;

import android.graphics.Color;

import com.github.xushifustudio.libduckeys.instruments.SensorMonitorView;
import com.github.xushifustudio.libduckeys.ui.box2.B2Align;
import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutParams;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.box2.B2Style;
import com.github.xushifustudio.libduckeys.ui.box2.B2StyleBuilder;
import com.github.xushifustudio.libduckeys.ui.box2.B2View;
import com.github.xushifustudio.libduckeys.ui.box2.B2ViewAbs;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Button;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2RectView;
import com.github.xushifustudio.libduckeys.ui.layouts.B2GridLayout;
import com.github.xushifustudio.libduckeys.ui.layouts.B2LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class SimplePadView extends B2Container {

    private final List<B2Button> mBankButtons;
    private final PadContext mPadContext;

    public SimplePadView(PadContext pc) {

        mBankButtons = new ArrayList<>();
        mPadContext = pc;

        Pad4x4View view4x4 = new Pad4x4View(pc);
        B2RectView rect1 = new SensorMonitorView(pc);
        B2RectView rect2 = new B2RectView();
        B2Container bank_bar = createBankSelectBar();

        rect1.setStyle(this.getStyleForRect1());
        rect2.setStyle(this.getStyleForRect2());
        view4x4.setStyle(this.getStyleForPad4x4());

        this.add(bank_bar);
        this.add(rect1);
        this.add(view4x4);
        this.add(rect2);

        B2LinearLayout la = B2LinearLayout.newInstance(B2LinearLayout.Direction.VERTICAL);
        this.setLayout(la);
    }


    private class MyBankButtonOnClickListener implements B2View.OnClickListener {

        private final int mNote0;

        MyBankButtonOnClickListener(int note0) {
            this.mNote0 = note0;
        }

        private void resetAllButtons() {
            for (B2Button btn : mBankButtons) {
                btn.selected = false;
            }
        }

        private void setPadNote0() {
            mPadContext.note0 = this.mNote0;
        }

        @Override
        public void onClick(B2View view) {
            this.resetAllButtons();
            view.selected = true;
            this.setPadNote0();
        }
    }


    private B2Container createBankSelectBar() {

        //B2LinearLayout la = B2LinearLayout.newInstance(B2LinearLayout.Direction.HORIZONTAL);
        B2GridLayout la = B2GridLayout.newInstance(8, 1);

        B2Style style_bank_btn = getStyleForBankButton();
        B2Style style_bank_bar = getStyleForBankBar();
        B2Container bar = new B2Container();

        for (int i = 0; i < 8; i++) {
            B2Button btn = new B2Button();
            btn.setText("bank" + i);
            btn.setStyle(style_bank_btn);
            btn.setOnClickListener(new MyBankButtonOnClickListener(i * 12));
            bar.add(btn);
            mBankButtons.add(btn);
        }

        bar.setStyle(style_bank_bar);
        bar.setLayout(la);
        return bar;
    }

    private B2Style getStyleForBankButton() {
        B2StyleBuilder b = new B2StyleBuilder();

        // normal

        b.setState(B2State.NORMAL);

        b.putColor(B2Style.background_color, Color.GRAY);
        b.putColor(B2Style.text_color, Color.DKGRAY);
        b.putColor(B2Style.border_color, Color.GRAY);

        b.putSize(B2Style.margin, 10);
        b.putSize(B2Style.padding, 5);
        b.putSize(B2Style.font_size, 23);
        b.putSize(B2Style.border_radius, 8);
        b.putSize(B2Style.border_width, 2);

        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_CONTENT);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_CONTENT);
        b.putInt(B2Style.layout_weight, 0);

        b.putAlign(B2Style.align, B2Align.CENTER);

        // pressed

        b.setState(B2State.PRESSED);
        b.putColor(B2Style.text_color, Color.YELLOW);
        b.putSize(B2Style.border_width, 5);

        // selected

        b.setState(B2State.SELECTED);
        b.putColor(B2Style.text_color, Color.WHITE);
        b.putSize(B2Style.border_width, 5);

        return b.create();
    }


    private B2Style getStyleForBankBar() {
        B2StyleBuilder b = new B2StyleBuilder();

        b.putColor(B2Style.background_color, Color.rgb(100, 100, 200));

        b.putSize(B2Style.padding, 5);

        b.putInt(B2Style.layout_height, 100);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_weight, 0);

        return b.create();
    }


    private B2Style getStyleForRect1() {
        B2StyleBuilder b = new B2StyleBuilder();
        b.putColor(B2Style.background_color, Color.BLUE);
        b.putInt(B2Style.layout_weight, 2);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_WEIGHT);
        return b.create();
    }

    private B2Style getStyleForRect2() {
        B2StyleBuilder b = new B2StyleBuilder();
        b.putColor(B2Style.background_color, Color.rgb(33, 33, 33));
        b.putInt(B2Style.layout_weight, 1);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_WEIGHT);
        return b.create();
    }

    private B2Style getStyleForPad4x4() {
        B2StyleBuilder b = new B2StyleBuilder();
        b.putInt(B2Style.layout_weight, 4);
        b.putInt(B2Style.layout_width, B2LayoutParams.SIZE_AS_PARENT);
        b.putInt(B2Style.layout_height, B2LayoutParams.SIZE_AS_WEIGHT);
        return b.create();
    }

}
