package com.github.xushifustudio.libduckeys.conn.bleconn;

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

public final class BleAdapter implements MidiEventDispatcher {


    private final static String UUID_SERVICE_BLE_MIDI1 = "03B80E5A-EDE8-4B33-A751-6CE34EC4C700";
    private final static String UUID_CHAR_BLE_MIDI1 = "7772E5DB-3868-4112-A1A9-F2669D106BF3";
    private final static String UUID_CHAR_ABOUT = "8E5169B6-3642-41BD-9957-F0039FCA0280";
    private final static String UUID_CHAR_UPSTREAM = "6C2A6D6C-B649-47B2-B947-F04D1BC1D2FC";
    private final static String UUID_CHAR_DOWNSTREAM = "55F891E6-22AB-44AA-B720-7510BB8F07FC";


    private final BleConnContext mContext;


    private BleAdapter(BleConnContext ctx) {
        this.mContext = ctx;
    }

    public static BleAdapter getInstance(BleConnContext ctx, BluetoothGatt gatt) {
        Builder b = new Builder();
        b.context = ctx;
        b.gatt = gatt;
        b.service = b.findService(UUID_SERVICE_BLE_MIDI1);
        b.standard = b.findCharacteristic(UUID_CHAR_BLE_MIDI1);
        b.about = b.findCharacteristic(UUID_CHAR_ABOUT);
        b.up = b.findCharacteristic(UUID_CHAR_UPSTREAM);
        b.down = b.findCharacteristic(UUID_CHAR_DOWNSTREAM);
        return b.create();
    }

    @Override
    public void dispatch(MidiEvent me) {
        String row = null;
        if (me != null) {
            if (me.data != null) {
                row = Hex.stringify(me.data, me.offset, me.count);
            }
        }
        if (row == null) {
            return;
        }
        final String row2 = row;
        mContext.runWithinTransaction(() -> {
            mContext.txBuffer.append(row2);
            mContext.txBuffer.append('\n');
        });
        this.flush();
    }

    public void flush() {
        mContext.runWithinTransaction(() -> {
            BleTxContext prev = mContext.dispatching;
            if (isBusy(prev)) {
                return;
            }
            String text = mContext.txBuffer.toString();
            mContext.txBuffer.setLength(0);
            if (text.length() == 0) {
                return;
            }
            BluetoothGattCharacteristic up = mContext.gattCharUpstream;
            byte[] data = text.getBytes(StandardCharsets.UTF_8);
            BleTxContext next = BleTxContext.newInstance(mContext, up, data);
            mContext.dispatching = next;
            next.send();
        });
    }


    public static interface Listener {
        void handle(BleAdapter adapter);
    }

    private static final class Builder {

        private BleConnContext context;

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

        public BleAdapter create() {

            context.gatt = this.gatt;
            context.service = this.service;
            context.gattCharAbout = this.about;
            context.gattCharStandard = this.standard;
            context.gattCharUpstream = this.up;
            context.gattCharDownstream = this.down;

            context.adapter = new BleAdapter(this.context);
            context.rtx.setTx(context.adapter);

            return context.adapter;
        }
    }


    private boolean isBusy(BleTxContext btc) {
        if (btc == null) {
            return false;
        }
        if (btc.done) {
            return false;
        }
        long now = System.currentTimeMillis();
        long t1 = btc.startedAt - 100;
        long t2 = btc.startedAt + btc.maxAge;
        return (t1 < now && now < t2);
    }
}
