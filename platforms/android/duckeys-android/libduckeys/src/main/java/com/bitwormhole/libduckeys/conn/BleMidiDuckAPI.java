package com.bitwormhole.libduckeys.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;

import com.bitwormhole.libduckeys.midi.MERT;
import com.bitwormhole.libduckeys.midi.MidiEvent;
import com.bitwormhole.libduckeys.midi.MidiEventDispatcher;
import com.bitwormhole.libduckeys.midi.MidiEventHandler;
import com.bitwormhole.libduckeys.midi.MidiEventRT;

import java.util.UUID;

// 参考：
// https://developer.android.google.cn/develop/connectivity/bluetooth/ble/transfer-ble-data?hl=zh-cn#java

public class BleMidiDuckAPI implements MERT {

    private BluetoothGatt mGatt;
    private BluetoothGattService mGattService;
    private BluetoothGattCharacteristic mGattCharUp;
    private BluetoothGattCharacteristic mGattCharDown;
    private final MidiEventRT mRT;
    private BluetoothGattServerCallback mGattServerCallback;

    public BleMidiDuckAPI() {
        mRT = new MidiEventRT();
        mRT.setTx((event) -> {
            onWrite(event);
        });
    }

    private final static String UUID_SERVICE = "00000ad9-0000-1000-8000-00805f9b34fb";
    private final static String UUID_CHAR_UP = "e7ea0001-8e30-2e58-bdb6-0987991661e8";
    private final static String UUID_CHAR_DOWN = "e7ea0002-8e30-2e58-bdb6-0987991661e8";

    public static BleMidiDuckAPI getInstance(BluetoothGatt gatt) {

        UUID uuidService = UUID.fromString(UUID_SERVICE);
        UUID uuidCharUp = UUID.fromString(UUID_CHAR_UP);
        UUID uuidCharDown = UUID.fromString(UUID_CHAR_DOWN);

        BluetoothGattService service = gatt.getService(uuidService);
        BluetoothGattCharacteristic charUp = service.getCharacteristic(uuidCharUp);
        BluetoothGattCharacteristic charDown = service.getCharacteristic(uuidCharDown);

        BleMidiDuckAPI api = new BleMidiDuckAPI();
        api.mGatt = gatt;
        api.mGattService = service;
        api.mGattCharUp = charUp;
        api.mGattCharDown = charDown;

        return api;
    }

    @Override
    public MidiEventDispatcher getTx() {
        return mRT;
    }

    @Override
    public void setRx(MidiEventHandler rx) {
        mRT.setRx(rx);
    }

    private void onWrite(MidiEvent me) throws SecurityException {
        BluetoothGattCharacteristic ch = mGattCharDown;
        ch.setValue("todo ...");
        mGatt.writeCharacteristic(ch);
    }
}
