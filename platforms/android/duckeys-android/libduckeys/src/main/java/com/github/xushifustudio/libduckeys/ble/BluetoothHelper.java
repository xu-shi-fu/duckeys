package com.github.xushifustudio.libduckeys.ble;

import android.bluetooth.BluetoothDevice;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class BluetoothHelper {

    private BluetoothHelper() {
    }

    public static String getDeviceName(BluetoothDevice dev) {
        try {
            return dev.getName();
        } catch (SecurityException e) {
            return e.getMessage();
        }
    }
}
