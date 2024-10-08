package com.github.xushifustudio.libduckeys.midi;

import android.content.Context;

import com.github.xushifustudio.libduckeys.conn.BleMidiConnector;
import com.github.xushifustudio.libduckeys.conn.UsbMidiConnector;
import com.github.xushifustudio.libduckeys.conn.VirtualMidiConnector;
import com.github.xushifustudio.libduckeys.conn.WifiMidiConnector;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.ComponentRegistrationBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


// uri like 'ble://midi@0.0.0.0/a/b?x=yz'

public final class DefaultMidiUriAgent implements MidiUriAgent, ComponentLife {

    private Context context;
    private MidiUriConnector[] connectors;
    private ComponentContext componentContext;

    public DefaultMidiUriAgent() {
    }


    private void loadConnectors() {
        List<MidiUriConnector> dst = new ArrayList<>();
        List<Object> src = this.componentContext.components.listAll();
        for (Object item : src) {
            if (item instanceof MidiUriConnector) {
                MidiUriConnector con = (MidiUriConnector) item;
                dst.add(con);
            }
        }
        int size = dst.size();
        this.connectors = dst.toArray(new MidiUriConnector[size]);
    }


    @Override
    public MidiUriConnector findConnector(URI uri) {
        MidiUriConnector[] all = this.connectors;
        for (MidiUriConnector con : all) {
            if (con.supports(uri)) {
                return con;
            }
        }
        throw new RuntimeException("no MidiUriConnector can handle the URI [" + uri + "]");
    }


    @Override
    public MidiUriConnection open(URI uri) throws IOException {
        MidiUriConnector connector = findConnector(uri);
        return connector.open(uri);
    }

    @Override
    public ComponentRegistration init(ComponentContext cc) {
        ComponentRegistrationBuilder b = ComponentRegistrationBuilder.newInstance(cc);
        b.setType(DefaultMidiUriAgent.class);
        b.setInstance(this);
        b.onCreate(this::loadConnectors);
        this.componentContext = cc;
        this.context = cc.context;
        return b.create();
    }
}
