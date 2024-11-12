package com.github.xushifustudio.duckeys;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeMonitor;
import com.github.xushifustudio.libduckeys.ui.activities.ExampleActivity;
import com.github.xushifustudio.libduckeys.ui.activities.StartingActivity;

public class StartupActivity extends StartingActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_startup);
    }

    @Override
    protected void goHome() {
        // super.goHome();
        Intent i = new Intent(this, DebugActivity.class);
        startActivity(i);
    }
}
