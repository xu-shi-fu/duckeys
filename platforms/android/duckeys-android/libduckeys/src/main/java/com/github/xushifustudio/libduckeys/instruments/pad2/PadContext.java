package com.github.xushifustudio.libduckeys.instruments.pad2;

import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.Keyboard;

public class PadContext extends InstrumentContext {

    public int note0; // 当前 pad 上的最低音

    public PadContext() {
        this.note0 = 36;
    }
}
