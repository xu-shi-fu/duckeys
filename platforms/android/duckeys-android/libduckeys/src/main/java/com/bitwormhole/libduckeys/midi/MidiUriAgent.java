package com.bitwormhole.libduckeys.midi;

import android.content.Context;

import com.bitwormhole.libduckeys.conn.BleMidiConnector;
import com.bitwormhole.libduckeys.conn.UsbMidiConnector;
import com.bitwormhole.libduckeys.conn.VirtualMidiConnector;
import com.bitwormhole.libduckeys.conn.WifiMidiConnector;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;


// uri like 'ble://midi@0.0.0.0/a/b?x=yz'

public final class MidiUriAgent {

    private final Context context;

    public MidiUriAgent(Context ctx) {
        context = ctx;
    }

    public static MidiUriAgent getInstance(Context ctx) {
        return new MidiUriAgent(ctx);
    }

    public MidiUriConnector findConnector(URI uri) throws IOException {
        String path = uri.getPath() + ""; // 必须 == '/midi'
        String scheme = uri.getScheme() + "";  // = [wifi|ble|usb|virtual]

        if (!path.equals("/midi")) {
            throw new IOException("path != '/midi'");
        }

        if ("virtual".equals(scheme)) {
            return new VirtualMidiConnector();
        } else if ("ble".equals(scheme)) {
            return new BleMidiConnector(context);
        } else if ("wifi".equals(scheme)) {
            return new WifiMidiConnector();
        } else if ("usb".equals(scheme)) {
            return new UsbMidiConnector();
        } else {
            throw new IOException("scheme != [ble|wifi|usb|virtual]");
        }
    }

    public MidiUriConnection open(URI uri) throws IOException {
        MidiUriConnector connector = findConnector(uri);
        return connector.open(uri);
    }
}
