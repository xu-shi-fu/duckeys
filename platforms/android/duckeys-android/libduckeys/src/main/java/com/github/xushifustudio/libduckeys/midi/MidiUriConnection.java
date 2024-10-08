package com.github.xushifustudio.libduckeys.midi;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public interface MidiUriConnection extends Closeable {

    URI getURI();

    MidiEventDispatcher getTx();

    MidiEventHandler getRx();

}
