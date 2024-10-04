package com.github.xushifustudio.libduckeys.api.clients;

import android.content.Context;
import android.os.Handler;

import java.util.concurrent.Executor;

public final class TaskContext {

    // public
    public Handler handler;
    public Task task;
    public Worker worker;
    public Context context;
    public ContextState state;
    public Throwable error;


    // private
    Listener mThenListener;
    Listener mCatchListener;
    Listener mFinallyListener;


    public TaskContext() {
    }

    public void execute() {
        TaskRuntime rt = new TaskRuntime(this);
        this.worker.execute(rt);
    }

    public interface Listener {
        void handle(TaskContext tc);
    }


    public TaskContext onThen(Listener l) {
        mThenListener = l;
        return this;
    }

    public TaskContext onCatch(Listener l) {
        mCatchListener = l;
        return this;
    }

    public TaskContext onFinally(Listener l) {
        mFinallyListener = l;
        return this;
    }
}
