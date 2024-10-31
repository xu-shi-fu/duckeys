package com.github.xushifustudio.libduckeys.instruments;

import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.ChordPattern;
import com.github.xushifustudio.libduckeys.midi.Note;

public class ChordManager {

    public final Pair output; // 当前显示(输出)的和弦
    public final Pair input; // 当前键入（输入）的和弦


    public ChordManager() {
        this.input = new Pair();
        this.output = new Pair();
    }


    public static class Pair {
        private Chord want;// 需要的和弦
        private Chord have; // 当前的和弦

        Pair() {
        }

        public Chord getWant() {
            return want;
        }

        public void setWant(Chord want) {
            this.want = want;
        }

        public Chord getHave() {
            return have;
        }

        public void setHave(Chord have) {
            this.have = have;
        }
    }


    public void apply(InstrumentContext ic, Pair p, boolean reload) {
        if (p == null || ic == null) {
            return;
        }
        if (!reload) {
            if (Chord.equal(p.want, p.have)) {
                return;
            }
        }
        NotePatternManager npm = new NotePatternManager(p.want);
        npm.applyTo(ic.getKeyboard());
        p.have = p.want;
    }

}
