package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.AppMainService;
import com.github.xushifustudio.libduckeys.api.services.ExampleService;

public final class ExampleController implements Controller {

    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {
        hr.register(Want.POST, ExampleService.URI, this::handle);
    }

    private Have handle(Want want) throws Exception {
        return new Have();
    }
}
