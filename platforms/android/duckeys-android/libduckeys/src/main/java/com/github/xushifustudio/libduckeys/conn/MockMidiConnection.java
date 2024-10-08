package com.github.xushifustudio.libduckeys.conn;

import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

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
        }
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
