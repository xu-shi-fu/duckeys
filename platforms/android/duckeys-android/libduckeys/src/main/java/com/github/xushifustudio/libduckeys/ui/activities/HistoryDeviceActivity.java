package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.services.HistoryDeviceService;
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.settings.apps.DeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryDeviceActivity extends LifeActivity {

    private TaskManager mTaskMan;
    private DuckClient mClient;

    private ListView mListView;
    private Map<String, DeviceInfo> mTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_device_list);

        mListView = findViewById(R.id.listview_history_items);

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch();
    }

    private void init() {

        mTable = new HashMap<>();
        mTaskMan = new TaskManager(this);
        mClient = new DuckClient(this);

        LifeManager lm = getLifeManager();
        lm.add(mTaskMan);
        lm.add(mClient);
    }

    private void fetch() {

        final MidiConnectionService.Holder holder = new MidiConnectionService.Holder();

        Task task = (tc) -> {
            Server server = mClient.waitForServerReady(3000);

            MidiConnectionService.Request req = new MidiConnectionService.Request();
            holder.request = req;
            Want want = MidiConnectionService.encode(req);
            want.method = Want.GET;
            want.url = MidiConnectionService.URI_HISTORY_LIST;

            Have have = server.invoke(want);
            MidiConnectionService.Response resp = MidiConnectionService.decode(have);
            holder.response = resp;
        };

        mTaskMan.createNewTask(task).onThen((tc) -> {
            this.onFetchOk(holder.response);
        }).onCatch((tc) -> {
            CommonErrorHandler.handle(tc.context, tc.error);
        }).onFinally((tc) -> {
            // nop
        }).execute();
    }

    private static class ListItemFields {
        public static String NAME = "name";
        public static String ADDRESS = "address";
        public static String KEY = "key";
    }

    private void onFetchOk(MidiConnectionService.Response response) {

        if (response == null) {
            return;
        }

        List<DeviceInfo> src = response.history;
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, DeviceInfo> table = new HashMap<>();

        if (src == null) {
            return;
        }

        for (DeviceInfo di : src) {
            String key = di.getUrl();
            Map<String, String> item = new HashMap<>();
            item.put(ListItemFields.NAME, di.getName());
            item.put(ListItemFields.ADDRESS, di.getUrl());
            item.put(ListItemFields.KEY, key);
            data.add(item);
            table.put(key, di);
        }

        int resId = android.R.layout.two_line_list_item;
        String[] from = {ListItemFields.NAME, ListItemFields.ADDRESS};
        int[] to = {android.R.id.text1, android.R.id.text2};
        ListAdapter ada = new SimpleAdapter(this, data, resId, from, to);
        mListView.setAdapter(ada);
        mTable = table;
    }
}
