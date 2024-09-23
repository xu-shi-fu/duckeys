package com.bitwormhole.libduckeys.ble;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.bitwormhole.libduckeys.context.BaseLife;
import com.bitwormhole.libduckeys.context.Life;
import com.bitwormhole.libduckeys.helper.CommonPermissionChecker;
import com.bitwormhole.libduckeys.helper.DuckLogger;

import java.util.List;

public class OlderBluetoothDeviceScanner extends BaseLife implements IBluetoothDeviceScanner {

    private final BluetoothManager mBluetoothManager;
    private final MyBroadcastReceiver mBroadcastReceiver;
    private final Activity mActivity;
    private final Handler mMainHandler;
    private IBluetoothScanningCallback mScanningCallback;
    private BluetoothDeviceListBuilder mDeviceListBuilder;
    private BluetoothScanningResult[] mResults;

    public OlderBluetoothDeviceScanner(Activity a) {
        mBluetoothManager = a.getSystemService(BluetoothManager.class);
        mBroadcastReceiver = new MyBroadcastReceiver();
        mActivity = a;
        mMainHandler = new Handler();
        mDeviceListBuilder = new BluetoothDeviceListBuilder();
    }

    private String[] listPermissionsWant() {
        String[] plist = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
        };
        return plist;
    }


    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            final String tag = DuckLogger.TAG;
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str = stringifyDevice(device);
                Log.i(tag, str);
                handleDevice(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i(tag, "ACTION_DISCOVERY_STARTED");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(tag, "ACTION_DISCOVERY_FINISHED");
            }
        }
    }


    @SuppressLint("MissingPermission")
    private static String stringifyDevice(BluetoothDevice dev) {
        StringBuilder b = new StringBuilder();
        b.append("find bluetooth device,");
        b.append(" address: ").append(dev.getAddress());
        b.append(" name:").append(dev.getName());
        return b.toString();
    }

    private void handleDevice(BluetoothDevice dev) {

        BluetoothScanningResult item2 = this.convertResult(dev);
        final int state = IBluetoothScanningCallback.UPDATED;

        mDeviceListBuilder.append(item2);
        mResults = mDeviceListBuilder.results(false);
        invokeCallback(state, mScanningCallback);
    }

    private BluetoothScanningResult convertResult(BluetoothDevice src) {
        BluetoothScanningResult dst = new BluetoothScanningResult();
        dst.device = src;
        dst.name = BluetoothHelper.getDeviceName(src);
        dst.index = 0;
        dst.ble1 = null;
        dst.ble2 = null;
        dst.address = src.getAddress();
        return dst;
    }


    @Override
    public void setResultsHandler(IBluetoothScanningCallback h) {
        mScanningCallback = h;
    }

    public void start() throws SecurityException {
        BluetoothAdapter adapter = mBluetoothManager.getAdapter();
        if (adapter == null) {
            throw new RuntimeException("蓝牙可能没打开！");
        }
        String[] plist = listPermissionsWant();
        CommonPermissionChecker.check(mActivity, plist);
        adapter.startDiscovery();

    }


    public void stop() throws SecurityException {
        BluetoothAdapter adapter = mBluetoothManager.getAdapter();
        if (adapter == null) {
            throw new RuntimeException("蓝牙可能没打开！");
        }
        adapter.cancelDiscovery();
    }


    private void invokeCallback(int state, IBluetoothScanningCallback cb) {
        if (cb == null) return;
        mMainHandler.post(() -> {
            cb.onScanningStateChanged(state, this);
        });
    }


    @Override
    public Life getMyLife() {
        return this;
    }

    @Override
    public BluetoothScanningResult[] listResults() {
        BluetoothScanningResult[] all = mResults;
        if (all == null) {
            all = new BluetoothScanningResult[0];
        }
        return all;
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mActivity.registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mActivity.unregisterReceiver(mBroadcastReceiver);
    }
}
