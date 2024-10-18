package com.github.xushifustudio.libduckeys.helper;

public interface Waiter {

    void wait(String tag, long timeout) throws InterruptedException;

    void notify(String tag);

}
