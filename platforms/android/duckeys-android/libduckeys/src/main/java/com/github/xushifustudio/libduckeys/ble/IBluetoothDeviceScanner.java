package com.github.xushifustudio.libduckeys.ble;

import com.github.xushifustudio.libduckeys.context.Life;

import java.util.List;

public interface IBluetoothDeviceScanner {

    void setResultsHandler(IBluetoothScanningCallback h);

    void start();

    void stop();

    Life getMyLife();

    BluetoothScanningResult[] listResults();

}
