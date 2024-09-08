package com.bitwormhole.libduckeys.context;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.helper.DuckLogger;

public class LifeMonitor implements Life {

    private final static String theDefaultTag = DuckLogger.TAG;
    private final String mTag;

    public LifeMonitor() {
        mTag = theDefaultTag;
    }

    public LifeMonitor(String tag) {
        if (tag == null) {
            tag = theDefaultTag;
        }
        mTag = theDefaultTag;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i(mTag, "onCreate()");
    }

    @Override
    public void onStart() {
        Log.i(mTag, "onStart()");
    }

    @Override
    public void onRestart() {
        Log.i(mTag, "onRestart()");
    }

    @Override
    public void onPause() {
        Log.i(mTag, "onPause()");
    }

    @Override
    public void onResume() {
        Log.i(mTag, "onResume()");
    }

    @Override
    public void onStop() {
        Log.i(mTag, "onStop()");
    }

    @Override
    public void onDestroy() {
        Log.i(mTag, "onDestroy()");
    }
}
