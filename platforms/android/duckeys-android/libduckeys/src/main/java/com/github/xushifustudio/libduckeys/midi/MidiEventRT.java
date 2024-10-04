package com.github.xushifustudio.libduckeys.midi;

public class MidiEventRT implements MidiEventDispatcher, MidiEventHandler {

    private MidiEventHandler mRx;
    private MidiEventDispatcher mTx;
    private final NopMERT nop = new NopMERT();

    public MidiEventRT() {
        mTx = nop;
        mRx = nop;
    }

    public final void setRx(MidiEventHandler rx) {
        if (rx == null) rx = nop;
        mRx = rx;
    }

    public final void setTx(MidiEventDispatcher tx) {
        if (tx == null) tx = nop;
        mTx = tx;
    }

    @Override
    public void dispatch(MidiEvent me) {
        mTx.dispatch(me);
    }

    @Override
    public void handle(MidiEvent me) {
        mRx.handle(me);
    }
}
