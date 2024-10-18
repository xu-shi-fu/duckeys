package com.github.xushifustudio.libduckeys.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;
import com.github.xushifustudio.libduckeys.settings.apps.DeviceInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidiDeviceActivity extends LifeActivity {

    // Model
    private Map<String, MidiDeviceInfo> mTable = new HashMap<>();
    private List<MidiDeviceInfo> mList = new ArrayList<>();


    // View
    private Button mButtonRefresh;
    private ListView mListViewDevices;

    // Controller
    private MidiManager mMidiMan;
    private TaskManager mTaskMan;
    private DuckClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_midi_devices);
        mButtonRefresh = findViewById(R.id.button_refresh);
        mListViewDevices = findViewById(R.id.listview_midi_device_list);

        mButtonRefresh.setOnClickListener((v) -> {
            fetch();
        });
        setupListViewListener();
    }


    @Override
    protected void onStart() {
        super.onStart();
        fetch();
    }

    private static class ListViewItemFields {
        static final String key = "key";
        static final String label = "label";
        static final String desc = "desc";
    }


    private void init() {

        mMidiMan = (MidiManager) this.getSystemService(Context.MIDI_SERVICE);
        mTaskMan = new TaskManager(this);
        mClient = new DuckClient(this);

        LifeManager lm = this.getLifeManager();
        lm.add(mTaskMan);
        lm.add(mClient);
    }

    private void setupListViewListener() {
        mListViewDevices.setOnItemClickListener((av, v, pos, id) -> {
            MidiDeviceInfo item = mList.get(pos);
            showItemDetailDialog(item);
        });
    }

    private void showItemDetailDialog(MidiDeviceInfo info) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("id:" + info.getId());
        b.setMessage(info.toString());
        b.setNegativeButton(R.string.cancel, (x, y) -> {
        });
        b.setPositiveButton(R.string.connect, (x, y) -> {
            connect2(info);
        });
        b.show();
    }

    private void connect2(MidiDeviceInfo info) {

        int id = info.getId();
        DeviceInfo dev = new DeviceInfo();
        dev.setUrl("android://media.midi/MidiDeviceInfo/" + id);

        Task task = (tc) -> {
            MidiConnectionService.Request req = new MidiConnectionService.Request();
            req.writeToHistory = true;
            req.device = dev;

            Want want = MidiConnectionService.encode(req);
            want.method = Want.POST;
            want.url = MidiConnectionService.URI_CONNECT;

            Server ser = mClient.waitForServerReady();
            Have have = ser.invoke(want);
            MidiConnectionService.decode(have);
        };
        mTaskMan.createNewTask(task).onThen((tc) -> {
            finish();
        }).onCatch((tc) -> {
            CommonErrorHandler.handle(this, tc.error);
        }).execute();
    }


    private void fetch() {
        MidiDeviceInfo[] src = mMidiMan.getDevices();
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, MidiDeviceInfo> table = new HashMap<>();
        List<MidiDeviceInfo> list = new ArrayList<>();

        for (MidiDeviceInfo info : src) {
            Map<String, String> m = new HashMap<>();
            String key = info.getId() + "";
            String label = info.toString();
            m.put(ListViewItemFields.key, key);
            m.put(ListViewItemFields.label, "" + label);
            m.put(ListViewItemFields.desc, "todo");
            data.add(m);
            table.put(key, info);
            list.add(info);
        }

        String[] from = new String[]{ListViewItemFields.label, ListViewItemFields.desc};
        int[] to = {android.R.id.text1, android.R.id.text2};
        int layout_id = android.R.layout.two_line_list_item;
        SimpleAdapter ada = new SimpleAdapter(this, data, layout_id, from, to);
        mListViewDevices.setAdapter(ada);
        mTable = table;
        mList = list;
    }
}
