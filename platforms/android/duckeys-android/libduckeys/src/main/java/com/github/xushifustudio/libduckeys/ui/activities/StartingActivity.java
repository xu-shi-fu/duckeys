package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.services.AppMainService;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.CommonErrorHandler;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;

public class StartingActivity extends LifeActivity {


    private TaskManager mTaskMan;
    private DuckClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.layout_starting);
    }

    @Override
    protected void onStart() {
        super.onStart();
        start();
    }

    private void init() {

        mTaskMan = new TaskManager(this);
        mClient = new DuckClient(this);

        LifeManager lm = getLifeManager();
        lm.add(mTaskMan);
        lm.add(mClient);
    }

    private void start() {
        AppMainService.Holder holder = new AppMainService.Holder();
        Task t = (tc) -> {
            AppMainService.Request req = new AppMainService.Request();
            Want want = AppMainService.encode(req);
            want.method = Want.POST;
            want.url = AppMainService.URI_APP_INIT;

            Server ser = mClient.waitForServerReady(15 * 1000);
            Have have = ser.invoke(want);

            AppMainService.Response resp = AppMainService.decode(have);
            holder.response = resp;
            holder.request = req;
        };
        mTaskMan.createNewTask(t).onThen((tc) -> {
            Log.i(DuckLogger.TAG, "OK");
        }).onCatch((tc) -> {
            CommonErrorHandler.handle(this, tc.error, CommonErrorHandler.FLAG_ALERT);
        }).execute();
    }
}
