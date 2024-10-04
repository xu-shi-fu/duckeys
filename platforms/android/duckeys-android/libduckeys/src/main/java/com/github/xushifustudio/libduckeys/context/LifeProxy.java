package com.github.xushifustudio.libduckeys.context;


import android.os.Bundle;

import androidx.annotation.Nullable;

public class LifeProxy implements Life {

    private Runnable _on_create;
    private Runnable _on_start;
    private Runnable _on_restart;
    private Runnable _on_stop;
    private Runnable _on_destroy;
    private Runnable _on_pause;
    private Runnable _on_resume;

    public LifeProxy() {
    }

    public void setOnCreate(Runnable r) {
        _on_create = r;
    }

    public void setOnStart(Runnable r) {
        _on_start = r;
    }

    public void setOnRestart(Runnable r) {
        _on_restart = r;
    }

    public void setOnPause(Runnable r) {
        _on_pause = r;
    }

    public void setOnResume(Runnable r) {
        _on_resume = r;
    }

    public void setOnStop(Runnable r) {
        _on_stop = r;
    }

    public void setOnDestroy(Runnable r) {
        _on_destroy = r;
    }


    private void invoke(Runnable r) {
        if (r == null) {
            return;
        }
        r.run();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        invoke(_on_create);
    }

    @Override
    public void onStart() {
        invoke(_on_start);
    }

    @Override
    public void onRestart() {
        invoke(_on_restart);
    }

    @Override
    public void onPause() {
        invoke(_on_pause);
    }

    @Override
    public void onResume() {
        invoke(_on_resume);
    }

    @Override
    public void onStop() {
        invoke(_on_stop);
    }

    @Override
    public void onDestroy() {
        invoke(_on_destroy);
    }
}
