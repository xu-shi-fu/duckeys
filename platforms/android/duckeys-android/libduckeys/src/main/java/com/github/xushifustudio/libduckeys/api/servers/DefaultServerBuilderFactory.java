package com.github.xushifustudio.libduckeys.api.servers;

import com.github.xushifustudio.libduckeys.api.controllers.Controllers;

import java.util.List;

public final class DefaultServerBuilderFactory implements ServerBuilderFactory {


    private DefaultServerBuilderFactory() {
    }

    @Override
    public ServerBuilder newBuilder() {

        /*

        ServerBuilder builder = new DefaultServerBuilder();
        List<Controller> all = Controllers.listAll();
        for (Controller controller : all) {
            builder.addController(controller);
        }
        return builder;

         */

        throw new RuntimeException("deprecated: use DuckConfig");
    }


    public static ServerBuilderFactory getInstance() {
        return new DefaultServerBuilderFactory();
    }
}
