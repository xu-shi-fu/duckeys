package com.github.xushifustudio.duckeys.demo;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.ui.activities.StartingActivity;

public class DemoStartupActivity extends StartingActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_startup);
    }

    @Override
    protected void goHome() {
        // super.goHome();
        Intent i = new Intent(this, DemoDebugActivity.class);
        startActivity(i);
    }
}
