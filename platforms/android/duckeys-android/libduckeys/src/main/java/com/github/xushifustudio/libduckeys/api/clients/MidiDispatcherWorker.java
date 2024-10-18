package com.github.xushifustudio.libduckeys.api.clients;

import com.github.xushifustudio.libduckeys.context.BaseLife;
import com.github.xushifustudio.libduckeys.helper.DefaultWaiter;
import com.github.xushifustudio.libduckeys.helper.Waiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MidiDispatcherWorker extends BaseLife implements Worker {

    private MyWorking mCurrentWorking;
    private final List<TaskRuntime> mTaskList;
    private final Waiter mWaiter;
    private final String mWaiterTag = "MidiDispatcherWorker";

    public MidiDispatcherWorker() {
        List<TaskRuntime> tlist = new ArrayList<>();
        mTaskList = Collections.synchronizedList(tlist);
        mWaiter = new DefaultWaiter();
    }

    private class MyWorking implements Runnable {

        @Override
        public void run() {
            while (alive()) {
                try {
                    if (mTaskList.size() == 0) {
                        mWaiter.wait(mWaiterTag, 1000);
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
        mWaiter.notify(mWaiterTag);
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
