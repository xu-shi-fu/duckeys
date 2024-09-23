package com.bitwormhole.libduckeys.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.ble.BLEDeviceScanner;
import com.bitwormhole.libduckeys.ble.BluetoothPermissionChecker;
import com.bitwormhole.libduckeys.ble.BluetoothSwitchChecker;
import com.bitwormhole.libduckeys.ble.IBluetoothDeviceScanner;
import com.bitwormhole.libduckeys.ble.OlderBluetoothDeviceScanner;
import com.bitwormhole.libduckeys.context.LifeActivity;
import com.bitwormhole.libduckeys.context.LifeManager;

public class BluetoothScannerActivity extends LifeActivity {


    // views
    private ListView mListViewDevices;
    private Button mButtonStartScanning;
    private Button mButtonStopScanning;

    // controllers
    private IBluetoothDeviceScanner mBtDevScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //  mBtDevScanner = new OlderBluetoothDeviceScanner(this);
        mBtDevScanner = new BLEDeviceScanner(this);

        LifeManager lm = getLifeManager();
        lm.add(new BluetoothSwitchChecker(this));
        lm.add(new BluetoothPermissionChecker(this));
        lm.add(mBtDevScanner.getMyLife());


        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bluetooth_scan);
        mListViewDevices = findViewById(R.id.listview_devices);
        mButtonStartScanning = findViewById(R.id.button_scanning_start);
        mButtonStopScanning = findViewById(R.id.button_scanning_stop);

        setupHandlers();
    }

    private void setupHandlers() {

        mButtonStartScanning.setOnClickListener((v) -> {
            mBtDevScanner.start();
        });

        mButtonStopScanning.setOnClickListener((v) -> {
            mBtDevScanner.stop();
        });

    }
}
