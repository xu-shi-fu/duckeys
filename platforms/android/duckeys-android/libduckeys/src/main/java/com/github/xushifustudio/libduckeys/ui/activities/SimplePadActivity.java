package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.midi.KeyStateManager;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.ui.views.MidiPadView;

public class SimplePadActivity extends LifeActivity {

    private KeyStateManager mKeyStateManager;
    private MidiPadView mPadView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simple_pad);

        mPadView = findViewById(R.id.midi_pad_view);

        setupListener();
    }


    private void init() {

        KeyStateManager ksm = new KeyStateManager();
        DuckClient client = new DuckClient(this);


        LifeManager lm = getLifeManager();
        lm.add(ksm);
        lm.add(client);

        mKeyStateManager = ksm;
    }

    private void setupListener() {
        mPadView.setOnMidiNoteListener((e) -> {
            mKeyStateManager.write(e);
        });
    }
}
