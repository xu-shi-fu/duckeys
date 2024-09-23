package com.bitwormhole.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.context.LifeActivity;
import com.bitwormhole.libduckeys.context.LifeManager;
import com.bitwormhole.libduckeys.helper.DuckLogger;
import com.bitwormhole.libduckeys.midi.MidiEvent;
import com.bitwormhole.libduckeys.midi.MidiEventDispatcher;
import com.bitwormhole.libduckeys.midi.MidiRouterClient;
import com.bitwormhole.libduckeys.midi.MidiEventHandler;

public class MidiConnectionActivity extends LifeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        MidiRouterClient client = new MidiRouterClient(this);

        LifeManager lm = getLifeManager();
        lm.add(client);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_midi_connection);


        findViewById(R.id.button_send_demo_midi_event).setOnClickListener((v) -> {
            MidiEvent me = new MidiEvent();
            me.timestamp = 0;
            me.data = new byte[]{0xa, 0xb, 0xc};
            me.offset = 0;
            me.count = me.data.length;
            MidiEventDispatcher tx = client.getTx();
            tx.dispatch(me);
        });

        client.setRx((me) -> {

            // todo ...
            Log.i(DuckLogger.TAG, "" + me);
        });
    }
}
