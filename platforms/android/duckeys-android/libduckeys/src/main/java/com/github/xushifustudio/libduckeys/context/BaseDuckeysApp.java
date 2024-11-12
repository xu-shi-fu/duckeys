package com.github.xushifustudio.libduckeys.context;

import android.app.Application;
import android.content.Context;

import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.servers.ServerHolder;
import com.github.xushifustudio.libduckeys.helper.AppCrashHandler;

import java.util.ArrayList;
import java.util.List;

public class BaseDuckeysApp extends Application implements ComponentRegistry {

    private FrameworkAPI mFramework;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCrashHandler.setup(this);
        this.mFramework = this.createFramework();
    }

    private FrameworkAPI createFramework() {
        FrameworkContext fc = new FrameworkContext();
        FrameworkAPI api = new FrameworkFacade(fc);
        fc.setApi(api);
        return api;
    }

    // 注册组件
    @Override
    public void registerComponents(ComponentRegistering cr) {

        Context ctx = this.getApplicationContext();
        DuckConfiguration cfg = new DuckConfiguration();

  //      cfg.registry = this;
   //     cfg.registering = cr;
    //    cfg.framework = this.mFramework;
     //   cfg.context = ctx;

        DuckConfig.configure(ctx, cfg);
    }
}
