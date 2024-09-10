package com.bitwormhole.libduckeys.midi;


import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class NoteUnitTest {

    static final String TAG = "NoteUnitTest";

    @Test
    public void doNoteConvertTest() {

        TestItemBuilder b = new TestItemBuilder();
        List<TestItem> items = new ArrayList<>();

        b.key('G').sharp(false).group(8).index(127).fullname("G8").writeTo(items);
        b.key('F').sharp(true).group(8).index(126).fullname("F#8").writeTo(items);
        b.key('F').sharp(false).group(8).index(125).fullname("F8").writeTo(items);


        b.key('C').sharp(false).group(3).index(60).fullname("C3").writeTo(items);
        b.key('D').sharp(false).group(2).index(50).fullname("D2").writeTo(items);
        b.key('E').sharp(false).group(1).index(40).fullname("E1").writeTo(items);
        b.key('F').sharp(true).group(0).index(30).fullname("F#0").writeTo(items);
        b.key('G').sharp(true).group(-1).index(20).fullname("G#-1").writeTo(items);
        b.key('A').sharp(true).group(-2).index(10).fullname("A#-2").writeTo(items);


        b.key('D').sharp(false).group(-2).index(2).fullname("D-2").writeTo(items);
        b.key('C').sharp(true).group(-2).index(1).fullname("C#-2").writeTo(items);
        b.key('C').sharp(false).group(-2).index(0).fullname("C-2").writeTo(items);

        for (TestItem ti : items) {
            ti.testSelf();
        }
        // assertEquals(4, 2 + 2);
    }

    @Test
    public void doCreateNoteWithIndexTest() {
        for (int idx = 0; idx < 128; idx++) {
            Note n = Note.forNote(idx);
            System.out.println("note[" + idx + "] = " + n);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static class TestItem {
        int index;
        int group;
        String fullname;
        boolean sharp;
        char key;

        void testSelf() {
            Note n1 = Note.forNote(index);
            Note n2 = Note.forNote(fullname);
            Note n3 = Note.forNote(key, sharp, group);

            checkNote("n1", n1);
            checkNote("n2", n2);
            checkNote("n3", n3);
        }

        void checkNote(String tag, Note n) {

            if (n.name != this.key) {
                throwError(tag, n, "bad key");
            }

            if (n.sharp != this.sharp) {
                throwError(tag, n, "bad sharp");
            }

            if (n.group != this.group) {
                throwError(tag, n, "bad group");
            }

            if (n.index != this.index) {
                throwError(tag, n, "bad index");
            }

            if (!this.fullname.equals(n.fullname)) {
                throwError(tag, n, "bad fullname");
            }
        }

        void throwError(String tag, Note n, String msg) {
            Gson gs = new Gson();
            String s1 = gs.toJson(n);
            String s2 = gs.toJson(this);
            StringBuilder sb = new StringBuilder();
            sb.append(tag);
            sb.append(" msg:").append(msg);
            sb.append(" n1:").append(s1);
            sb.append(" n2:").append(s2);
            throw new RuntimeException(sb.toString());
        }
    }

    private static class TestItemBuilder {

        int mIndex;
        String mFullName;
        char mKey;
        boolean mSharp;
        int mGroup;

        public TestItemBuilder index(int i) {
            mIndex = i;
            return this;
        }

        public TestItemBuilder fullname(String fn) {
            mFullName = fn;
            return this;
        }

        public TestItemBuilder key(char k) {
            mKey = k;
            return this;
        }

        public TestItemBuilder sharp(boolean s) {
            mSharp = s;
            return this;
        }

        public TestItemBuilder group(int g) {
            mGroup = g;
            return this;
        }


        TestItem create() {
            TestItem ti = new TestItem();
            ti.fullname = mFullName;
            ti.group = mGroup;
            ti.index = mIndex;
            ti.key = mKey;
            ti.sharp = mSharp;
            return ti;
        }

        void writeTo(List<TestItem> dest) {
            TestItem ti = this.create();
            dest.add(ti);
        }
    }
}
