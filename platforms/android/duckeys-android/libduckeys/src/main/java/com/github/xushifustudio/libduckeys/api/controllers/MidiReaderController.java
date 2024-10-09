package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.MidiReaderService;
import com.github.xushifustudio.libduckeys.api.services.MidiWriterService;

public final class MidiReaderController implements Controller {

    private final static String URI = MidiReaderService.URI;


    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {
        hr.register(Want.GET, URI, this::handleRead);
        hr.register(Want.POST, URI, this::handleRead);
    }

    private Have h1(Want w) {
        return null;
    }

    private Have handleRead(Want w) {

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Have();
    }
}
