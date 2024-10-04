package com.github.xushifustudio.libduckeys.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.services.CurrentDeviceService;
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;

public class CurrentDeviceActivity extends LifeActivity {

    private TaskManager mTaskMan;
    private DuckClient mClient;

    private TextView mTextCurrentDeviceName;
    private TextView mTextCurrentDeviceType;
    private TextView mTextCurrentDeviceAddress;
    private Button mButtonAddNewDevice;
    private Button mButtonShowHistoryDevice;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        initLifecycle();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_current_device_state);

        mTextCurrentDeviceName = findViewById(R.id.text_current_device_name);
        mTextCurrentDeviceType = findViewById(R.id.text_current_device_type);
        mTextCurrentDeviceAddress = findViewById(R.id.text_current_device_address);
        mButtonAddNewDevice = findViewById(R.id.button_add_new_device);
        mButtonShowHistoryDevice = findViewById(R.id.button_show_history_device_list);

        setupListeners();
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadCurrentDeviceInfo();
    }


    private void initLifecycle() {

        mTaskMan = new TaskManager(this);
        mClient = new DuckClient(this);

        LifeManager lm = getLifeManager();
        lm.add(mTaskMan);
        lm.add(mClient);
    }

    private void setupListeners() {
        mButtonShowHistoryDevice.setOnClickListener((v) -> {
            Intent i = new Intent(this, HistoryDeviceActivity.class);
            startActivity(i);
        });
        mButtonAddNewDevice.setOnClickListener((v) -> {
            Intent i = new Intent(this, BluetoothScanningActivity.class);
            startActivity(i);
        });
    }


    private void loadCurrentDeviceInfo() {

        final MidiConnectionService.Holder holder = new MidiConnectionService.Holder();

        Task task = (tc) -> {
            Server server = mClient.waitForServerReady(3000);
            MidiConnectionService.Request req = new MidiConnectionService.Request();
            Want want = MidiConnectionService.encode(req);

            holder.request = req;
            Have have = server.invoke(want);
            holder.response = MidiConnectionService.decode(have);
        };

        mTaskMan.createNewTask(task).onThen((tc) -> {

            //     DeviceInfo dev = mSettingsManager.getCurrentDevice();
            //     mTextCurrentDeviceName.setText(dev.getName());
            //   mTextCurrentDeviceAddress.setText(dev.getUrl());
            // mTextCurrentDeviceType.setText(dev.getScheme());

        }).onCatch((tc) -> {


        }).onFinally((tc) -> {

        }).execute();
    }
}
