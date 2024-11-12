package com.github.xushifustudio.libduckeys.context;

public interface DuckAPI {

    UserAPI getCurrentUser();

    DuckContext getDuckContext();

    void shutdown();
}
