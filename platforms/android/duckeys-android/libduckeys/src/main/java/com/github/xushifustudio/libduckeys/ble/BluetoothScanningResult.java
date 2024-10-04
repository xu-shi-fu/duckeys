package com.github.xushifustudio.libduckeys.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;

public class BluetoothScanningResult {

    public int index; // 在设备列表中的 index
    public String name; // the device name
    public String address; // the device address
    public BluetoothDevice device;


    public ScanResult ble1;
    public ScanRecord ble2;

}
