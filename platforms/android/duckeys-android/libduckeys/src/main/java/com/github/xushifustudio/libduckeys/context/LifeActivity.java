package com.github.xushifustudio.libduckeys.context;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LifeActivity extends Activity {

    private final LifeManager mLifeManager;

    public LifeActivity() {
        mLifeManager = new LifeManager();
    }

    public final LifeManager getLifeManager() {
        return mLifeManager;
    }


    private void forMainLife(AbstractLife.OnLifeListener listener) {
        Life ml = mLifeManager.getMain();
        listener.OnLife(ml);
    }


    // handlers

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forMainLife((ml) -> {
            ml.onCreate(savedInstanceState);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        forMainLife(Life::onStart);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        forMainLife(Life::onRestart);
    }

    @Override
    protected void onPause() {
        super.onPause();
        forMainLife(Life::onPause);
    }

    @Override
    protected void onResume() {
        super.onResume();
        forMainLife(Life::onResume);
    }

    @Override
    protected void onStop() {
        super.onStop();
        forMainLife(Life::onStop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        forMainLife(Life::onDestroy);
    }
}
