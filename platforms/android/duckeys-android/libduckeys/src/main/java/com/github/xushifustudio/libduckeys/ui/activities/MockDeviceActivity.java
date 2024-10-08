package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.settings.apps.DeviceInfo;

public class MockDeviceActivity extends LifeActivity {

    private TaskManager mTaskMan;
    private DuckClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mock_device);


        findViewById(R.id.button_connect_to_mock_device).setOnClickListener(this::handleClickConnectToMockDevice);
    }


    private void init() {

        mClient = new DuckClient(this);
        mTaskMan = new TaskManager(this);


        LifeManager lm = getLifeManager();
        lm.add(mClient);
        lm.add(mTaskMan);
    }

    private void handleClickConnectToMockDevice(View v) {

        DeviceInfo info = new DeviceInfo();
        info.setUrl("mock://localhost/connections/mock");
        info.setName("mock");

        Task task = (tc) -> {

            MidiConnectionService.Request req = new MidiConnectionService.Request();
            req.device = info;
            req.writeToHistory = true;

            Want want = MidiConnectionService.encode(req);
            want.method = Want.POST;
            want.url = MidiConnectionService.URI_CONNECT;

            Server ser = mClient.waitForServerReady();
            Have have = ser.invoke(want);
            MidiConnectionService.Response resp = MidiConnectionService.decode(have);
            Log.i(DuckLogger.TAG, "" + resp);
        };

        mTaskMan.createNewTask(task).onThen((tc) -> {
            Log.i(DuckLogger.TAG, "ok");
        }).onCatch((tc) -> {
            CommonErrorHandler.handle(this, tc.error);
        }).execute();
    }
}
