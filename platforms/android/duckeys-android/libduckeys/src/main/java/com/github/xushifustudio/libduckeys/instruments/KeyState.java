package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.ChordNote;
import com.github.xushifustudio.libduckeys.midi.ModeNote;
import com.github.xushifustudio.libduckeys.midi.Note;

public class KeyState {

    public final Keyboard keyboard;
    public final Note note;
    public boolean want;  // 1=note_on, 0=note_off
    public boolean have; // 1=note_on, 0=note_off
    public byte velocity;

    public ChordNote chord; // 和弦音
    public ModeNote mode; // 调式音


    public KeyState(Keyboard kb, Note n) {
        this.keyboard = kb;
        this.note = n;
        this.velocity = 100;
    }
}
