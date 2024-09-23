package com.bitwormhole.libduckeys.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.ble.BLEDeviceScanner;
import com.bitwormhole.libduckeys.ble.BluetoothPermissionChecker;
import com.bitwormhole.libduckeys.ble.BluetoothScanningResult;
import com.bitwormhole.libduckeys.ble.BluetoothSwitchChecker;
import com.bitwormhole.libduckeys.ble.IBluetoothDeviceScanner;
import com.bitwormhole.libduckeys.context.LifeActivity;
import com.bitwormhole.libduckeys.context.LifeManager;
import com.bitwormhole.libduckeys.helper.IO;
import com.bitwormhole.libduckeys.midi.MidiUriAgent;
import com.bitwormhole.libduckeys.midi.MidiUriConnection;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothScanningActivity extends LifeActivity {

    // views
    private ListView mListViewDevices;
    private Button mButtonStartScanning;
    private Button mButtonStopScanning;

    // controllers
    private IBluetoothDeviceScanner mBtDevScanner;

    // data
    private Map<String, BluetoothScanningResult> mTable;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //  mBtDevScanner = new OlderBluetoothDeviceScanner(this);
        mBtDevScanner = new BLEDeviceScanner(this);
        mTable = new HashMap<>();

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

        mBtDevScanner.setResultsHandler((state, scanner) -> {
            onResultsUpdated(state, scanner);
        });

        mButtonStartScanning.setOnClickListener((v) -> {
            mBtDevScanner.start();
        });

        mButtonStopScanning.setOnClickListener((v) -> {
            mBtDevScanner.stop();
        });

        mListViewDevices.setOnItemClickListener((av, v, idx, id) -> {
            Object item = av.getAdapter().getItem(idx);
            handleClickItem(item);
        });
    }

    static interface ListItemFields {
        String NAME = "name";
        String ADDRESS = "addr";
        String ID = "id";
    }

    private void onResultsUpdated(int state, IBluetoothDeviceScanner scanner) {

        BluetoothScanningResult[] list = scanner.listResults();
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, BluetoothScanningResult> table = new HashMap<>();

        for (BluetoothScanningResult item : list) {
            String id = item.address + "";
            Map<String, String> m = new HashMap<>();
            m.put(ListItemFields.NAME, item.name);
            m.put(ListItemFields.ADDRESS, item.address);
            m.put(ListItemFields.ID, id);
            data.add(m);
            table.put(id, item);
        }

        int resId = android.R.layout.two_line_list_item;
        String[] from = {ListItemFields.NAME, ListItemFields.ADDRESS};
        int[] to = {android.R.id.text1, android.R.id.text2};
        ListAdapter ada = new SimpleAdapter(this, data, resId, from, to);
        mListViewDevices.setAdapter(ada);
        mTable = table;
    }

    private void handleClickItem(Object item) {

        Map<String, String> it2 = (Map<String, String>) item;
        String id = it2.get(ListItemFields.ID);
        BluetoothScanningResult it3 = mTable.get(id);

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(it3.address).setMessage(it3.name);
        adb.setPositiveButton("connect", (a, b) -> {
            tryConnectToBleDevice(it3);
        });
        adb.setNegativeButton("close", (a, b) -> {
        });
        adb.show();
    }

    private URI prepareUriForBleDevice(BluetoothScanningResult bsr) {
        String addr = bsr.address + "";
        addr = addr.toLowerCase().replace(':', '-');
        String str = "ble://" + addr + "/midi";
        return URI.create(str);
    }

    private void tryConnectToBleDevice(BluetoothScanningResult bsr) {
        MidiUriConnection conn = null;
        try {
            MidiUriAgent agent = MidiUriAgent.getInstance(this);
            URI uri = prepareUriForBleDevice(bsr);
            conn = agent.open(uri);
            conn.connect();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            IO.close(conn);
        }
    }

}
