package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.MidiWriterService;

public final class MidiWriterController implements Controller {

    final static String URI = MidiWriterService.URI;

    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {


        hr.register(Want.GET, URI, this::h1);
        hr.register(Want.POST, URI, this::h2);

    }

    private Have h1(Want w) {
        return null;
    }

    private Have h2(Want w) {
        return null;
    }

}
