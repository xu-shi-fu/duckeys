package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiNoteMessage;
import com.github.xushifustudio.libduckeys.midi.Note;

public class Keyboard {

    private final KeyState[] keys;
    private final KeyState keyEmpty;
    private final InstrumentContext instrumentContext;
    private byte channel;

    public Keyboard(InstrumentContext ic) {
        keys = initKeys();
        keyEmpty = initKeyEmpty();
        instrumentContext = ic;
    }

    private KeyState initKeyEmpty() {
        Note n = Note.forNote(0);
        return new KeyState(this, n);
    }

    private KeyState[] initKeys() {
        Note[] src = Note.listAll();
        KeyState[] dst = new KeyState[src.length];
        for (int i = 0; i < src.length; i++) {
            Note n = src[i];
            dst[i] = new KeyState(this, n);
        }
        return dst;
    }

    private void send(KeyState ks, boolean note_on) {
        if (ks == null) {
            return;
        }
        MidiEventRT rtx = this.instrumentContext.getMert();
        Note note = ks.note;
        if (note == null || rtx == null) {
            return;
        }
        MidiNoteMessage msg = new MidiNoteMessage();
        msg.on = note_on;
        msg.note = (byte) note.midi;
        msg.velocity = note_on ? ks.velocity : 0;
        msg.channel = this.channel;
        MidiEvent event = MidiNoteMessage.toMidiEvent(msg);
        rtx.dispatch(event);
    }

    public int countKeyState() {
        return keys.length;
    }

    public KeyState getKeyState(int index) {
        if (0 <= index && index < keys.length) {
            return keys[index];
        }
        return this.keyEmpty;
    }

    public void flush() {
        for (KeyState ks : this.keys) {
            if (ks.want != ks.have) {
                this.send(ks, ks.want);
                ks.have = ks.want;
            }
        }
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }
}
