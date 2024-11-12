package com.github.xushifustudio.libduckeys.context;

import android.app.Application;

import com.github.xushifustudio.libduckeys.api.servers.Server;

public class FrameworkFacade implements FrameworkAPI {

    private final FrameworkContext mFC;

    public FrameworkFacade(FrameworkContext fc) {
        mFC = fc;
    }

    @Override
    public DuckAPI getCurrentDuck() {
        return mFC.getDuck();
    }

    @Override
    public FrameworkContext getFrameworkContext() {
        return mFC;
    }

    @Override
    public DuckAPI startup(StartupContext sc) {


  //      cfg.framework = this;
    //    cfg.registering = null;


    //    Application app = cfg.app;
      //  ComponentRegistry reg = (ComponentRegistry) app;
        ComponentContext cc = new ComponentContext();
        ComponentsLoader loader = new ComponentsLoader(cc);
      //  cc.context = app;
      //  loader.loadComponents(reg::registerComponents);

        // Server ser = cc.components.find(Server.class);


        mFC.setDuck(null); // todo
        return null;
    }

    @Override
    public void shutdown(DuckAPI duck) {
        final DuckAPI current = mFC.getDuck();
        if (current == null || duck == null) {
            return;
        }
        if (!current.equals(duck)) {
            return;
        }
        duck.shutdown();
        mFC.setDuck(null);
    }

    @Override
    public boolean isReady() {
        return false;
    }
}
