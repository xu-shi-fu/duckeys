package com.bitwormhole.libduckeys.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.context.LifeActivity;

public class SimplePadActivity extends LifeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simple_pad);
    }
}
