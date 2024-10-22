package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Note;

public class KeyState {

    public final Keyboard keyboard;
    public final Note note;
    public boolean want;  // 1=key_on, 0=key_off
    public boolean have; // 1=key_on, 0=key_off
    public byte velocity;

    public KeyState(Keyboard kb, Note n) {
        this.keyboard = kb;
        this.note = n;
        this.velocity = 100;
    }
}
