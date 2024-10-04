package com.github.xushifustudio.libduckeys.api.clients;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.github.xushifustudio.libduckeys.context.BaseLife;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;

import java.util.concurrent.Executor;

public final class TaskManager extends BaseLife {

    private final ContextState mState;
    private final Handler mHandler;
    private final Context mContext;


    public TaskManager(Context ctx) {
        mHandler = new Handler();
        mState = new ContextState();
        mContext = ctx;
    }


    @Override
    public void onStart() {
        super.onStart();
        mState.running = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mState.running = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mState.working = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mState.working = true;
    }

    public TaskContext createNewTask(Task task) {
        TaskContext tc = new TaskContext();
        tc.worker = getDefaultWorker();
        tc.handler = mHandler;
        tc.context = mContext;
        tc.state = mState;
        tc.task = task;
        return tc;
    }

    public  Worker   getDefaultWorker() {
        return new DefaultWorker();
    }

    public Worker getMidiWorker() {
        Log.w(DuckLogger.TAG, "todo: impl  getMidiWorker");
        return new DefaultWorker();
    }

    public Worker getBackgroundWorker() {
        Log.w(DuckLogger.TAG, "todo: impl  getBackgroundWorker");
        return new DefaultWorker();
    }

    public Worker getForegroundWorker() {
        Log.w(DuckLogger.TAG, "todo: impl  getForegroundWorker");
        return new DefaultWorker();
    }
}
