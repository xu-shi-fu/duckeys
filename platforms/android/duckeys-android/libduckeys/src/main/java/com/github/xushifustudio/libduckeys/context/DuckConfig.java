package com.github.xushifustudio.libduckeys.context;

import android.content.Context;

import com.github.xushifustudio.libduckeys.api.controllers.AppMainController;
import com.github.xushifustudio.libduckeys.api.controllers.BleScannerController;
import com.github.xushifustudio.libduckeys.api.controllers.CurrentDeviceController;
import com.github.xushifustudio.libduckeys.api.controllers.HistoryDeviceController;
import com.github.xushifustudio.libduckeys.api.controllers.MidiConnectionController;
import com.github.xushifustudio.libduckeys.api.controllers.MidiReaderController;
import com.github.xushifustudio.libduckeys.api.controllers.MidiWriterController;
import com.github.xushifustudio.libduckeys.api.controllers.SettingsController;
import com.github.xushifustudio.libduckeys.api.servers.DefaultServer;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.conn.BleMidiConnector;
import com.github.xushifustudio.libduckeys.conn.MockMidiConnector;
import com.github.xushifustudio.libduckeys.conn.UsbMidiConnector;
import com.github.xushifustudio.libduckeys.conn.WifiMidiConnector;
import com.github.xushifustudio.libduckeys.midi.DefaultMidiUriAgent;
import com.github.xushifustudio.libduckeys.midi.MidiUriAgent;
import com.github.xushifustudio.libduckeys.settings.DefaultSettingManager;
import com.github.xushifustudio.libduckeys.settings.SettingManager;
import com.github.xushifustudio.libduckeys.settings.apps.AppSettingBank;
import com.github.xushifustudio.libduckeys.settings.apps.CurrentDeviceSettings;
import com.github.xushifustudio.libduckeys.settings.apps.CurrentUserSettings;
import com.github.xushifustudio.libduckeys.settings.apps.HistoryDeviceSettings;
import com.github.xushifustudio.libduckeys.settings.users.UserSettingBank;

public final class DuckConfig {

    private DuckConfig() {
    }

    public static void configure(Context ctx, ComponentRegistering cr) {

        final String nil = null;

        // api (controllers)
        cr.register(nil, new AppMainController());
        cr.register(nil, new BleScannerController());
        cr.register(nil, new CurrentDeviceController());
        cr.register(nil, new HistoryDeviceController());
        cr.register(nil, new MidiConnectionController());
        cr.register(nil, new MidiReaderController());
        cr.register(nil, new MidiWriterController());
        cr.register(nil, new SettingsController());


        // connectors
        cr.register(nil, new BleMidiConnector());
        cr.register(nil, new UsbMidiConnector());
        cr.register(nil, new WifiMidiConnector());
        cr.register(nil, new MockMidiConnector());


        // settings
        cr.register(nil, new UserSettingBank());
        cr.register(nil, new AppSettingBank());

        cr.register(nil, new CurrentUserSettings());
        cr.register(nil, new CurrentDeviceSettings());
        cr.register(nil, new HistoryDeviceSettings());


        // others
        cr.register(nil, new LifeMonitor("components.life.monitor"));
        cr.register(Server.class, new DefaultServer());
        cr.register(SettingManager.class, new DefaultSettingManager());
        cr.register(DuckContext.class, new DuckContext());
        cr.register(MidiUriAgent.class, new DefaultMidiUriAgent());
    }
}
