package com.github.xushifustudio.libduckeys.context;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public abstract class LifeService extends Service {

    private final LifeManager mLifeManager;
    private boolean mStarted;

    public LifeService() {
        mLifeManager = new LifeManager();
    }

    public final LifeManager getLifeManager() {
        return mLifeManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mLifeManager.getMain().onCreate(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mStarted) {
            mLifeManager.getMain().onRestart();
        } else {
            mLifeManager.getMain().onStart();
            mStarted = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mLifeManager.getMain().onDestroy();
    }
}
