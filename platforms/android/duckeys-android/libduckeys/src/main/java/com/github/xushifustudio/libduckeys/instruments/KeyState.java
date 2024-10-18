package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Note;

public class KeyState {

    public final Note note;
    public boolean pressedOlder;
    public boolean pressedNewer;

    public KeyState(Note n) {
        this.note = n;
    }

    public void setPressed(boolean pressed) {
        this.pressedNewer = pressed;
    }

    public void flush(InstrumentContext ic) {
        if (pressedNewer == pressedOlder) {
            return;
        }

        // ic.getMert().dispatch(  );

        pressedOlder = pressedNewer;
    }

}
