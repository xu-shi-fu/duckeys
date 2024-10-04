package com.github.xushifustudio.libduckeys.api.servers;

import android.content.Context;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Want;

public interface ServerBuilder {

    Server create();

    void addController(Controller ctl);

    void setServerContext(ServerContext sc);

    void setContext(Context ctx);

}
