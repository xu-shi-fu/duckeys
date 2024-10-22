package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;
import com.github.xushifustudio.libduckeys.ui.box2.B2RenderThis;
import com.github.xushifustudio.libduckeys.ui.elements.b2.B2Button;

public class KeyButton extends B2Button {

    public KeyState keyState;
    private InstrumentContext ic;

    public KeyButton(InstrumentContext inst_ctx) {
        this.ic = inst_ctx;
    }


    @Override
    protected void onTouchBefore(B2OnTouchThis self) {

        // super.onTouchBefore(self);

        int action = self.context.action;
        switch (action) {
            case B2OnTouchContext.ACTION_DOWN:
            case B2OnTouchContext.ACTION_POINTER_DOWN:
            case B2OnTouchContext.ACTION_MOVE:
                this.setNoteOn(this.keyState, true);
                break;
            case B2OnTouchContext.ACTION_UP:
            case B2OnTouchContext.ACTION_POINTER_UP:
                this.setNoteOn(this.keyState, false);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onTouchChildren(B2OnTouchThis self) {
        // super.onTouchChildren(self);
    }

    @Override
    protected void onTouchAfter(B2OnTouchThis self) {
        // super.onTouchAfter(self);
    }

    @Override
    protected void onPaintBefore(B2RenderThis self) {
        this.loadKeyState();
        super.onPaintBefore(self);
    }

    private void loadKeyState() {
        KeyState ks = this.getKeyState();
        if (ks == null) {
            return;
        }
        this.pressed = ks.have;
    }

    private void setNoteOn(KeyState ks, boolean on) {
        if (ks == null) {
            return;
        }
        ks.want = on;
    }

    public KeyState getKeyState() {
        return keyState;
    }

    public void setKeyState(KeyState keyState) {
        this.keyState = keyState;
    }
}
