package com.github.xushifustudio.libduckeys.instruments.pad2;

import com.github.xushifustudio.libduckeys.ui.box2.B2State;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Button;

public class PadBankButton extends B2Button {

    public PadBankButton() {
    }


    @Override
    public B2State getState() {
        if (this.pressed) {
            return B2State.PRESSED;
        }
        if (this.selected) {
            return B2State.SELECTED;
        }
        return super.getState();
    }
}
