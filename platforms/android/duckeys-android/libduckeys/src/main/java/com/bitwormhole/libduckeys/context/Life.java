package com.bitwormhole.libduckeys.context;


import android.os.Bundle;

import androidx.annotation.Nullable;

public interface Life {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onPause();

    void onResume();

    void onStop();

    void onDestroy();

}
