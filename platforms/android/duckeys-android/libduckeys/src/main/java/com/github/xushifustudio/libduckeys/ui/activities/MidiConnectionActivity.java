package com.github.xushifustudio.libduckeys.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.context.LifeActivity;
import com.github.xushifustudio.libduckeys.context.LifeManager;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventDispatcher;
import com.github.xushifustudio.libduckeys.context.DuckClient;

public class MidiConnectionActivity extends LifeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        DuckClient client = new DuckClient(this);

        LifeManager lm = getLifeManager();
        lm.add(client);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_midi_connection);


        /*
        findViewById(R.id.button_send_demo_midi_event).setOnClickListener((v) -> {
            MidiEvent me = new MidiEvent();
            me.timestamp = 0;
            me.data = new byte[]{0xa, 0xb, 0xc};
            me.offset = 0;
            me.count = me.data.length;
            MidiEventDispatcher tx = client.getTx();
            tx.dispatch(me);
        });
*/


    }
}
