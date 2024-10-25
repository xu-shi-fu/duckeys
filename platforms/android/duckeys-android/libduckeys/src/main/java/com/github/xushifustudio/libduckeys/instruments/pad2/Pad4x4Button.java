package com.github.xushifustudio.libduckeys.instruments.pad2;

import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.KeyButton;
import com.github.xushifustudio.libduckeys.instruments.KeyState;
import com.github.xushifustudio.libduckeys.midi.ChordNote;
import com.github.xushifustudio.libduckeys.midi.ModeNote;
import com.github.xushifustudio.libduckeys.ui.box2.B2LayoutThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;

public class Pad4x4Button extends KeyButton {

    public Pad4x4Button(InstrumentContext ctx) {
        super(ctx);
    }

    @Override
    protected void onLayoutBefore(B2LayoutThis self) {
        super.onLayoutBefore(self);
        KeyState ks = this.getKeyState();
        if (ks != null) {
            this.setText(ks.note.name);
        }
    }

    @Override
    public B2State getState() {

        /*
        KeyState ks = this.getKeyState();
        if (ks != null) {
            if (ks.have) {
                return B2State.PRESSED;
            }
            if (ks.chord != ChordNote.NONE) {
                return B2State.CUSTOM2;  // CUSTOM2 表示和弦内音
            }
            if (ks.mode != ModeNote.NONE) {
                return B2State.CUSTOM1;  // CUSTOM1 表示调内音
            }
        }
        return B2State.NORMAL;
        */

        return super.getState();
    }
}
