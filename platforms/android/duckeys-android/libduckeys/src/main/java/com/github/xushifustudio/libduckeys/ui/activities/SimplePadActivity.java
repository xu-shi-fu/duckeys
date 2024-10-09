package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.midi.KeyStateManager;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiNoteEvent;
import com.github.xushifustudio.libduckeys.midi.MidiUserAgent;
import com.github.xushifustudio.libduckeys.ui.views.MidiPadView;

public class SimplePadActivity extends LifeActivity {

    private MidiUserAgent mMidiUA;
    private MidiPadView mPadView;
    //  private DuckClient mDuckClient;
    //  private TaskManager mTaskMan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simple_pad);

        mPadView = findViewById(R.id.midi_pad_view);

        setupListener();
    }


    private void init() {

        DuckClient client = new DuckClient(this);
        TaskManager tm = new TaskManager(this);
        MidiUserAgent ua = new MidiUserAgent(this, client, tm);

        LifeManager lm = getLifeManager();
        lm.add(tm);
        lm.add(client);
        lm.add(ua);

        //    mDuckClient = client;
        //    mTaskMan = tm;
        mMidiUA = ua;
    }

    private void setupListener() {
        mPadView.setOnMidiNoteListener((e1) -> {
            MidiEvent e2 = MidiNoteEvent.toMidiEvent(e1);
            mMidiUA.getTx().dispatch(e2);
        });
    }
}
