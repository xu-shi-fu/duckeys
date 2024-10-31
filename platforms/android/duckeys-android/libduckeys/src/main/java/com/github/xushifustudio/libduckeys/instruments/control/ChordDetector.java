package com.github.xushifustudio.libduckeys.instruments.control;

import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.KeyState;
import com.github.xushifustudio.libduckeys.instruments.Keyboard;
import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.Chords;
import com.github.xushifustudio.libduckeys.midi.Note;

import java.util.ArrayList;
import java.util.List;

public class ChordDetector {

    private final InstrumentContext ic;

    public ChordDetector(InstrumentContext _ic) {
        this.ic = _ic;
    }

    public void detect() {
        Keyboard kb = ic.getKeyboard();
        Chord ch = this.detect(kb);
        ic.getChordManager().output.setWant(ch);
    }

    public Chord detect(Keyboard kb) {
        List<Note> list = new ArrayList<>();
        int count = kb.countKeyState();
        for (int i = 0; i < count; i++) {
            KeyState ks = kb.getKeyState(i);
            if (ks.have) {
                list.add(ks.note);
            }
        }
        return Chords.detect(list);
    }

    public InstrumentContext getIC() {
        return ic;
    }
}
