package com.bitwormhole.libduckeys.helper;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

public final class AppCrashHandler {

    private AppCrashHandler() {
    }

    public static void setup(Application app) {
        setupMainThreadErrorHandler(app);
        setupWorkerThreadErrorHandler(app);
    }

    private static void setupMainThreadErrorHandler(Application app) {
        Looper lp = Looper.getMainLooper();
        Handler h = new Handler(lp);
        h.post(() -> {
            for (; ; ) {
                try {
                    Looper.loop();
                } catch (Throwable e) {
                    CommonErrorHandler.handle(app, e);
                }
            }
        });
    }

    private static void setupWorkerThreadErrorHandler(Application app) {

        final Handler h = new Handler();

        // Thread.UncaughtExceptionHandler older = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thd, err) -> {
            h.post(() -> {
                CommonErrorHandler.handle(app, err);
            });
        });
    }
}
