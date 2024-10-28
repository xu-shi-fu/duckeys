package com.github.xushifustudio.libduckeys.midi;

public class ChordPattern extends AbstractPattern {

    public ChordPattern(String name, int[] offset_list) {
        super(name, offset_list);
    }

    public static class Builder {

        final MyOffsetListBuilder off_list_b = new MyOffsetListBuilder();
        String mName;

        public ChordPattern create() {
            int[] offsets = this.off_list_b.create();
            return new ChordPattern(mName, offsets);
        }

        public Builder name(String name) {
            this.mName = name;
            return this;
        }

        public Builder offset(int offset) {
            this.off_list_b.add(offset);
            return this;
        }
    }

    public static boolean equal(ChordPattern a, ChordPattern b) {
        return AbstractPattern.equal(a, b);
    }


    @Override
    public int type() {
        return NotePattern.TYPE_CHORD;
    }
}
