package com.github.xushifustudio.libduckeys.conn.bleconn;

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

public final class BleConnection implements MidiUriConnection {

    private final BleConnContext mContext;

    public BleConnection(Context ctx, URI uri, BluetoothDevice dev, MidiEventHandler h) {
        BleConnContext cc = new BleConnContext();
        cc.context = ctx;
        cc.device = dev;
        cc.uri = uri;
        cc.rtx.setRx(h);
        mContext = cc;
    }


    @Override
    public URI getURI() {
        return mContext.uri;
    }

    @Override
    public MidiEventDispatcher getTx() {
        return mContext.rtx;
    }

    @Override
    public MidiEventHandler getRx() {
        return mContext.rtx;
    }


    public void connect(int timeout) throws IOException, SecurityException {
        boolean auto = true;
        Context ctx = mContext.context;
        BluetoothDevice device = mContext.device;
        BluetoothGattCallback callback = mContext.callback;
        device.connectGatt(ctx, auto, callback);

        // wait for connected
        final int step = 30; // in ms
        for (int ttl = timeout; ttl > 0; ttl -= step) {
            Throwable err = mContext.error;
            if (mContext.ready) {
                return;
            }
            if (err != null) {
                throw new IOException(err);
            }
            mContext.sleep(step);
        }
        throw new IOException("timeout");
    }


    private void disconnect() throws IOException, SecurityException {
        BluetoothGatt gatt = mContext.gatt;
        if (gatt == null) {
            return;
        }
        gatt.disconnect();
    }

    @Override
    public void close() throws IOException {
        this.disconnect();
    }
}
