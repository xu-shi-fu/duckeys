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
import com.github.xushifustudio.libduckeys.context.DuckContext;
import com.github.xushifustudio.libduckeys.helper.IO;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiUriAgent;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;
import com.github.xushifustudio.libduckeys.settings.SettingManager;
import com.github.xushifustudio.libduckeys.settings.SettingSession;
import com.github.xushifustudio.libduckeys.settings.apps.CurrentDeviceSettings;
import com.github.xushifustudio.libduckeys.settings.apps.DeviceInfo;
import com.github.xushifustudio.libduckeys.settings.apps.HistoryDeviceSettings;

import java.io.IOException;
import java.net.URI;

public final class MidiConnectionController implements Controller, ComponentLife {

    private ServerContext mSC;
    private DuckContext mDC;
    private MidiUriAgent mAgent;
    private SettingManager mSettings;


    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {

        mSC = sc;

        hr.register(Want.GET, MidiConnectionService.URI_HISTORY_LIST, this::handleQueryHistoryList);
        hr.register(Want.GET, MidiConnectionService.URI_CURRENT_STATE, this::handleQueryCurrentState);
        hr.register(Want.POST, MidiConnectionService.URI_CONNECT, this::handleConnect);
        hr.register(Want.POST, MidiConnectionService.URI_RECONNECT, this::handleReconnect);
        hr.register(Want.POST, MidiConnectionService.URI_DISCONNECT, this::handleDisconnect);

    }

    private Have handleQueryHistoryList(Want w) {
        SettingSession session = null;
        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        try {
            session = mSettings.openSession();
            HistoryDeviceSettings h0 = new HistoryDeviceSettings();
            HistoryDeviceSettings history = session.get(HistoryDeviceSettings.class, h0);
            resp.history = history.getDevices();
        } finally {
            IO.close(session);
        }
        return MidiConnectionService.encode(resp);
    }

    private Have handleQueryCurrentState(Want w) {
        SettingSession session = null;
        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        MidiUriConnection conn = mDC.getCurrentConnection();
        try {
            session = mSettings.openSession();
            CurrentDeviceSettings c0 = new CurrentDeviceSettings();
            CurrentDeviceSettings current = session.get(CurrentDeviceSettings.class, c0);
            resp.current = current.getDevice();
            resp.connected = (conn != null);
            resp.enabled = current.isEnabled();
        } finally {
            IO.close(session);
        }
        return MidiConnectionService.encode(resp);
    }

    private Have handleConnect(Want w) throws IOException {

        MidiConnectionService.Request req = MidiConnectionService.decode(w);

        MidiEventRT router = mDC.getMidiRouter();
        MidiUriAgent agent = mAgent;
        MidiUriConnection conn = null;


        try {
            // open
            String url = req.device.getUrl();
            conn = agent.open(URI.create(url), router);

            // write to history
            if (req.writeToHistory) {
                this.writeToHistory(req);
            }

            // swap newer & older
            MidiUriConnection olderConn = mDC.getCurrentConnection();
            mDC.setCurrentConnection(conn);
            router.setTx(conn.getTx());
            conn = olderConn;
        } finally {
            IO.close(conn);
        }

        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        return MidiConnectionService.encode(resp);
    }


    private Have handleDisconnect(Want w) throws IOException {
        MidiUriConnection conn = null;
        MidiEventRT router = mDC.getMidiRouter();
        try {
            conn = mDC.getCurrentConnection();
            mDC.setCurrentConnection(null);
            router.setTx(null);
        } finally {
            IO.close(conn);
        }
        MidiConnectionService.Response resp = new MidiConnectionService.Response();
        return MidiConnectionService.encode(resp);
    }


    private Have handleReconnect(Want w) throws IOException {

        // 重新构建请求
        MidiConnectionService.Request req = new MidiConnectionService.Request();

        // disconnect
        w = MidiConnectionService.encode(req);
        w.method = Want.POST;
        w.url = MidiConnectionService.URI_DISCONNECT;
        Have h = this.handleDisconnect(w);

        // read settings
        SettingSession session = null;
        CurrentDeviceSettings current = new CurrentDeviceSettings();
        try {
            Thread.sleep(1000);
            session = mSettings.openSession();
            current = session.get(CurrentDeviceSettings.class, current);
            req.device = current.getDevice();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            IO.close(session);
        }

        // connect
        w = MidiConnectionService.encode(req);
        w.method = Want.POST;
        w.url = MidiConnectionService.URI_CONNECT;
        h = this.handleConnect(w);

        return h;
    }


    private void writeToHistory(MidiConnectionService.Request req) {
        SettingSession session = null;
        DeviceInfo dev = req.device;
        if (dev == null) {
            return;
        }
        long now = System.currentTimeMillis();
        dev.setConnectedAt(now);
        try {
            session = mSettings.openSession();
            HistoryDeviceSettings history = session.get(HistoryDeviceSettings.class);
            CurrentDeviceSettings current = session.get(CurrentDeviceSettings.class);
            if (history == null) {
                history = new HistoryDeviceSettings();
            }
            if (current == null) {
                current = new CurrentDeviceSettings();
            }

            current.setDevice(dev);
            history.add(dev);
            history.dedup();

            int scope = current.scope();
            session.put(history, scope);
            session.put(current, scope);
            session.commit();
        } finally {
            IO.close(session);
        }
    }

    private void onCreate(ComponentContext cc) {
        mAgent = cc.components.find(MidiUriAgent.class);
        mDC = cc.components.find(DuckContext.class);
        mSettings = cc.components.find(SettingManager.class);
    }

    @Override
    public ComponentRegistration init(ComponentContext cc) {
        ComponentRegistrationBuilder b = ComponentRegistrationBuilder.newInstance(cc);
        b.setInstance(this);
        b.onCreate(() -> {
            onCreate(cc);
        });
        return b.create();
    }
}
