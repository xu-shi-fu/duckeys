package com.github.xushifustudio.libduckeys.midi;

import java.net.URI;

import java.io.IOException;

public interface MidiUriConnector {

    boolean supports(URI uri);

    MidiUriConnection open(URI uri, MidiEventHandler h) throws IOException;

}
