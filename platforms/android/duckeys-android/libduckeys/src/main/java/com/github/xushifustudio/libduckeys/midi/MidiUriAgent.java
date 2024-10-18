package com.github.xushifustudio.libduckeys.midi;

import java.io.IOException;
import java.net.URI;


// uri like 'ble://midi@0.0.0.0/a/b?x=yz'

public interface MidiUriAgent {

    MidiUriConnector findConnector(URI uri);

    MidiUriConnection open(URI uri, MidiEventHandler rx) throws IOException;

}
