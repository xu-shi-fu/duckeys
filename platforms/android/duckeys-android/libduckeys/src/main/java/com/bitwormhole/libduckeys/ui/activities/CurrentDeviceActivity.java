package com.bitwormhole.libduckeys.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.context.LifeActivity;
import com.bitwormhole.libduckeys.context.LifeManager;
import com.bitwormhole.libduckeys.settings.SettingsManager;

public class CurrentDeviceActivity extends LifeActivity {

    private SettingsManager mSettingsManager;

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

    private void initLifecycle() {
        mSettingsManager = new SettingsManager(this);
        LifeManager lm = getLifeManager();
        lm.add(mSettingsManager);
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

}
