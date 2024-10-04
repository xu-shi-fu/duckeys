package com.github.xushifustudio.libduckeys.api.clients;

import android.content.Context;
import android.os.Handler;

public final class TaskRuntime {

    public final Runnable runnable;
    public final TaskContext context;


    public TaskRuntime(TaskContext tc) {
        this.context = tc;
        this.runnable = new TaskProxy(tc.task);
    }

    private class TaskProxy implements Runnable {

        private final Task target;

        TaskProxy(Task task) {
            this.target = task;
        }

        void invokeListener(final TaskContext.Listener l) {
            if (l == null) {
                return;
            }
            context.handler.post(() -> {
                l.handle(context);
            });
        }

        @Override
        public void run() {
            try {
                this.target.run(context);
                invokeListener(context.mThenListener);
            } catch (Exception e) {
                context.error = e;
                invokeListener(context.mCatchListener);
            } finally {
                invokeListener(context.mFinallyListener);
            }
        }
    }
}
