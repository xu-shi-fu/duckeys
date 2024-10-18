package com.github.xushifustudio.libduckeys.conn;

import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public final class MockMidiConnection implements MidiUriConnection {

    private final URI mURI;
    private final MidiEventHandler mRx;
    private final MidiEventDispatcher mTx;

    public MockMidiConnection(URI uri, MidiEventHandler rx) {
        mURI = uri;
        mRx = rx;
        mTx = new MyDispatcher();
    }

    private class MyDispatcher implements MidiEventDispatcher {

        @Override
        public void dispatch(MidiEvent me) {
            String msg = "MockMidiConnection.dispatch:event:" + me;
            Log.i(DuckLogger.TAG, msg);
            tryEcho(me);
        }
    }

    private void tryEcho(MidiEvent req) {

        if (req == null) {
            return;
        }
        if (req.data == null) {
            return;
        }

        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        final int end = req.offset + req.count;
        final byte[] prefix = {'e', 'c', 'h', 'o', ' '};
        final byte[] buffer = req.data;
        int k = 0;

        for (int i = req.offset; i < end; i++, k++) {
            byte have = buffer[i];
            if (k < prefix.length) {
                byte want = prefix[k];
                if (have != want) {
                    return;
                }
            } else {
                result.write(have);
            }
        }

        MidiEvent resp = new MidiEvent();
        resp.data = result.toByteArray();
        resp.count = result.size();
        if (resp.count == 0) {
            return;
        }
        mRx.handle(resp);
    }

    @Override
    public MidiEventDispatcher getTx() {
        return mTx;
    }

    @Override
    public URI getURI() {
        return mURI;
    }


    @Override
    public MidiEventHandler getRx() {
        return mRx;
    }

    @Override
    public void close() throws IOException {
    }
}
