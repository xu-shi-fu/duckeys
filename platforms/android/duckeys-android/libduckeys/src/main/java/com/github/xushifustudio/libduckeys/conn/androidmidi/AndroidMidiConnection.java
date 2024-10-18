package com.github.xushifustudio.libduckeys.conn.androidmidi;

import com.github.xushifustudio.libduckeys.helper.IO;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnector;

import java.io.IOException;
import java.net.URI;

public class AndroidMidiConnection implements MidiUriConnection, MidiEventDispatcher {

    private final AndroidMidiContext mContext;

    public AndroidMidiConnection(AndroidMidiContext ctx) {
        mContext = ctx;
    }

    @Override
    public URI getURI() {
        return mContext.uri;
    }

    @Override
    public MidiEventDispatcher getTx() {
        return this;
    }

    @Override
    public MidiEventHandler getRx() {
        return mContext.handler;
    }

    @Override
    public void close() throws IOException {
        IO.close(mContext.input);
        IO.close(mContext.output);
        IO.close(mContext.device);
        mContext.closed = true;
    }

    @Override
    public void dispatch(MidiEvent me) {
        if (me == null) {
            return;
        }
        try {
            byte[] b = me.data;
            int off = me.offset;
            int len = me.count;
            if ((b != null) && (len > 0)) {
                mContext.input.send(b, off, len);
            }
            if (me.flush) {
                mContext.input.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
