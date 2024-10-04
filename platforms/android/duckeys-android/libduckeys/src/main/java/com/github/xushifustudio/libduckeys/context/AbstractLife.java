package com.github.xushifustudio.libduckeys.context;


import android.os.Bundle;

import androidx.annotation.Nullable;

public class AbstractLife implements Life {


    public interface OnLifeListener {
        void OnLife(Life ml);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
