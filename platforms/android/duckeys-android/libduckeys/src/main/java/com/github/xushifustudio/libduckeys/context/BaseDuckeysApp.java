package com.github.xushifustudio.libduckeys.context;

import android.app.Application;
import android.content.Context;

import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.servers.ServerHolder;
import com.github.xushifustudio.libduckeys.helper.AppCrashHandler;

import java.util.ArrayList;
import java.util.List;

public class BaseDuckeysApp extends Application implements ComponentRegistry {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCrashHandler.setup(this);
    }

    // 注册组件
    @Override
    public void registerComponents(ComponentRegistering cr) {
        Context ctx = this.getApplicationContext();
        DuckConfig.configure(ctx, cr);
    }
}
