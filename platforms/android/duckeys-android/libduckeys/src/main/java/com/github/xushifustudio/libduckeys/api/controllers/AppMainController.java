package com.github.xushifustudio.libduckeys.api.controllers;

import android.util.Log;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.AppMainService;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.LifeProxy;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.helper.IO;
import com.github.xushifustudio.libduckeys.settings.SettingManager;
import com.github.xushifustudio.libduckeys.settings.SettingSession;
import com.github.xushifustudio.libduckeys.settings.apps.CurrentDeviceSettings;
import com.github.xushifustudio.libduckeys.settings.apps.CurrentUserSettings;

public final class AppMainController implements Controller, ComponentLife {

    private SettingManager mSettingManager;

    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {
        hr.register(Want.POST, AppMainService.URI_APP_INIT, this::handleInit);
    }


    private Have handleInit(Want want) throws Exception {
        SettingSession session = null;
        try {
            session = mSettingManager.openSession();
            CurrentUserSettings cu = session.get(CurrentUserSettings.class);
            CurrentDeviceSettings cd = session.get(CurrentDeviceSettings.class);

            Log.i(DuckLogger.TAG, "" + cu);
            Log.i(DuckLogger.TAG, "" + cd);

        } finally {
            IO.close(session);
        }
        return new Have();
    }

    @Override
    public ComponentRegistration init(ComponentContext cc) {

        LifeProxy lp = new LifeProxy();
        lp.setOnCreate(() -> {
            onCreate(cc);
        });

        ComponentRegistration cr = new ComponentRegistration();
        cr.life = lp;
        return cr;
    }

    private void onCreate(ComponentContext cc) {
        mSettingManager = cc.components.find(SettingManager.class);
    }
}
