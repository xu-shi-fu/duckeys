package com.bitwormhole.libduckeys.context;

import android.app.Application;

import com.bitwormhole.libduckeys.helper.AppCrashHandler;

public class BaseDuckeysApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCrashHandler.setup(this);
    }

}
