package com.bitwormhole.libduckeys.midi;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public interface MidiUriConnection extends Closeable {

    URI getURI();

    void connect() throws IOException;

    void disconnect() throws IOException;

}
