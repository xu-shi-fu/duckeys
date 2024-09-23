package com.bitwormhole.libduckeys.conn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import androidx.annotation.NonNull;

import com.bitwormhole.libduckeys.midi.MidiEventDispatcher;
import com.bitwormhole.libduckeys.midi.MidiEventHandler;
import com.bitwormhole.libduckeys.midi.MidiUriConnection;

import java.io.IOException;
import java.net.URI;

public final class BleMidiConnection implements MidiUriConnection {

    private final URI mURI;
    private final BluetoothDevice mDevice;
    private final Context mContext;
    private final BluetoothGattCallback mCallback = new MyGattCallback();

    private BluetoothGatt mGatt;

    public BleMidiConnection(Context ctx, URI uri, BluetoothDevice dev) {
        mContext = ctx;
        mDevice = dev;
        mURI = uri;
    }


    private class MyGattCallback extends BluetoothGattCallback {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
            super.onCharacteristicRead(gatt, characteristic, value, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value) {
            super.onCharacteristicChanged(gatt, characteristic, value);
        }
    }

    @Override
    public URI getURI() {
        return mURI;
    }

    @Override
    public void connect() throws IOException, SecurityException {
        boolean auto = true;
        mDevice.connectGatt(mContext, auto, mCallback);
    }

    @Override
    public void disconnect() throws IOException {

    }

    @Override
    public MidiEventDispatcher getTx() {
        return null;
    }

    @Override
    public void setRx(MidiEventHandler rx) {

    }

    @Override
    public void close() throws IOException {

    }
}
