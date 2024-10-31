package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.ChordNote;
import com.github.xushifustudio.libduckeys.midi.ModeNote;
import com.github.xushifustudio.libduckeys.midi.Note;
import com.github.xushifustudio.libduckeys.ui.box2.B2State;

public class KeyState {


    public static final B2State STATE_MODE_N1 = B2State.CUSTOM1; // 主音
    public static final B2State STATE_MODE_X = B2State.CUSTOM2; // 调内音
    public static final B2State STATE_CHORD_ROOT = B2State.CUSTOM3; // 和弦根音
    public static final B2State STATE_CHORD_X = B2State.CUSTOM4; // 和弦音


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
