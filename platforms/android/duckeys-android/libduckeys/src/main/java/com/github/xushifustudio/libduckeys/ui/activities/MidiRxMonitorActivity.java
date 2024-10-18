package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiMessages;
import com.github.xushifustudio.libduckeys.midi.MidiUserAgent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidiRxMonitorActivity extends LifeActivity {

    // view
    private ListView mListView;
    private Button mButtonClear;
    private Button mButtonEcho;

    // model
    private Map<String, MidiEvent> mTable;
    private List<MidiEvent> mList;

    // controller
    private DuckClient mClient;
    private TaskManager mTaskMan;
    private MidiUserAgent mMidiUA;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        init();
        // initCCList();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_midi_rx_monitor);

        mListView = findViewById(R.id.listview_rx_midi_events);
        mButtonClear = findViewById(R.id.button_clear);
        mButtonEcho = findViewById(R.id.button_echo_xxx);

        setupListView();
        setupListItemListener();
        setupButtonsListener();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMidiUA.setRx((evt) -> {
            String msg = "midi_event: " + evt;
            Log.i(DuckLogger.TAG, msg);
            handleEvent(evt);
        });


        // for debug
        mList.add(new MidiEvent());
        mList.add(new MidiEvent());
        mList.add(new MidiEvent());
        updateListView();
    }


    private void handleEvent(MidiEvent me) {

        try {
            Object anyMsg = MidiMessages.parseAny(me);
            Log.i(DuckLogger.TAG, "handle midi message: " + anyMsg.getClass());
        } catch (Exception e) {
            CommonErrorHandler.handle(this, e, CommonErrorHandler.FLAG_LOG);
        }

        Task task = (tc) -> {
        };
        mTaskMan.createNewTask(task).onThen((tc) -> {
            mList.add(me);
            updateListView();
        }).execute();
    }


    private void init() {
        mClient = new DuckClient(this);
        mTaskMan = new TaskManager(this);
        mMidiUA = new MidiUserAgent(this, mClient, mTaskMan);

        mList = new ArrayList<>();
        mTable = new HashMap<>();

        LifeManager lm = getLifeManager();
        lm.add(mClient);
        lm.add(mTaskMan);
        lm.add(mMidiUA);
    }

    private void updateListView() {
        int res_id = android.R.layout.simple_list_item_1;
        List<MidiEvent> src = mList;
        ArrayAdapter<MidiEvent> ada = new ArrayAdapter<>(this, res_id, src);
        mListView.setAdapter(ada);
    }


    private static class ListItemFields {
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String LABEL = "label";
        public static final String VALUE = "value";
        public static final String DESCRIPTION = "desc";
    }


    private void setupListView() {
/*
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

 */
    }

    private void sendEcho(String text) {
        final String msg = "echo " + text;
        MidiEvent evt = new MidiEvent();
        evt.data = msg.getBytes(StandardCharsets.UTF_8);
        evt.offset = 0;
        evt.count = evt.data.length;
        mMidiUA.dispatch(evt);
    }

    private void setupButtonsListener() {
        mButtonClear.setOnClickListener((v) -> {
            mList.clear();
            updateListView();
        });
        mButtonEcho.setOnClickListener((v) -> {
            sendEcho("hello");
        });
    }


    private void setupListItemListener() {
        mListView.setOnItemClickListener((parent, view, pos, id) -> {
            if (0 <= pos && pos < mList.size()) {
                // CCProperty p = mList.get(pos);
                //  showSendCCEventDialog(p);
            }
        });
    }
}
