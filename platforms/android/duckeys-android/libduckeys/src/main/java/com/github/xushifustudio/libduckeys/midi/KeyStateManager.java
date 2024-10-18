package com.github.xushifustudio.libduckeys.midi;

import com.github.xushifustudio.libduckeys.context.BaseLife;

public class KeyStateManager extends BaseLife {

    private final KeyState mKeyNA;
    private final KeyState[] mKeys;
    private final MidiEventRT mMeRT;

    public KeyStateManager() {
        mKeys = initKeys();
        mMeRT = new MidiEventRT("owner:KeyStateManager");
        mKeyNA = initKeyNA();
    }


    public MidiEventRT getRT() {
        return mMeRT;
    }


    public void write(MidiNoteMessage event) {

        if (!event.on) {
            event.velocity = 0;
        }

        KeyState state = findByIndex(event.note);
        if (state.on == event.on) {
            return; // ignore
        }

        state.on = event.on;
        state.velocity = event.velocity;

        MidiEvent me = MidiNoteMessage.toMidiEvent(event);
        mMeRT.dispatch(me);
    }

    private KeyState initKeyNA() {
        Note note = Note.forNote(0);
        KeyState state = new KeyState();
        state.note = note;
        state.noteValue = (byte) note.midi;
        state.velocity = 0;
        state.on = false;
        return state;
    }


    private KeyState[] initKeys() {
        final int count = 128;
        KeyState[] list = new KeyState[count];
        for (int i = 0; i < count; i++) {
            Note note = Note.forNote(i);
            KeyState state = new KeyState();
            state.note = note;
            state.noteValue = (byte) note.midi;
            state.velocity = 0;
            state.on = false;
            list[i] = state;
        }
        return list;
    }


    private final static class KeyState {
        Note note;
        boolean on;
        byte noteValue;
        byte velocity;
    }

    private KeyState findByIndex(int index) {
        KeyState[] all = mKeys;
        if (0 <= index && index < all.length) {
            return all[index];
        }
        return mKeyNA;
    }
}
