package com.github.xushifustudio.libduckeys.context;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;

public class DuckClient extends BaseLife {

    private final Activity mActivity;
    private final Intent mServiceIntent;
    private DuckBinder mBinder;


    public DuckClient(Activity a) {
        mActivity = a;
        mServiceIntent = new Intent(a, DuckService.class);
    }

    private final ServiceConnection mServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder ib) {
            mBinder = (DuckBinder) ib;
            // long now = System.currentTimeMillis();
            // String msg = mMidiRouterBinder.hello("ts_" + now);
            // Log.i(DuckLogger.TAG, "onServiceConnected >>> " + msg);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder = null;
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        this.startService();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.stopService();
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.unbindService(mServiceConn);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.bindService(mServiceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }


    public final Server waitForServerReady() {
        return waitForServerReady(3000);
    }


    public final Server waitForServerReady(int timeout) {
        Server server = mBinder;
        int ttl = timeout;
        final int step = 20;
        while (ttl > 0) {
            if (server != null) {
                return server;
            }
            try {
                Thread.sleep(step);
                ttl -= step;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            server = mBinder;
        }
        throw new RuntimeException("waitForServerReady: timeout");
    }


    private void startService() {
        Context ctx = mActivity.getApplicationContext();
        ctx.startForegroundService(mServiceIntent);
    }

    private void stopService() {
    }
}
