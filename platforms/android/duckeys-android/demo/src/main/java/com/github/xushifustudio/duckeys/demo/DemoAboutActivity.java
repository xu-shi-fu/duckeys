package com.github.xushifustudio.duckeys.demo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.ui.activities.BaseAboutActivity;

public class DemoAboutActivity extends BaseAboutActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_open_duckeys);
    }
}
