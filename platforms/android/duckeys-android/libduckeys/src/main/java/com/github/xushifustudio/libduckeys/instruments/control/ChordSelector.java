package com.github.xushifustudio.libduckeys.instruments.control;

import android.app.AlertDialog;
import android.content.Context;

import com.github.xushifustudio.libduckeys.instruments.ChordManager;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.midi.Chord;
import com.github.xushifustudio.libduckeys.midi.ChordPattern;
import com.github.xushifustudio.libduckeys.midi.Chords;
import com.github.xushifustudio.libduckeys.midi.Mode;
import com.github.xushifustudio.libduckeys.midi.Modes;
import com.github.xushifustudio.libduckeys.midi.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChordSelector {

    private InstrumentContext ic;

    private final MyChordTypeSet chord_type_set = new MyChordTypeSet();
    private final MyChordRootSet chord_root_set = new MyChordRootSet();

    private ChordPattern mType;
    private Note mRoot;

    public ChordSelector() {
    }


    private static class MyChordTypeSet {

        final Map<String, ChordPattern> table;

        public MyChordTypeSet() {
            this.table = new HashMap<>();
            this.init();
        }

        private void init() {
            ChordPattern[] src = this.listChordTypes();
            Map<String, ChordPattern> dst = this.table;
            for (ChordPattern pattern : src) {
                String key = pattern.name();
                dst.put(key, pattern);
            }
        }

        private ChordPattern[] listChordTypes() {
            Note root = Note.empty();
            List<Chord> list = new ArrayList<>();

            list.add(Chords.major(root));
            list.add(Chords.major7(root));
            list.add(Chords.major9(root));
            list.add(Chords.major11(root));

            list.add(Chords.minor(root));
            list.add(Chords.minor7(root));
            list.add(Chords.minor9(root));

            list.add(Chords.none());

            ChordPattern[] array = new ChordPattern[list.size()];
            for (int i = 0; i < array.length; i++) {
                Chord ch = list.get(i);
                array[i] = ch.getPattern();
            }
            return array;
        }


        public String[] keys() {
            List<String> all = new ArrayList<>(table.keySet());
            Collections.sort(all);
            return all.toArray(new String[0]);
        }
    }

    private static class MyChordRootSet {

        final Map<String, Note> table;

        public MyChordRootSet() {
            this.table = new HashMap<>();
            this.init();
        }

        private void init() {
            Map<String, Note> dst = this.table;
            Note[] src = this.listChordRoots();
            for (Note root : src) {
                String key = root.key + (root.sharp ? "#" : "");
                dst.put(key, root);
            }
        }

        private Note[] listChordRoots() {
            int group = 1;
            List<Note> list = new ArrayList<>();

            list.add(Note.forNote('c', false, group));
            list.add(Note.forNote('d', false, group));
            list.add(Note.forNote('e', false, group));
            list.add(Note.forNote('f', false, group));
            list.add(Note.forNote('g', false, group));
            list.add(Note.forNote('a', false, group));
            list.add(Note.forNote('b', false, group));

            list.add(Note.forNote('c', true, group));
            list.add(Note.forNote('d', true, group));
            list.add(Note.forNote('f', true, group));
            list.add(Note.forNote('g', true, group));
            list.add(Note.forNote('a', true, group));

            return list.toArray(new Note[0]);
        }

        public String[] keys() {
            List<String> all = new ArrayList<>(table.keySet());
            Collections.sort(all);
            return all.toArray(new String[0]);
        }
    }

    public void showRootDialog() {
        this.load();
        Context ctx = ic.getParent();
        String[] keys = chord_root_set.keys();

        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle("Select Chord Root");
        b.setItems(keys, (di, idx) -> {
            String key = keys[idx];
            Note root = chord_root_set.table.get(key);
            this.mRoot = root;
            this.apply();
        });
        b.setNegativeButton(R.string.cancel, (u, v) -> {
        });
        b.show();
    }

    public void showTypeDialog() {
        this.load();
        Context ctx = ic.getParent();
        String[] keys = chord_type_set.keys();

        AlertDialog.Builder b = new AlertDialog.Builder(ctx);
        b.setTitle("Select Chord Type");
        b.setItems(keys, (di, idx) -> {
            String key = keys[idx];
            ChordPattern pattern = chord_type_set.table.get(key);
            this.mType = pattern;
            this.apply();
        });
        b.setNegativeButton(R.string.cancel, (u, v) -> {
        });
        b.show();
    }


    private void load() {
        if (ic == null) {
            return;
        }
        Chord current = ic.getChordManager().getHave();
        if (current == null) {
            return;
        }
        mRoot = current.getRoot();
        mType = current.getPattern();
    }

    private void apply() {
        if (ic == null || mRoot == null || mType == null) {
            return;
        }
        ChordManager cm = ic.getChordManager();
        cm.setWant(new Chord(mRoot, mType));
        cm.apply(ic, false);
    }

    public InstrumentContext getIC() {
        return ic;
    }

    public void setIC(InstrumentContext ic) {
        this.ic = ic;
    }
}
