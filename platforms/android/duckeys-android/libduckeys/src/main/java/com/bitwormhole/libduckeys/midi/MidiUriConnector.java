package com.bitwormhole.libduckeys.midi;

import java.net.URI;

import java.io.IOException;

public interface MidiUriConnector {

    MidiUriConnection open(URI uri) throws IOException;

}
