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
import com.bitwormhole.libduckeys.midi.NopMERT;

import java.io.IOException;
import java.net.URI;

public final class MockMidiConnection implements MidiUriConnection {

    private final URI mURI;
    private final NopMERT mRT;


    public MockMidiConnection() {
        mURI = URI.create("mock://midi@localhost");
        mRT = new NopMERT();
    }


    @Override
    public void connect() throws IOException, SecurityException {
    }

    @Override
    public void disconnect() throws IOException {
    }

    @Override
    public MidiEventDispatcher getTx() {
        return mRT;
    }

    @Override
    public URI getURI() {
        return mURI;
    }


    @Override
    public void setRx(MidiEventHandler rx) {
    }

    @Override
    public void close() throws IOException {
    }
}
