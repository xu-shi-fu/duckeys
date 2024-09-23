package com.bitwormhole.libduckeys.ble;

import com.bitwormhole.libduckeys.context.Life;

import java.util.List;

public interface IBluetoothDeviceScanner {

    void setResultsHandler(IBluetoothScanningCallback h);

    void start();

    void stop();

    Life getMyLife();

    BluetoothScanningResult[] listResults();

}
