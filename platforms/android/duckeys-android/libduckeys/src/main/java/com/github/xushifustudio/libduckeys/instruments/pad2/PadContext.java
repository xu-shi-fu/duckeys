package com.github.xushifustudio.libduckeys.instruments.pad2;

import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.Keyboard;

public class PadContext {

    public final Keyboard keyboard;
    public final InstrumentContext ic;
    public int note0; // 当前 pad 上的最低音

    public PadContext(InstrumentContext ic1) {
        this.keyboard = new Keyboard(ic1);
        this.ic = ic1;
        this.note0 = 36;
    }
}
