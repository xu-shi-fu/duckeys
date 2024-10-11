package com.github.xushifustudio.libduckeys.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.midi.MidiCCEvent;
import com.github.xushifustudio.libduckeys.midi.MidiCCTable;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiUserAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidiCCConsoleActivity extends LifeActivity {

    // view
    private ListView mListView;
    private Switch mSwitchAutoSend;

    // model
    private Map<String, CCProperty> mTable;
    private List<CCProperty> mList;

    // controller
    private DuckClient mClient;
    private TaskManager mTaskMan;
    private MidiUserAgent mMidiUA;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        init();
        initCCList();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_midi_cc_console);

        mListView = findViewById(R.id.listview_cc_props);
        mSwitchAutoSend = findViewById(R.id.switch_auto_send);

        setupListView();
        setupListItemListener();
    }

    private void init() {
        mClient = new DuckClient(this);
        mTaskMan = new TaskManager(this);
        mMidiUA = new MidiUserAgent(this, mClient, mTaskMan);

        LifeManager lm = getLifeManager();
        lm.add(mClient);
        lm.add(mTaskMan);
        lm.add(mMidiUA);
    }

    private void initCCList() {
        List<CCProperty> list = new ArrayList<>();
        for (int i = 0; i < 128; i++) {
            byte cc = (byte) i;
            MidiCCTable.Item info = MidiCCTable.find(this, cc);
            CCProperty p = new CCProperty();
            p.key = "cc" + cc;
            p.label = info.label;
            p.cc = cc;
            p.value = 60;
            p.description = info.description;
            list.add(p);
        }
        this.mList = list;
    }

    private static class ListItemFields {
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String LABEL = "label";
        public static final String VALUE = "value";
        public static final String DESCRIPTION = "desc";
    }

    private static class CCProperty {
        String key;
        byte cc;
        String label;
        String description;
        int value;
    }

    private class MyConsoleDialogController {

        private View mRoot;
        private CCProperty mProperty;

        MyConsoleDialogController() {
        }

        void inflate(Activity ctx) {

            LinearLayout view = new LinearLayout(ctx);
            ctx.getLayoutInflater().inflate(R.layout.view_cc_console_dialog, view);
            Button btn_send = view.findViewById(R.id.button_send_cc);
            EditText edit_value = view.findViewById(R.id.edit_cc_value);
            SeekBar seek_value = view.findViewById(R.id.seekbar_value);

            btn_send.setOnClickListener((v) -> {
                int value = 0;
                try {
                    String str = edit_value.getText().toString();
                    value = Integer.parseInt(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendCC(mProperty, value);
            });

            seek_value.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    String str = String.valueOf(progress);
                    edit_value.setText(str);
                    tryAutoSend(mProperty, progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            int value = seek_value.getProgress();
            edit_value.setText(String.valueOf(value));
            mRoot = view;
        }


        void setProperty(CCProperty p) {
            mProperty = p;
        }
    }

    private void setupListView() {

        List<CCProperty> src = this.mList;
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, CCProperty> table = new HashMap<>();

        if (src == null) {
            return;
        }

        for (CCProperty item1 : src) {
            String key = item1.key;
            Map<String, String> m = new HashMap<>();
            m.put(ListItemFields.KEY, key);
            m.put(ListItemFields.LABEL, item1.label);
            m.put(ListItemFields.VALUE, String.valueOf(item1.value));
            m.put(ListItemFields.DESCRIPTION, item1.description);
            data.add(m);
            table.put(key, item1);
        }

        int resId = android.R.layout.two_line_list_item;
        String[] from = {ListItemFields.LABEL, ListItemFields.DESCRIPTION};
        int[] to = {android.R.id.text1, android.R.id.text2};
        ListAdapter ada = new SimpleAdapter(this, data, resId, from, to);
        mListView.setAdapter(ada);
        mTable = table;
    }

    private void setupListItemListener() {
        mListView.setOnItemClickListener((parent, view, pos, id) -> {
            if (0 <= pos && pos < mList.size()) {
                CCProperty p = mList.get(pos);
                showSendCCEventDialog(p);
            }
        });
    }

    private void showSendCCEventDialog(CCProperty p) {
        if (p == null) {
            return;
        }

        MyConsoleDialogController cdc = new MyConsoleDialogController();
        cdc.inflate(this);
        cdc.setProperty(p);

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle(p.label);
        ab.setView(cdc.mRoot);
        ab.setNegativeButton(R.string.close, (a, b) -> {
        });
        AlertDialog dlg = ab.create();
        dlg.show();
    }


    private void tryAutoSend(CCProperty p, int value) {
        if (!mSwitchAutoSend.isChecked()) {
            return;
        }
        sendCC(p, value);
    }


    private void sendCC(CCProperty p, int value) {
        String msg = "send midi event:" + p.label + " value:" + value;
        Log.i(DuckLogger.TAG, msg);


        MidiCCEvent ev1 = new MidiCCEvent();
        ev1.cc = p.cc;
        ev1.value = (byte) value;

        p.value = value;

        MidiEvent ev2 = MidiCCEvent.toMidiEvent(ev1);
        mMidiUA.getTx().dispatch(ev2);
    }
}
