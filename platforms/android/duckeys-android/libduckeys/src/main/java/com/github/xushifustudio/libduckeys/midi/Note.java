package com.github.xushifustudio.libduckeys.midi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Note {

    public final char key; // 按键名，取值为(C,D,E,F,G,A,B)其中之一; 与 sharp 字段组合构成音名
    public final boolean sharp;  // 是否为黑键 (指带 # 记号)
    public final int group; // 所在 group 的 ID
    public final int index;  // 在 midi[128] 音符数组中的索引
    public final int midi;    // midi 音符的值 (它的值应该与 index 相同)
    public final float frequency;  // midi 音符的 频率，单位：Hz
    public final String name; // 音符的全名，例如："F#-2"


    private Note(Builder b) {
        String simpleName = String.valueOf(b.key).toUpperCase();
        key = simpleName.charAt(0);
        sharp = b.sharp;
        group = b.group;
        midi = b.value;
        index = b.value;
        frequency = b.frequency;
        name = simpleName + (sharp ? "#" : "") + b.group;
    }


    // public /////////////////////////////////////////////////

    @NonNull
    @Override
    public String toString() {
        return name;
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
            return o1.midi == o2.midi;
        }
        return false;
    }

    public static Note empty() {
        Cache c = getCache();
        return c.findByIndex(0, true);
    }

    public static Note forNote(int index) {
        Cache c = getCache();
        return c.findByIndex(index, true);
    }

    public static Note forNote(String name) {
        Cache c = getCache();
        return c.findByFilter((n) -> {
            return n.name.equals(name);
        }, true);
    }

    public static Note forNote(char key, boolean sharp, int group) {
        if ('a' <= key && key <= 'z') {
            key = (char) (key + 'A' - 'a');
        }
        final char k2 = key;
        Cache c = getCache();
        return c.findByFilter((n) -> {
            return ((k2 == n.key) && (sharp == n.sharp) & (group == n.group));
        }, true);
    }

    public static Note[] listAll() {
        Cache c = getCache();
        Note[] src = c.notes;
        Note[] dst = new Note[src.length];
        for (int i = 0; i < src.length; i++) {
            dst[i] = src[i];
        }
        return dst;
    }

    public static boolean equal(Note a, Note b) {
        if (a == null || b == null) {
            return false;
        }
        return ((a.key == b.key) && (a.sharp == b.sharp) && (a.group == b.group));
    }


    // private /////////////////////////////////////////////////

    private static Cache cache;

    private static Note makeErrNote() {
        Builder b = new Builder();
        b.key = 'z';
        b.frequency = 1;
        return b.create();
    }

    private static Cache getCache() {
        Cache c = cache;
        if (c == null) {
            c = loadCache();
            cache = c;
        }
        return c;
    }

    private static Cache loadCache() {
        Cache c = new Cache();
        c.err = makeErrNote();
        c.notes = getAllNotes();
        return c;
    }

    private final static class Cache {
        private Note[] notes;
        private Note err;

        Note findByFilter(Filter f, boolean no_nil) {
            Note[] all = notes;
            for (Note item : all) {
                if (item == null) {
                    continue;
                }
                if (f.accept(item)) {
                    return item;
                }
            }
            if (no_nil) {
                return err;
            }
            return null;
        }

        Note findByIndex(int index, boolean no_nil) {
            Note[] all = notes;
            Note have = null;
            if (0 <= index && index < all.length) {
                have = all[index];
            }
            if (have == null && no_nil) {
                have = err;
            }
            return have;
        }
    }

    private static interface Filter {
        boolean accept(Note n);
    }

    private final static class Builder {

        public char key;
        public boolean sharp;
        public int group;
        public int value;
        public float frequency;

        public Note create() {
            return new Note(this);
        }
    }


    private final static class ShiftNoteGroupBuilder {

        private final Note[] g0;
        private final Map<String, Note> table; // map full-name to note

        public ShiftNoteGroupBuilder(Note[] ref) {
            this.g0 = ref;
            this.table = new HashMap<>();
        }

        // @Param shift 表示与基准音符组的偏移（即相差的八度个数）
        // @return 返回成功添加的音符个数
        public int addGroupNotes(int shift) {
            int count = 0;
            for (Note note0 : g0) {
                Note note1 = makeNote(note0, shift);
                if (note1 != null) {
                    table.put(note1.name, note1);
                    count++;
                }
            }
            return count;
        }

        public Note makeNote(Note n0, final int shift) {
            Builder b = new Builder();
            b.sharp = n0.sharp;
            b.key = n0.key;
            b.frequency = n0.frequency;
            b.value = n0.midi;
            b.group = n0.group;
            int step = shift;
            if (step < 0) {
                for (; step < 0; step++) {
                    b.frequency /= 2;
                    b.value -= 12;
                    b.group--;
                }
            } else if (step > 0) {
                for (; step > 0; step--) {
                    b.frequency *= 2;
                    b.value += 12;
                    b.group++;
                }
            }
            if (b.value < 0 || 127 < b.value) {
                return null;
            }
            return b.create();
        }


        public Note[] create() {
            List<Note> list = new ArrayList<>();
            table.values().forEach((item) -> {
                list.add(item);
            });
            list.sort((a, b) -> {
                return a.index - b.index;
            });
            Note[] arr = new Note[list.size()];
            return list.toArray(arr);
        }
    }

    private final static class ReferenceNoteGroupBuilder {

        private final List<Note> list;
        private final Note ref;

        public ReferenceNoteGroupBuilder(Note base) {
            list = new ArrayList<>();
            ref = base;
        }

        void add(int offset, char key, char sharp) {
            final float et12 = 1.0594630943593F; // 12平均律的系数
            float f = ref.frequency;
            int step = offset;
            if (step < 0) {
                for (; step < 0; step++) {
                    f = f / et12;
                }
            } else if (step > 0) {
                for (; step > 0; step--) {
                    f = f * et12;
                }
            }
            Builder b = new Builder();
            b.group = ref.group;
            b.value = ref.midi + offset;
            b.frequency = f;
            b.sharp = (sharp == '#');
            b.key = key;
            list.add(b.create());
        }

        public Note[] create() {
            int count = list.size();
            Note[] arr = new Note[count];
            return list.toArray(arr);
        }
    }


    // 获取所有音符
    private static Note[] getAllNotes() {
        Note[] ref = getReferenceNoteGroup();
        ShiftNoteGroupBuilder b = new ShiftNoteGroupBuilder(ref);
        for (int shift = -1; ; shift--) {
            int count = b.addGroupNotes(shift);
            if (count <= 0) {
                break;
            }
        }
        b.addGroupNotes(0);
        for (int shift = 1; ; shift++) {
            int count = b.addGroupNotes(shift);
            if (count <= 0) {
                break;
            }
        }
        return b.create();
    }

    // 获取参考音符组
    private static Note[] getReferenceNoteGroup() {

        Note a440 = getReferenceNote();
        ReferenceNoteGroupBuilder b = new ReferenceNoteGroupBuilder(a440);

        b.add(-9, 'C', '-');
        b.add(-8, 'C', '#');
        b.add(-7, 'D', '-');
        b.add(-6, 'D', '#');
        b.add(-5, 'E', '-');
        b.add(-4, 'F', '-');
        b.add(-3, 'F', '#');
        b.add(-2, 'G', '-');
        b.add(-1, 'G', '#');
        b.add(0, 'A', '-');
        b.add(1, 'A', '#');
        b.add(2, 'B', '-');

        return b.create();
    }

    // 获取参考音符 (A,440Hz)
    private static Note getReferenceNote() {
        Builder b = new Builder();
        b.key = 'A';
        b.sharp = false;
        b.frequency = 440;
        b.group = 3;
        b.value = 69;
        return b.create();
    }
}
