package com.github.xushifustudio.libduckeys.midi;

import android.content.Context;

import com.github.xushifustudio.libduckeys.conn.BleMidiConnector;
import com.github.xushifustudio.libduckeys.conn.UsbMidiConnector;
import com.github.xushifustudio.libduckeys.conn.VirtualMidiConnector;
import com.github.xushifustudio.libduckeys.conn.WifiMidiConnector;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;


// uri like 'ble://midi@0.0.0.0/a/b?x=yz'

public interface MidiUriAgent {

    MidiUriConnector findConnector(URI uri);

    MidiUriConnection open(URI uri, MidiEventHandler rx) throws IOException;

}
