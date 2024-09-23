package com.bitwormhole.libduckeys.conn;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import com.bitwormhole.libduckeys.midi.MidiUriConnection;
import com.bitwormhole.libduckeys.midi.MidiUriConnector;

import java.io.IOException;
import java.net.URI;

public class BleMidiConnector implements MidiUriConnector {

    private final BluetoothManager mBTM;
    private final Context mContext;

    public BleMidiConnector(Context context) {
        mBTM = context.getSystemService(BluetoothManager.class);
        mContext = context;
    }

    @Override
    public MidiUriConnection open(URI uri) throws IOException {

        String addr = uri.getHost() + "";
        addr = addr.replace('-', ':');
        addr = addr.replace('.', ':');
        addr = addr.toUpperCase();

        BluetoothAdapter adapter = mBTM.getAdapter();
        BluetoothDevice device = adapter.getRemoteDevice(addr);


        return new BleMidiConnection(mContext, uri, device);
    }
}
