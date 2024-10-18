package com.github.xushifustudio.libduckeys.conn.androidmidi;

import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.util.Log;

import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.ComponentRegistrationBuilder;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.helper.IO;
import com.github.xushifustudio.libduckeys.helper.Sleeper;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnector;

import java.io.IOException;
import java.net.URI;

//  mURI = URI.create("android://media.midi/MidiDeviceInfo/{id}");

public class AndroidMidiConnector implements MidiUriConnector, ComponentLife {

    private MidiManager mMidiMan;

    public AndroidMidiConnector() {
    }


    private void onCreate(ComponentContext cc) {
        Context ctx = cc.context;
        mMidiMan = (MidiManager) ctx.getSystemService(Context.MIDI_SERVICE);
    }


    private MidiDeviceInfo findDeviceInfo(URI uri) {
        String path = uri.getPath();
        MidiDeviceInfo[] all = mMidiMan.getDevices();
        for (MidiDeviceInfo info : all) {
            int id = info.getId();
            String want = "/" + id;
            if (path.endsWith(want)) {
                return info;
            }
        }
        throw new RuntimeException("no MidiDeviceInfo with URI [" + uri + "]");
    }

    private MidiDeviceInfo.PortInfo findPortInfo(MidiDeviceInfo info, int wantType) {
        MidiDeviceInfo.PortInfo[] plist = info.getPorts();
        for (MidiDeviceInfo.PortInfo item : plist) {
            if (item.getType() == wantType) {
                return item;
            }
        }
        return null;
    }


    private static class MyOnDeviceOpenedListener implements MidiManager.OnDeviceOpenedListener {

        private MidiDevice mDevice;

        @Override
        public void onDeviceOpened(MidiDevice midiDevice) {
            this.mDevice = midiDevice;
        }

        MidiDevice waitForMidiDeviceReady(final int timeout) throws IOException {
            final int step = 100;
            for (int ttl = timeout; ttl > 0; ttl -= step) {
                MidiDevice dev = mDevice;
                if (dev != null) {
                    return dev;
                }
                Sleeper.sleep(step);
            }
            throw new IOException("timeout");
        }
    }


    @Override
    public boolean supports(URI uri) {
        String str = uri.toString();
        return str.startsWith("android:");
    }

    @Override
    public MidiUriConnection open(URI uri, MidiEventHandler h) throws IOException {
        AndroidMidiConnection conn = null;
        AndroidMidiConnection result = null;
        AndroidMidiContext ctx = new AndroidMidiContext();
        try {
            MidiDeviceInfo info = findDeviceInfo(uri);
            MyOnDeviceOpenedListener listener = new MyOnDeviceOpenedListener();
            mMidiMan.openDevice(info, listener, null);
            MidiDevice device = listener.waitForMidiDeviceReady(3000);

            MidiDeviceInfo.PortInfo in1 = findPortInfo(info, MidiDeviceInfo.PortInfo.TYPE_INPUT);
            MidiDeviceInfo.PortInfo out1 = findPortInfo(info, MidiDeviceInfo.PortInfo.TYPE_OUTPUT);
            MidiInputPort in2 = null;
            MidiOutputPort out2 = null;
            if (in1 != null) {
                in2 = device.openInputPort(in1.getPortNumber());
            }
            if (out1 != null) {
                out2 = device.openOutputPort(out1.getPortNumber());
            }

            ctx.uri = uri;
            ctx.handler = h;
            ctx.manager = mMidiMan;
            ctx.info = info;
            ctx.device = device;
            ctx.input = in2;
            ctx.output = out2;

            AndroidMidiRx.newInstance(ctx);
            conn = new AndroidMidiConnection(ctx);

            // ok
            result = conn;
            conn = null;
        } finally {
            IO.close(conn);
        }
        Log.i(DuckLogger.TAG, "connect to " + uri);
        return result;
    }


    @Override
    public ComponentRegistration init(ComponentContext cc) {
        ComponentRegistrationBuilder b = new ComponentRegistrationBuilder();
        b.setType(this.getClass());
        b.setInstance(this);
        b.onCreate(() -> {
            onCreate(cc);
        });
        return b.create();
    }
}
