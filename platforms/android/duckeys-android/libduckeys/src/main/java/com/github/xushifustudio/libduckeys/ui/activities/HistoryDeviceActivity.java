package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;

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

public class HistoryDeviceActivity extends LifeActivity {

    private TaskManager mTaskMan;
    private DuckClient mClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_device_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch();
    }

    private void init() {

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

    private void onFetchOk(MidiConnectionService.Response response) {
        Log.i(DuckLogger.TAG, response + "");
    }
}
