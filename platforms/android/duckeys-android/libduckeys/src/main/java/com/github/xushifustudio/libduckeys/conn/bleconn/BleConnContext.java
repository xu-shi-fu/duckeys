package com.github.xushifustudio.libduckeys.conn.bleconn;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;

import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

import java.net.URI;

public class BleConnContext {


    public URI uri;
    public BluetoothDevice device;
    public Context context;
    public Throwable error;
    public boolean ready;


    public BluetoothGatt gatt;
    public MidiUriConnection connection;
    public BluetoothGattCallback callback;

    public BluetoothGattService service;
    public BluetoothGattCharacteristic gattCharStandard;
    public BluetoothGattCharacteristic gattCharUpstream;
    public BluetoothGattCharacteristic gattCharDownstream;
    public BluetoothGattCharacteristic gattCharAbout;

    public BleAdapter adapter;

    public final MidiEventRT rtx;

    // 在访问这些字段时，需要启用事务:
    public BleTxContext dispatching;
    public final StringBuilder txBuffer;
    public int txBufferCapacity;


    public BleConnContext() {
        rtx = new MidiEventRT("owner:BleConnContext");
        callback = new BleGattCallback(this);
        txBuffer = new StringBuilder();
        txBufferCapacity = 32;
    }

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forAdapter(BleAdapter.Listener l) {
        BleAdapter ada = this.adapter;
        if (ada == null || l == null) {
            return;
        }
        l.handle(ada);
    }

    public synchronized void runWithinTransaction(Runnable r) {
        r.run();
    }
}
