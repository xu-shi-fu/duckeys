package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.midi.KeyStateManager;
import com.github.xushifustudio.libduckeys.ui.views.MidiPadView;

public class ExampleActivity extends LifeActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_example);
    }


    private void init() {
        LifeManager lm = getLifeManager();
        //   lm.add(ksm);
    }
}
