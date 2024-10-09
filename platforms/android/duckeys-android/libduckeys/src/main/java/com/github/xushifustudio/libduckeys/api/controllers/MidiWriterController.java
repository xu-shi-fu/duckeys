package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.MidiWriterService;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.ComponentRegistrationBuilder;
import com.github.xushifustudio.libduckeys.context.DuckContext;

public final class MidiWriterController implements Controller, ComponentLife {

    // const
    private final static String URI = MidiWriterService.URI;

    // var
    private DuckContext mDC;


    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {
        hr.register(Want.GET, URI, this::handleWrite);
        hr.register(Want.POST, URI, this::handleWrite);
    }

    private Have handleWrite(Want w) {
        MidiWriterService.Request req = MidiWriterService.decode(w);
        mDC.getMidiRouter().getTx().dispatch(req.event);
        return new Have();
    }

    @Override
    public ComponentRegistration init(ComponentContext cc) {
        ComponentRegistrationBuilder crb = ComponentRegistrationBuilder.newInstance(cc);
        crb.onCreate(() -> {
            this.onCreate(cc);
        });
        return crb.create();
    }


    private void onCreate(ComponentContext cc) {
        mDC = cc.components.find(DuckContext.class);
    }
}
