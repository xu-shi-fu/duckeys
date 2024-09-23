package com.bitwormhole.libduckeys.midi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.bitwormhole.libduckeys.context.BaseLife;
import com.bitwormhole.libduckeys.helper.DuckLogger;

public class MidiRouterServiceController extends BaseLife {

    private final Activity mActivity;
    private final Intent mServiceIntent;

    public MidiRouterServiceController(Activity a) {
        mActivity = a;
        mServiceIntent = new Intent(a, MidiRouterService.class);
    }

    private MidiRouterBinder mMidiRouterBinder;

    private final ServiceConnection mMidiRouterConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder ib) {
            mMidiRouterBinder = (MidiRouterBinder) ib;

            long now = System.currentTimeMillis();
            String msg = mMidiRouterBinder.hello("ts_" + now);
            Log.i(DuckLogger.TAG, "onServiceConnected >>> " + msg);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMidiRouterBinder = null;
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
        // unbind
        mActivity.unbindService(mMidiRouterConn);
    }

    @Override
    public void onResume() {
        super.onResume();
        // bind
        mActivity.bindService(mServiceIntent, mMidiRouterConn, Context.BIND_AUTO_CREATE);
    }


    private void startService() {
        Context ctx = mActivity.getApplicationContext();
        ctx.startForegroundService(mServiceIntent);
    }

    private void stopService() {
    }
}
