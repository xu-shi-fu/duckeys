package com.github.xushifustudio.libduckeys.conn;

import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnector;

import java.io.IOException;
import java.net.URI;

public class UsbMidiConnector implements MidiUriConnector {
    @Override
    public MidiUriConnection open(URI uri) throws IOException {
        return null;
    }
}
