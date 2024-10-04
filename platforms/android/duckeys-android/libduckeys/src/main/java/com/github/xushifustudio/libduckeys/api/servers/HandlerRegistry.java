package com.github.xushifustudio.libduckeys.api.servers;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Want;

public interface HandlerRegistry {

    void register(String method, String url, API api);

    API findAPI(String method, String url);

    API findAPI(Want want);

}
