package com.bitwormhole.libduckeys.midi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Note {

    public final char name; // 按键的音名，取值为(C,D,E,F,G,A,B)其中之一
    public final boolean sharp;  // 是否为黑键 (指带 # 记号)
    public final int group; // 所在 group 的 ID
    public final String fullname; // 音符的全名，例如："F#-2"
    public final int index;  // 在 midi[128] 音符数组中的索引

    private Note(Builder b) {
        String simpleName = String.valueOf(b.name).toUpperCase();
        name = b.name;
        sharp = b.sharp;
        group = b.group;
        index = Convert.computeNoteIndex(b);
        fullname = simpleName + (sharp ? "#" : "") + b.group;
    }

    @NonNull
    @Override
    public String toString() {
        return fullname;
    }

    @Override
    public int hashCode() {
        String str = this.toString();
        return str.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof Note) {
            Note o1 = this;
            Note o2 = (Note) other;
            return o1.index == o2.index;
        }
        return false;
    }

    private static final Note[] cache = new Note[128];
    private static final Note theErrNote = makeErrNote();


    private static Note makeErrNote() {
        Builder b = new Builder();
        return b.create();
    }

    private final static class Builder {

        public char name;
        public boolean sharp;
        public int group;

        public Note create() {
            return new Note(this);
        }
    }

    private final static class Convert {

        static char charAt(String str, int i) {
            int len = str.length();
            if ((0 <= i) && (i < len)) {
                return str.charAt(i);
            }
            return 'x';
        }

        public static Note parseNote(String name) {
            if (name == null) {
                return theErrNote;
            }
            String name2 = name.toUpperCase();
            char ch0 = charAt(name2, 0);
            char ch1 = charAt(name2, 1);
            Builder b = new Builder();
            b.name = ch0;
            b.sharp = (ch1 == '#');
            try {
                String group = name.substring(b.sharp ? 2 : 1);
                b.group = Integer.parseInt(group);
            } catch (Exception e) {
                b.group = 0;
            }
            return b.create();
        }

        static final int theGroupShift = 2;

        private static final String[] theGroupKeys = {
                "C", "C#", "D", "D#", "E",
                "F", "F#", "G", "G#", "A", "A#", "B"
        };

        public static Note createNoteWithIndex(int index) {
            if (index < 0 || 127 < index) {
                return theErrNote;
            }
            final int nKey = index % 12;
            final int nGroup = index / 12;
            String keyName = theGroupKeys[nKey];
            char ch0 = charAt(keyName, 0);
            char ch1 = charAt(keyName, 1);
            Builder b = new Builder();
            b.name = ch0;
            b.sharp = (ch1 == '#');
            b.group = nGroup - theGroupShift;
            return b.create();
        }

        public static int computeNoteIndex(Builder b) {
            int base = (b.group + theGroupShift) * 12;
            int offset = 0;
            switch (b.name) {
                case 'C':
                    offset = 0;
                    break;
                case 'D':
                    offset = 2;
                    break;
                case 'E':
                    offset = 4;
                    break;
                case 'F':
                    offset = 5;
                    break;
                case 'G':
                    offset = 7;
                    break;
                case 'A':
                    offset = 9;
                    break;
                case 'B':
                    offset = 11;
                    break;
                default:
                    break;
            }
            if (b.sharp) {
                offset++;
            }
            return base + offset;
        }
    }


    private static Note load(Note want) {
        final int min = 0;
        final int max = cache.length - 1;
        if (want == null) {
            want = theErrNote;
        }
        int index = want.index;
        if ((min <= index) && (index <= max)) {
            // good, NOP
        } else {
            index = min;
            want = theErrNote;
        }
        Note have = cache[index];
        if (have == null) {
            have = want;
            cache[index] = want;
        }
        return have;
    }


    public static Note forNote(char name, boolean sharp, int group) {
        Builder b = new Builder();
        b.name = name;
        b.sharp = sharp;
        b.group = group;
        return load(b.create());
    }

    public static Note forNote(int index) {
        Note want = Convert.createNoteWithIndex(index);
        return load(want);
    }

    public static Note forNote(String name) {
        Note want = Convert.parseNote(name);
        return load(want);
    }
}
