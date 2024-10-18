package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.context.LifeActivity;

public class BaseAboutActivity extends LifeActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.layout_about_libduckeys);
    }


    private void init() {
        // LifeManager lm = getLifeManager();
        //   lm.add(ksm);
    }
}
