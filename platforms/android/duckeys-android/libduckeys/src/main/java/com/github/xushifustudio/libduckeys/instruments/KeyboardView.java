package com.github.xushifustudio.libduckeys.instruments;

import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.instruments.control.ChordDetector;
import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.ui.box2.B2Container;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchContext;
import com.github.xushifustudio.libduckeys.ui.box2.B2OnTouchThis;

public class KeyboardView extends B2Container {

    private final InstrumentContext ic;

    public KeyboardView(InstrumentContext inst_ctx) {
        this.ic = inst_ctx;
    }


    private final void releaseAllPressedKey(B2OnTouchThis self) {
        boolean req_flush = false;
        Keyboard kb = ic.getKeyboard();
        int count = kb.countKeyState();
        for (int i = 0; i < count; ++i) {
            KeyState ks = kb.getKeyState(i);
            ks.want = false;
            if (ks.want != ks.have) {
                req_flush = true;
            }
        }
        if (req_flush) {
            kb.flush();
        }
    }


    @Override
    protected void onTouchBefore(B2OnTouchThis self) {
        if (self.context.action == B2OnTouchContext.ACTION_DOWN) {
            // 先释放所有还没 note-off 的按键
            releaseAllPressedKey(self);
        }
        super.onTouchBefore(self);
    }

    @Override
    protected void onTouchAfter(B2OnTouchThis self) {
        super.onTouchAfter(self);
        Keyboard kb = ic.getKeyboard();
        if (kb != null) {
            kb.flush();
        }
        if (self.context.action == B2OnTouchContext.ACTION_POINTER_DOWN) {
            this.detectChord(kb);
        }
    }

    private void detectChord(Keyboard kb) {
        if (kb == null) {
            return;
        }
        ChordDetector cd = new ChordDetector(ic);
        Chord ch = cd.detect(kb);
        // if (ch == null) {
        //   return;
        // }
        //   Log.i(DuckLogger.TAG, "detected chord: " + ch);
        ic.getChordManager().input.setWant(ch);
    }
}
