package com.github.xushifustudio.libduckeys.conn;

import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnector;
import com.github.xushifustudio.libduckeys.midi.NopMERT;

import java.io.IOException;
import java.net.URI;


//         mURI = URI.create("mock://midi@localhost");
public final class MockMidiConnector implements MidiUriConnector {

    public MockMidiConnector() {
    }

    @Override
    public boolean supports(URI uri) {
        String str = uri.toString();
        return str.startsWith("mock:");
    }

    @Override
    public MidiUriConnection open(URI uri) throws IOException {
        return new MockMidiConnection(uri);
    }
}
