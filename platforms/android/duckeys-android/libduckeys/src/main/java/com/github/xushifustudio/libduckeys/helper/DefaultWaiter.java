package com.github.xushifustudio.libduckeys.helper;

public class DefaultWaiter implements Waiter {

    @Override
    public void wait(String tag, long timeout) throws InterruptedException {
        synchronized (this) {
            this.wait(timeout);
        }
    }

    @Override
    public void notify(String tag) {
        synchronized (this) {
            this.notify();
        }
    }
}
