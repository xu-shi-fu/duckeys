package com.github.xushifustudio.libduckeys.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.ble.BLEDeviceScanner;
import com.github.xushifustudio.libduckeys.ble.BluetoothPermissionChecker;
import com.github.xushifustudio.libduckeys.ble.BluetoothScanningResult;
import com.github.xushifustudio.libduckeys.ble.BluetoothSwitchChecker;
import com.github.xushifustudio.libduckeys.ble.IBluetoothDeviceScanner;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.settings.apps.DeviceInfo;

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
    private TaskManager mTaskMan;
    private DuckClient mClient;


    // data

    private Map<String, BluetoothScanningResult> mTable;

    private void init() {

        //  mBtDevScanner = new OlderBluetoothDeviceScanner(this);
        mBtDevScanner = new BLEDeviceScanner(this);
        mTable = new HashMap<>();
        mClient = new DuckClient(this);
        mTaskMan = new TaskManager(this);

        LifeManager lm = getLifeManager();
        lm.add(new BluetoothSwitchChecker(this));
        lm.add(new BluetoothPermissionChecker(this));
        lm.add(mBtDevScanner.getMyLife());
        lm.add(mTaskMan);
        lm.add(mClient);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        init();
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
            // tryConnectToBleDevice(it3);
            connect2(it3);
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


    private void connect2(BluetoothScanningResult bsr) {

        URI uri = prepareUriForBleDevice(bsr);
        final DeviceInfo dev = new DeviceInfo();
        dev.setUrl(uri.toString());
        dev.setName(bsr.name);
        dev.setScheme("ble");

        Task task = (tc) -> {
            MidiConnectionService.Request req = new MidiConnectionService.Request();
            req.device = dev;
            req.writeToHistory = true;

            Want want = MidiConnectionService.encode(req);
            want.method = Want.POST;
            want.url = MidiConnectionService.URI_CONNECT;

            Server server = mClient.waitForServerReady();
            Have have = server.invoke(want);
            MidiConnectionService.Response resp = MidiConnectionService.decode(have);
        };

        mTaskMan.createNewTask(task).onThen((tc) -> {
            Log.i(DuckLogger.TAG, "ok");
        }).onCatch((tc) -> {
            CommonErrorHandler.handle(this, tc.error);
        }).onFinally((tc) -> {
            Log.i(DuckLogger.TAG, "done");
        }).execute();
    }

}
