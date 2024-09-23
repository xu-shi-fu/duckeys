package com.bitwormhole.libduckeys.conn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;

import com.bitwormhole.libduckeys.midi.MERT;
import com.bitwormhole.libduckeys.midi.MidiEventDispatcher;
import com.bitwormhole.libduckeys.midi.MidiEventHandler;

public class BleMidi1p0API implements MERT {

    private BluetoothGatt mGatt;
    private BluetoothGattCharacteristic mGattChar;

    public BleMidi1p0API() {
    }

    public static BleMidi1p0API getInstance(BluetoothGatt gatt) {
        return new BleMidi1p0API();
    }

    @Override
    public MidiEventDispatcher getTx() {
        return null;
    }

    @Override
    public void setRx(MidiEventHandler rx) {

    }
}
