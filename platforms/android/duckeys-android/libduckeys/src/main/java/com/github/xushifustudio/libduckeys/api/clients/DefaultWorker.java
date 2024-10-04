package com.github.xushifustudio.libduckeys.api.clients;

import java.util.concurrent.Executor;

public class DefaultWorker implements Worker {
    @Override
    public void execute(final TaskRuntime rt) {
        Thread th = new Thread(rt.runnable);
        th.start();
    }
}
