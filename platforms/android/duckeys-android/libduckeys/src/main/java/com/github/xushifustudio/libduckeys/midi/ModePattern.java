package com.github.xushifustudio.libduckeys.midi;

import java.util.List;

public class ModePattern extends AbstractPattern {

    public ModePattern(String name, int[] offset_list) {
        super(name, offset_list);
    }

    public static class Builder {

        final MyOffsetListBuilder mOffsetListB = new MyOffsetListBuilder();
        String mName;

        public ModePattern create() {
            int[] offsets = this.mOffsetListB.create();
            return new ModePattern(mName, offsets);
        }

        public Builder offset(int offset) {
            this.mOffsetListB.add(offset);
            return this;
        }

        public Builder name(String name) {
            this.mName = name;
            return this;
        }
    }

    @Override
    public int type() {
        return NotePattern.TYPE_MODE;
    }
}
