package com.github.xushifustudio.libduckeys.api.servers;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.services.MidiReaderService;
import com.github.xushifustudio.libduckeys.api.services.MidiWriterService;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.Life;
import com.github.xushifustudio.libduckeys.context.LifeProxy;

import java.net.URI;
import java.util.List;

public class DefaultServer implements Server, ComponentLife {

    private final ServerContext mSC;

    public DefaultServer(ServerContext sc) {
        mSC = sc;
    }

    public DefaultServer() {
        mSC = new ServerContext();
    }


    @Override
    public Have invoke(Want want) throws Exception {
        API api = findAPI(want);
        return api.invoke(want);
    }

    @Override
    public API findAPI(Want want) {
        return mSC.hr.findAPI(want);
    }

    @Override
    public API getMidiWriterAPI() {
        Want want = new Want();
        want.method = Want.GET;
        want.url = MidiWriterService.URI;
        return findAPI(want);
    }

    @Override
    public API getMidiReaderAPI() {
        Want want = new Want();
        want.method = Want.GET;
        want.url = MidiReaderService.URI;
        return findAPI(want);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private void scanAPIs() {
        HandlerRegistry dst = mSC.hr;
        List<Object> src = mSC.components.listAll();
        for (Object item : src) {
            if (item instanceof Controller) {
                Controller ctl = (Controller) item;
                ctl.registerSelf(mSC, dst);
            }
        }
    }


    @Override
    public ComponentRegistration init(ComponentContext cc) {

        LifeProxy lp = new LifeProxy();
        lp.setOnCreate(() -> {
            sleep(1);
            scanAPIs();
        });
        lp.setOnStart(() -> {
            sleep(2);
        });


        ComponentRegistration cr = new ComponentRegistration();
        cr.instance = this;
        cr.type = this.getClass();
        cr.life = lp;
        cr.order = -10;

        mSC.components = cc.components;
        mSC.context = cc.context;
        mSC.server = this;
        mSC.hr = new DefaultHR();
        mSC.currentConnection = null;

        return cr;
    }
}
