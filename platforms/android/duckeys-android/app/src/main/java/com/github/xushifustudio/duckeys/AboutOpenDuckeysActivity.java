package com.github.xushifustudio.duckeys;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeMonitor;
import com.github.xushifustudio.libduckeys.ui.activities.BaseAboutActivity;

public class AboutOpenDuckeysActivity extends BaseAboutActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_open_duckeys);
    }
}
