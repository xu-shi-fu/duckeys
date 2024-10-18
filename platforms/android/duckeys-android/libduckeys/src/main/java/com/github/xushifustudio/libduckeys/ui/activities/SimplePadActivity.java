package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.view.SurfaceView;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.instruments.InstrumentContext;
import com.github.xushifustudio.libduckeys.instruments.pad2.SimplePad2;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiUserAgent;

public class SimplePadActivity extends LifeActivity {

    private MidiUserAgent mMidiUA;
    private InstrumentContext mIC;
    private SurfaceView mSurfaceView;

    // private MidiPadView mPadView;
    //  private DuckClient mDuckClient;
    //  private TaskManager mTaskMan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_simple_pad);
        mSurfaceView = findViewById(R.id.surface_view_pad);

        init();

        setupListener();
    }


    private void init() {

        DuckClient client = new DuckClient(this);
        TaskManager tm = new TaskManager(this);
        MidiUserAgent ua = new MidiUserAgent(this, client, tm);
        LifeManager lm = getLifeManager();

        InstrumentContext ic = SimplePad2.create(this, mSurfaceView, lm);
        MidiEventRT mert = ic.getMert();
        ua.setRx(mert);
        mert.setTx(ua);

        lm.add(tm);
        lm.add(client);
        lm.add(ua);
        lm.add(ic.getLife());

        mMidiUA = ua;
        mIC = ic;
    }

    private void setupListener() {

    }
}
