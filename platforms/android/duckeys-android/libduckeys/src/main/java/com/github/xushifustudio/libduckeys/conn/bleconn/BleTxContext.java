package com.github.xushifustudio.libduckeys.conn.bleconn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.Arrays;

public class BleTxContext {

    public final BleConnContext parent;

    public final BluetoothGatt gatt;
    public final BluetoothGattService service;
    public final BluetoothGattCharacteristic characteristic;

    public final byte[] buffer;
    public final int offset;
    public final int length;
    public boolean done;
    public long maxAge;
    public final long startedAt;

    public BleTxContext(BleConnContext ctx, BluetoothGattCharacteristic ch, byte[] b, int off, int len) {
        this.parent = ctx;

        this.gatt = ctx.gatt;
        this.service = ctx.service;
        this.characteristic = ch;

        this.buffer = b;
        this.offset = off;
        this.length = len;

        this.startedAt = System.currentTimeMillis();
        this.maxAge = 1000;
    }


    public void send() throws SecurityException {
        BluetoothGattCharacteristic ch = characteristic;
        final int from = offset;
        final int to = from + length;
        byte[] data = Arrays.copyOfRange(buffer, from, to);
        ch.setValue(data);
        gatt.writeCharacteristic(ch);
    }


    public static BleTxContext newInstance(BleConnContext ctx, BluetoothGattCharacteristic ch, byte[] b) {
        return new BleTxContext(ctx, ch, b, 0, b.length);
    }
}
