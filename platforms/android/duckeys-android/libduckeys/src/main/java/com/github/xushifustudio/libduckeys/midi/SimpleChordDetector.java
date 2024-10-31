package com.github.xushifustudio.libduckeys.midi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleChordDetector {

    private final Map<String, Chord> table;

    public SimpleChordDetector() {
        this.table = Inner.makeChordTable();
    }

    private static class Inner {

        private static Map<String, Chord> makeChordTable() {
            Map<String, Chord> dst = new HashMap<>();
            List<Chord> src = new ArrayList<>();
            Note root = Note.forNote(12 * 5);
            src = Chords.listAll(root, src);
            for (Chord chord : src) {
                int[] plist = createPositionList(chord);
                String fp = fingerprint(plist);
                dst.put(fp, chord);
            }
            return dst;
        }


        /**
         * 把 List<Note> 转换成 note_list
         */
        private static Note[] createNoteList(List<Note> notes) {
            final Map<String, Note> m = new HashMap<>();
            for (Note n : notes) {
                String key = String.valueOf(n.key);
                m.put(key, n);
            }
            List<Note> list = new ArrayList<>(m.values());
            return list.toArray(new Note[0]);
        }

        private static int computePositionOffset(Note root, Note note) {
            final int m0 = root.midi;
            int mx = note.midi;
            while (mx > m0) {
                mx -= 12;
            }
            while (mx < m0) {
                mx += 12;
            }
            return mx - m0;
        }


        private static int[] createPositionList(Note root, Note[] note_list, int[] dst) {
            if (dst == null) {
                dst = new int[note_list.length];
            }
            for (int i = 0; i < dst.length; ++i) {
                Note note = note_list[i];
                if (note.midi == root.midi) {
                    dst[i] = 0;
                } else {
                    dst[i] = computePositionOffset(root, note);
                }
            }
            return dst;
        }


        /**
         * 把 ChordPattern 转换成 pos_list
         */
        private static int[] createPositionList(Chord chord) {
            if (chord == null) {
                return new int[0];
            }
            final Note root = chord.getRoot();
            final ChordPattern pattern = chord.getPattern();
            final int count = pattern.count();
            Note[] note_array = new Note[count];
            for (int i = 0; i < count; ++i) {
                note_array[i] = pattern.getNote(i, root);
            }
            return createPositionList(root, note_array, null);
        }


        /**
         * 计算和弦的签名
         *
         * @return fingerprint text
         */
        private static String fingerprint(int[] pos_list) {
            if (pos_list == null) {
                return "";
            }
            Arrays.sort(pos_list);
            StringBuilder b = new StringBuilder();
            for (int pos : pos_list) {
                b.append(pos).append('.');
            }
            return b.toString();
        }
    }

    /**
     * 如果探测到，返回对应的和弦；否则，返回 null
     */
    public synchronized Chord detect(List<Note> notes) {
        if (notes == null) {
            return null;
        }
        Note[] note_array = Inner.createNoteList(notes);
        int[] offsets = new int[note_array.length];
        for (Note root : note_array) {
            offsets = Inner.createPositionList(root, note_array, offsets);
            String fp = Inner.fingerprint(offsets);
            Chord ch = this.table.get(fp);
            if (ch != null) {
                return new Chord(root, ch.getPattern());
            }
        }
        return null;
    }
}
