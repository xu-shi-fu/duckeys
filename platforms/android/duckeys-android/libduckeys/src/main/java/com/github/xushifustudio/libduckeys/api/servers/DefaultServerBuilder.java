package com.github.xushifustudio.libduckeys.api.servers;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public final class DefaultServerBuilder implements ServerBuilder {

    private List<Controller> mControllerList;
    private ServerContext mServerContext;
    private Context mContext;

    DefaultServerBuilder() {
        mServerContext = new ServerContext();
        mControllerList = new ArrayList<>();
    }


    @Override
    public Server create() {


        HandlerRegistry hr = new DefaultHR();
        Server ser = new DefaultServer(mServerContext);
        List<Controller> clist = mControllerList;

        mServerContext.server = ser;
        mServerContext.context = mContext;
        mServerContext.hr = hr;

        for (Controller ctl : clist) {
            ctl.registerSelf(mServerContext, hr);
        }

        return ser;
    }

    @Override
    public void addController(Controller ctl) {
        if (ctl == null) {
            return;
        }
        mControllerList.add(ctl);
    }

    @Override
    public void setServerContext(ServerContext sc) {
        if (sc == null) {
            return;
        }
        mServerContext = sc;
    }

    @Override
    public void setContext(Context ctx) {
        mContext = ctx;
        mServerContext.context = ctx;
    }
}
