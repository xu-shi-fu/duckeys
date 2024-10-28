package com.github.xushifustudio.libduckeys.midi;

import com.github.xushifustudio.libduckeys.helper.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractPattern implements NotePattern {

    private final String mName;
    private final int[] mOffsets;

    public AbstractPattern(String name, int[] offset_list) {
        if (offset_list == null) {
            offset_list = new int[1];
        }
        if (name == null) {
            name = "unnamed";
        }
        this.mOffsets = offset_list;
        this.mName = name;
    }

    protected static class MyOffsetListBuilder {
        final List<Integer> list = new ArrayList<>();

        void add(int offset) {
            this.list.add(offset);
        }

        int[] create() {
            int[] array = new int[list.size()];
            for (int i = 0; i < array.length; i++) {
                array[i] = list.get(i);
            }
            return array;
        }
    }


    @Override
    public String name() {
        return mName;
    }

    @Override
    public int count() {
        return this.mOffsets.length;
    }

    @Override
    public int offset(int index) {
        if (0 <= index && index < mOffsets.length) {
            return mOffsets[index];
        }
        return 0;
    }


    public static boolean equal(AbstractPattern a, AbstractPattern b) {
        if (a == null || b == null) {
            return false;
        }
        return Arrays.equals(a.mOffsets, b.mOffsets);
    }


    @Override
    public Note getNote(int index, Note base) {
        if (base == null) {
            base = Note.forNote(12 * 5);
        }
        int off = this.offset(index);
        int i_note = base.index + off;
        return Note.forNote(i_note);
    }
}
