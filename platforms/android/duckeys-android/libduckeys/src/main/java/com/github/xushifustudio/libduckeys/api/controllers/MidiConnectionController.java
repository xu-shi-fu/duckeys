package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.MidiConnectionService;
import com.github.xushifustudio.libduckeys.helper.IO;
import com.github.xushifustudio.libduckeys.midi.MidiUriAgent;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

import java.io.IOException;
import java.net.URI;

public final class MidiConnectionController implements Controller {

    private ServerContext mSC;

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

        MidiUriAgent agent = MidiUriAgent.getInstance(mSC.context);
        MidiUriConnection conn = null;

        try {
            String url = req.device.getUrl();
            conn = agent.open(URI.create(url));


            MidiUriConnection olderConn = mSC.currentConnection;
            mSC.currentConnection = conn;
            conn = olderConn;
        } finally {
            IO.close(conn);
        }


        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        return MidiConnectionService.encode(resp);
    }
}
