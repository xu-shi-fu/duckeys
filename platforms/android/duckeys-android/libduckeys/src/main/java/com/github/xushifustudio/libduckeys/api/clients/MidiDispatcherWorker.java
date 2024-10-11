package com.github.xushifustudio.libduckeys.api.clients;

import com.github.xushifustudio.libduckeys.context.BaseLife;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MidiDispatcherWorker extends BaseLife implements Worker {

    private MyWorking mCurrentWorking;
    private final List<TaskRuntime> mTaskList;

    public MidiDispatcherWorker() {
        List<TaskRuntime> tlist = new ArrayList<>();
        mTaskList = Collections.synchronizedList(tlist);
    }

    private class MyWorking implements Runnable {

        @Override
        public void run() {
            while (alive()) {
                try {
                    if (mTaskList.size() == 0) {
                        this.wait(1000);
                        continue;
                    }
                    TaskRuntime item = mTaskList.remove(0);
                    runTask(item);
                } catch (Exception e) {
                    e.toString();
                }
            }
        }

        boolean alive() {
            return this.equals(mCurrentWorking);
        }

        void add(TaskRuntime task) {
            mTaskList.add(task);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        MyWorking wk = new MyWorking();
        Thread th = new Thread(wk);
        mCurrentWorking = wk;
        th.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCurrentWorking = null;
    }

    @Override
    public void execute(TaskRuntime rt) {
        MyWorking wk = mCurrentWorking;
        if (wk == null || rt == null) {
            return;
        }
        wk.add(rt);
        synchronized (wk) {
            wk.notify();
        }
    }

    private void runTask(TaskRuntime rt) {
        if (rt == null) {
            return;
        }
        if (rt.runnable == null) {
            return;
        }
        rt.runnable.run();
    }
}
