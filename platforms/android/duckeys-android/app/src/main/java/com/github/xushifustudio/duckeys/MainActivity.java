package com.github.xushifustudio.duckeys;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;

import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeMonitor;

public class MainActivity extends LifeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        getLifeManager().add(new LifeMonitor());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);
    }
}
