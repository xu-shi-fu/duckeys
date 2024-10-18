package com.github.xushifustudio.libduckeys.conn.androidmidi;

import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiInputPort;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;

import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnector;

import java.io.IOException;
import java.net.URI;

class AndroidMidiContext {

    public URI uri;

    public MidiManager manager;
    public MidiEventHandler handler;
    public MidiDevice device;
    public MidiDeviceInfo info;

    public MidiInputPort input;
    public MidiOutputPort output;

    public boolean closed;

}
