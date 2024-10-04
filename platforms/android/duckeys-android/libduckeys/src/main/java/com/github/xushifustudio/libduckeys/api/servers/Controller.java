package com.github.xushifustudio.libduckeys.api.servers;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Want;

public interface Controller {

    void registerSelf(ServerContext sc, HandlerRegistry hr);

}
