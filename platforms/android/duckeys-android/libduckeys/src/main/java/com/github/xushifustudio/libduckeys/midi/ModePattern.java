package com.github.xushifustudio.libduckeys.midi;

import java.util.List;

public class ModePattern extends AbstractPattern {

    public ModePattern(String name, int[] offset_list) {
        super(name, offset_list);
    }

    public static class Builder {

        final MyOffsetListBuilder mOffsetListB = new MyOffsetListBuilder();
        String mName;
        private int next_offset;

        public ModePattern create() {
            int[] offsets = this.mOffsetListB.create();
            return new ModePattern(mName, offsets);
        }

        /***
         * @param offset 相对于 note1 的半音阶
         * */
        public Builder offset(int offset) {
            this.mOffsetListB.add(offset);
            return this;
        }

        /***
         * @param delta 相对于 上一个音符 的半音阶
         * */
        public Builder add(final int delta) {
            final int current = this.next_offset;
            this.next_offset = current + delta;
            return this.offset(current);
        }

        public Builder name(String name) {
            this.mName = name;
            return this;
        }
    }


    public static boolean equal(ModePattern a, ModePattern b) {
        return AbstractPattern.equal(a, b);
    }

    @Override
    public int type() {
        return NotePattern.TYPE_MODE;
    }
}
