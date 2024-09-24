package com.bitwormhole.libduckeys.conn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bitwormhole.libduckeys.helper.DuckLogger;
import com.bitwormhole.libduckeys.midi.MERT;
import com.bitwormhole.libduckeys.midi.MidiEventDispatcher;
import com.bitwormhole.libduckeys.midi.MidiEventHandler;
import com.bitwormhole.libduckeys.midi.MidiEventRT;
import com.bitwormhole.libduckeys.midi.MidiUriConnection;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BleMidiConnection implements MidiUriConnection {

    private final URI mURI;
    private final BluetoothDevice mDevice;
    private final Context mContext;
    private final BluetoothGattCallback mCallback = new MyGattCallback();
    private final List<MERT> mListMERT = new ArrayList<>();
    private final MidiEventRT mMidiEventRT = new MidiEventRT();

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
            if (status == BluetoothGatt.GATT_SUCCESS) {
                switch (newState) {
                    case BluetoothProfile.STATE_CONNECTED:
                        Log.i(DuckLogger.TAG, "onConnectionStateChange(STATE_CONNECTED)");
                        onBleDeviceConnected(gatt);
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        Log.i(DuckLogger.TAG, "onConnectionStateChange(STATE_DISCONNECTED)");
                        onBleDeviceDisconnected(gatt);
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(DuckLogger.TAG, "onServicesDiscovered()");
                checkoutBleMidi1p0(gatt);
                checkoutBleMidiDuck(gatt);
            }
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

    private void onBleDeviceConnected(BluetoothGatt gatt) throws SecurityException {
        mGatt = gatt;
        gatt.discoverServices();
    }

    private void onBleDeviceDisconnected(BluetoothGatt gatt) throws SecurityException {
        mMidiEventRT.setTx(null);
        mGatt = null;
    }


    private void checkoutBleMidi1p0(BluetoothGatt gatt) throws SecurityException {
        try {
            BleMidi1p0API api = BleMidi1p0API.getInstance(gatt);
            api.setRx(mMidiEventRT);
            mMidiEventRT.setTx(api.getTx());
            mListMERT.add(api);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkoutBleMidiDuck(BluetoothGatt gatt) {
        try {
            BleMidiDuckAPI api = BleMidiDuckAPI.getInstance(gatt);
            api.setRx(mMidiEventRT);
            mMidiEventRT.setTx(api.getTx());
            mListMERT.add(api);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
    public void disconnect() throws IOException, SecurityException {
        mGatt.disconnect();
    }

    @Override
    public MidiEventDispatcher getTx() {
        return mMidiEventRT;
    }

    @Override
    public void setRx(MidiEventHandler rx) {
        mMidiEventRT.setRx(rx);
    }

    @Override
    public void close() throws IOException {

    }
}
