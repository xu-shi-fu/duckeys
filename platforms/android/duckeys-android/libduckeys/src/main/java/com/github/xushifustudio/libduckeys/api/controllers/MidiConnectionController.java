package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.ComponentRegistrationBuilder;
import com.github.xushifustudio.libduckeys.helper.IO;
import com.github.xushifustudio.libduckeys.midi.MidiUriAgent;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

import java.io.IOException;
import java.net.URI;

public final class MidiConnectionController implements Controller, ComponentLife {

    private ServerContext mSC;
    private MidiUriAgent mAgent;

    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {

        mSC = sc;

        hr.register(Want.GET, MidiConnectionService.URI_HISTORY_LIST, this::handleQueryHistoryList);
        hr.register(Want.GET, MidiConnectionService.URI_CURRENT_STATE, this::handleQueryCurrentState);
        hr.register(Want.POST, MidiConnectionService.URI_CONNECT, this::handleConnect);

    }

    private Have handleQueryHistoryList(Want w) {


        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        resp.history = null;
        return MidiConnectionService.encode(resp);
    }

    private Have handleQueryCurrentState(Want w) {


        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        resp.current = null;
        return MidiConnectionService.encode(resp);
    }

    private Have handleConnect(Want w) throws IOException {

        MidiConnectionService.Request req = MidiConnectionService.decode(w);

        MidiUriAgent agent = mAgent;
        MidiUriConnection conn = null;

        try {
            // open
            String url = req.device.getUrl();
            conn = agent.open(URI.create(url));

            // save to settings

            // swap newer & older
            MidiUriConnection olderConn = mSC.currentConnection;
            mSC.currentConnection = conn;
            conn = olderConn;
        } finally {
            IO.close(conn);
        }


        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        return MidiConnectionService.encode(resp);
    }

    private void loadAgent(ComponentContext cc) {
        mAgent = cc.components.find(MidiUriAgent.class);
    }

    @Override
    public ComponentRegistration init(ComponentContext cc) {
        ComponentRegistrationBuilder b = ComponentRegistrationBuilder.newInstance(cc);
        b.setInstance(this);
        b.onCreate(() -> {
            loadAgent(cc);
        });
        return b.create();
    }
}
