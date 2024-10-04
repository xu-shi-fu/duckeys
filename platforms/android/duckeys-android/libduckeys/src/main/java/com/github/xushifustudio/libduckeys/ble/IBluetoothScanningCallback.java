package com.github.xushifustudio.libduckeys.ble;

public interface IBluetoothScanningCallback {

    int START = 1;
    int UPDATED = 2;
    int FINISH = 3;

    void onScanningStateChanged(int state, IBluetoothDeviceScanner scanner);

}
