package com.bitwormhole.libduckeys.conn;

import com.bitwormhole.libduckeys.midi.MidiUriConnection;
import com.bitwormhole.libduckeys.midi.MidiUriConnector;

import java.io.IOException;
import java.net.URI;

public class WifiMidiConnector implements MidiUriConnector {
    @Override
    public MidiUriConnection open(URI uri) throws IOException {
        return null;
    }
}
