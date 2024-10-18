package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Note;

public class KeyStateManager {

    private final KeyState[] mKeys;

    public KeyStateManager() {
        mKeys = initKeys();
    }

    private KeyState[] initKeys() {
        Note[] src = Note.listAll();
        KeyState[] dst = new KeyState[src.length];
        for (int i = 0; i < src.length; i++) {
            Note n = src[i];
            dst[i] = new KeyState(n);
        }
        return dst;
    }

}
