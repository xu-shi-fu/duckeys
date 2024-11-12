package com.github.xushifustudio.libduckeys.context;

public interface FrameworkAPI {

    DuckAPI getCurrentDuck();

    FrameworkContext getFrameworkContext();

    DuckAPI startup(StartupContext sc);

    void shutdown(DuckAPI duck);

    boolean isReady();
}
