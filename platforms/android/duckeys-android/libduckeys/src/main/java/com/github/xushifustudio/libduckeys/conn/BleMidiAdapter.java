package com.github.xushifustudio.libduckeys.conn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.github.xushifustudio.libduckeys.helper.Hex;
import com.github.xushifustudio.libduckeys.midi.MERT;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class BleMidiAdapter implements MERT {


    private final static String UUID_SERVICE_BLE_MIDI1 = "03B80E5A-EDE8-4B33-A751-6CE34EC4C700";
    private final static String UUID_CHAR_BLE_MIDI1 = "7772E5DB-3868-4112-A1A9-F2669D106BF3";
    private final static String UUID_CHAR_ABOUT = "8E5169B6-3642-41BD-9957-F0039FCA0280";
    private final static String UUID_CHAR_UPSTREAM = "6C2A6D6C-B649-47B2-B947-F04D1BC1D2FC";
    private final static String UUID_CHAR_DOWNSTREAM = "55F891E6-22AB-44AA-B720-7510BB8F07FC";


    private final BluetoothGatt mGatt;
    private final BluetoothGattService mGattService;
    private final BluetoothGattCharacteristic mGattCharStandard;
    private final BluetoothGattCharacteristic mGattCharUpstream;
    private final BluetoothGattCharacteristic mGattCharDownstream;
    private final BluetoothGattCharacteristic mGattCharAbout;


    private final MidiEventRT mMidiEventRT = new MyEventRT();


    private BleMidiAdapter(Builder b) {
        mGatt = b.gatt;
        mGattService = b.service;
        mGattCharAbout = b.about;
        mGattCharStandard = b.standard;
        mGattCharUpstream = b.up;
        mGattCharDownstream = b.down;
    }

    public static BleMidiAdapter getInstance(BluetoothGatt gatt) {
        Builder b = new Builder();
        b.gatt = gatt;
        b.service = b.findService(UUID_SERVICE_BLE_MIDI1);
        b.standard = b.findCharacteristic(UUID_CHAR_BLE_MIDI1);
        b.about = b.findCharacteristic(UUID_CHAR_ABOUT);
        b.up = b.findCharacteristic(UUID_CHAR_UPSTREAM);
        b.down = b.findCharacteristic(UUID_CHAR_DOWNSTREAM);
        return b.create();
    }

    private static final class Builder {

        private BluetoothGatt gatt;
        private BluetoothGattService service;

        private BluetoothGattCharacteristic standard;
        private BluetoothGattCharacteristic about;
        private BluetoothGattCharacteristic up;
        private BluetoothGattCharacteristic down;


        public BluetoothGattService findService(String uuid) {
            UUID want = UUID.fromString(uuid);
            BluetoothGattService ser = gatt.getService(want);
            if (ser == null) {
                throw new RuntimeException("no GATT service with UUID: " + want);
            }
            return ser;
        }

        public BluetoothGattCharacteristic findCharacteristic(String uuid) {
            UUID want = UUID.fromString(uuid);
            BluetoothGattCharacteristic ch = service.getCharacteristic(want);
            if (ch == null) {
                throw new RuntimeException("no GATT characteristic with UUID: " + want);
            }
            return ch;
        }

        public BleMidiAdapter create() {
            return new BleMidiAdapter(this);
        }
    }

    private class MyEventRT extends MidiEventRT {

        @Override
        public void dispatch(MidiEvent me) {
            super.dispatch(me);
            sendData(me.data, me.offset, me.count);
        }
    }


    private void sendData(byte[] data, int offset, int length) {

        StringBuilder sb = new StringBuilder();
        int end = offset + length;
        for (int i = offset; i < end; i++) {
            Hex.stringify(data[i], sb);
            sb.append(',');
        }
        sb.append('\t');

        BluetoothGattCharacteristic ch1 = mGattCharUpstream;
        BluetoothGatt gatt = mGatt;

        try {
            BluetoothGattCharacteristic ch2 = prepareWrite(gatt, ch1);
            String value = sb.toString();
            ch2.setValue(value);
            gatt.writeCharacteristic(ch2);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private BluetoothGattCharacteristic prepareWrite(BluetoothGatt gatt, BluetoothGattCharacteristic ch) {
        BluetoothGattService service = gatt.getService(ch.getService().getUuid());
        return service.getCharacteristic(ch.getUuid());
    }

    @Override
    public MidiEventDispatcher getTx() {
        return mMidiEventRT;
    }

    @Override
    public void setRx(MidiEventHandler rx) {
        mMidiEventRT.setRx(rx);
    }
}
