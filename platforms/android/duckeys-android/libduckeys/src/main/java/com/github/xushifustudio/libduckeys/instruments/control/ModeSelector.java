package com.github.xushifustudio.libduckeys.instruments.control;

import android.app.AlertDialog;
import android.content.Context;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.ModeManager;
import com.github.xushifustudio.libduckeys.midi.Mode;
import com.github.xushifustudio.libduckeys.midi.ModePattern;
import com.github.xushifustudio.libduckeys.midi.Modes;
import com.github.xushifustudio.libduckeys.midi.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeSelector {

    private final Context context;
    private InstrumentContext ic;

    private final MyModeSet mode_set = new MyModeSet();
    private final MyBaseSet base_set = new MyBaseSet();

    private ModePattern currentMode;
    private Note currentBase;


    public ModeSelector(Context ctx) {
        this.context = ctx;
    }

    private static class MyModeSet {

        final Map<String, Mode> table;
        private Note mBase;

        MyModeSet() {
            table = new HashMap<>();
            this.init();
        }

        public void init() {
            Mode[] src = this.listNoteMode(null);
            for (Mode item : src) {
                String key = item.getPattern().name();
                table.put(key, item);
            }
        }

        public Note getRegularBaseNote(Note base) {
            if (base != null) {
                return base;
            }
            base = this.mBase;
            if (base != null) {
                return base;
            }
            base = Note.forNote(12 * 5);
            mBase = base;
            return base;
        }

        private Mode[] listNoteMode(Note base) {
            base = getRegularBaseNote(base);
            List<Mode> list = new ArrayList<>();

            list.add(Modes.major(base));
            list.add(Modes.minor(base));

            list.add(Modes.ionian(base));
            list.add(Modes.dorian(base));
            list.add(Modes.phrygian(base));
            list.add(Modes.lydian(base));
            list.add(Modes.mixolydian(base));
            list.add(Modes.aeolian(base));
            list.add(Modes.locrian(base));

            list.add(Modes.none());

            int size = list.size();
            return list.toArray(new Mode[size]);
        }

        public String[] keys() {
            List<String> all = new ArrayList<>(table.keySet());
            Collections.sort(all);
            return all.toArray(new String[0]);
        }
    }

    private static class MyBaseSet {

        final Map<String, Note> table;

        MyBaseSet() {
            table = new HashMap<>();
            this.init();
        }

        public void init() {
            Note[] src = this.listNoteBase();
            for (Note item : src) {
                String key = item.key + (item.sharp ? "#" : "");
                table.put(key, item);
            }
        }

        private Note[] listNoteBase() {

            final Note c, d, e, f, g, a, b;
            final Note cs, ds, fs, gs, as;
            final int group = 1;

            c = Note.forNote('c', false, group);
            d = Note.forNote('d', false, group);
            e = Note.forNote('e', false, group);
            f = Note.forNote('f', false, group);
            g = Note.forNote('g', false, group);
            a = Note.forNote('a', false, group);
            b = Note.forNote('b', false, group);

            cs = Note.forNote('c', true, group);
            ds = Note.forNote('d', true, group);
            fs = Note.forNote('f', true, group);
            gs = Note.forNote('g', true, group);
            as = Note.forNote('a', true, group);

            return new Note[]{c, cs, d, ds, e, f, fs, g, gs, a, as, b};
        }

        public String[] keys() {
            List<String> all = new ArrayList<>(table.keySet());
            Collections.sort(all);
            return all.toArray(new String[0]);
        }
    }


    public void showBaseDialog() {
        this.load();
        String[] items = this.base_set.keys();
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("select base note");
        b.setItems(items, (di, idx) -> {
            String key = items[idx];
            this.currentBase = this.base_set.table.get(key);
            this.apply();
        });
        b.setNegativeButton(R.string.cancel, (i, j) -> {
        });
        b.show();
    }


    public void showModeDialog() {
        this.load();
        String[] items = this.mode_set.keys();
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("select mode");
        b.setItems(items, (di, idx) -> {
            String key = items[idx];
            Mode mode = this.mode_set.table.get(key);
            if (mode != null) {
                this.currentMode = mode.getPattern();
            }
            this.apply();
        });
        b.setNegativeButton(R.string.cancel, (i, j) -> {
        });
        b.show();
    }

    private void load() {
        ModeManager mm = ic.getModeManager();
        Mode mode = mm.getHave();
        if (mode == null) {
            return;
        }
        this.currentMode = mode.getPattern();
        this.currentBase = mode.getNote1();
    }

    private void apply() {
        Note n1 = this.currentBase;
        ModePattern mp = this.currentMode;
        if (n1 == null || mp == null) {
            return;
        }
        ModeManager mm = ic.getModeManager();
        // mm.setWant( );
        ic.getInstrument().apply(new Mode(n1, mp));
    }

    public InstrumentContext getIC() {
        return ic;
    }

    public void setIC(InstrumentContext ic) {
        this.ic = ic;
    }
}
