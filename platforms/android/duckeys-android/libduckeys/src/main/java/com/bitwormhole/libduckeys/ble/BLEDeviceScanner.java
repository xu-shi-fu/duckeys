package com.bitwormhole.libduckeys.ble;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Handler;
import android.util.Log;

import com.bitwormhole.libduckeys.context.BaseLife;
import com.bitwormhole.libduckeys.context.Life;
import com.bitwormhole.libduckeys.helper.CommonPermissionChecker;
import com.bitwormhole.libduckeys.helper.DuckLogger;

import java.util.List;

public class BLEDeviceScanner extends BaseLife implements IBluetoothDeviceScanner {

    private final BluetoothManager mBluetoothManager;
    private final Activity mActivity;
    private final MyCallback mCallback;
    private final Handler mMainHandler;
    private IBluetoothScanningCallback mScanningCallback;
    private BluetoothDeviceListBuilder mDeviceListBuilder;
    private BluetoothScanningResult[] mResults;


    public BLEDeviceScanner(Activity a) {
        mBluetoothManager = a.getSystemService(BluetoothManager.class);
        mActivity = a;
        mCallback = new MyCallback();
        mDeviceListBuilder = new BluetoothDeviceListBuilder();
        mMainHandler = new Handler();
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


    private class MyCallback extends ScanCallback {

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.i(DuckLogger.TAG, "BLE::ScanCallback::onScanResult, callbackType=" + callbackType);

            BluetoothScanningResult item2 = this.convertResult(result);
            int state = IBluetoothScanningCallback.UPDATED;
            BLEDeviceScanner self = BLEDeviceScanner.this;

            mDeviceListBuilder.append(item2);
            mResults = mDeviceListBuilder.results(false);
            invokeCallback(state, mScanningCallback);
        }

        private BluetoothScanningResult convertResult(ScanResult src) {
            BluetoothScanningResult dst = new BluetoothScanningResult();
            BluetoothDevice dev = src.getDevice();
            dst.address = dev.getAddress();
            dst.name = BluetoothHelper.getDeviceName(dev);
            dst.ble1 = src;
            dst.ble2 = src.getScanRecord();
            return dst;
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i(DuckLogger.TAG, "BLE::ScanCallback::onBatchScanResults");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e(DuckLogger.TAG, "BLE::ScanCallback::onScanFailed, errorCode=" + errorCode);
        }
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

        BluetoothLeScanner leScanner = adapter.getBluetoothLeScanner();
        leScanner.startScan(mCallback);
    }

    public void stop() throws SecurityException {
        BluetoothAdapter adapter = mBluetoothManager.getAdapter();
        BluetoothLeScanner leScanner = adapter.getBluetoothLeScanner();
        leScanner.stopScan(mCallback);
    }

    private void invokeCallback(int state, IBluetoothScanningCallback cb) {
        if (cb == null) return;
        mMainHandler.post(() -> {
            cb.onScanningStateChanged(state, this);
        });
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
        this.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.stop();
    }

    @Override
    public Life getMyLife() {
        return this;
    }
}
