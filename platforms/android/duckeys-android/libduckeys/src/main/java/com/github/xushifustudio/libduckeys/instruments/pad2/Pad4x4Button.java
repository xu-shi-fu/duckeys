package com.github.xushifustudio.libduckeys.instruments.pad2;

import com.github.xushifustudio.libduckeys.instruments.KeyButton;
import com.github.xushifustudio.libduckeys.instruments.KeyState;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;

public class Pad4x4Button extends KeyButton {

    public Pad4x4Button() {
    }

    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        KeyState ks = this.getKeyState();
        if (ks != null) {
            this.setText(ks.note.name);
        }
    }
}
