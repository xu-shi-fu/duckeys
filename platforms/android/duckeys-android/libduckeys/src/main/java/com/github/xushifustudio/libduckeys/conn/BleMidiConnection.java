package com.github.xushifustudio.libduckeys.conn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.midi.MERT;
import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public final class BleMidiConnection implements MidiUriConnection {

    private final URI mURI;
    private final BluetoothDevice mDevice;
    private final Context mContext;
    private final BluetoothGattCallback mCallback = new MyGattCallback();
    private final List<MERT> mListMERT = new ArrayList<>();
    private final MidiEventRT mMidiEventRT = new MidiEventRT();

    private BluetoothGatt mGatt;
    private Throwable mError;
    private boolean mReady;

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
            BleMidiAdapter api = BleMidiAdapter.getInstance(gatt);
            api.setRx(mMidiEventRT);
            mMidiEventRT.setTx(api.getTx());
            mListMERT.add(api);
            mReady = true;
        } catch (SecurityException e) {
            e.printStackTrace();
            mError = e;
        } catch (Exception e) {
            // e.printStackTrace();
            mError = e;
        }
    }


    @Override
    public URI getURI() {
        return mURI;
    }


    public void connect(int timeout) throws IOException, SecurityException {
        boolean auto = true;
        mDevice.connectGatt(mContext, auto, mCallback);

        // wait for connected
        final int step = 30; // in ms
        for (int ttl = timeout; ttl > 0; ttl -= step) {
            Throwable err = mError;
            if (mReady) {
                return;
            }
            if (err != null) {
                throw new IOException(err);
            }
            this.sleep(step);
        }
        throw new IOException("timeout");
    }

    private void sleep(int step) {
        try {
            Thread.sleep(step);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() throws IOException, SecurityException {
        if (mGatt == null) {
            return;
        }
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
        this.disconnect();
    }
}
