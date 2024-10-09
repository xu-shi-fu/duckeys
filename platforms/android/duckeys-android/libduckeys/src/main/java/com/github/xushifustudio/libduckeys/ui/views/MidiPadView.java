package com.github.xushifustudio.libduckeys.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.xushifustudio.libduckeys.R;
import com.github.xushifustudio.libduckeys.midi.MidiNoteEvent;
import com.github.xushifustudio.libduckeys.midi.Note;

public class MidiPadView extends LinearLayout {

    private final KeyConfig[] mKeys;
    private MidiNoteEvent.Listener mListener;

    public MidiPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_midi_pad, this);

        final int count = 16;
        Button[] buttons = new Button[count];
        KeyConfig[] keys = new KeyConfig[count];

        buttons[0] = findViewById(R.id.button_pad_b0);
        buttons[1] = findViewById(R.id.button_pad_b1);
        buttons[2] = findViewById(R.id.button_pad_b2);
        buttons[3] = findViewById(R.id.button_pad_b3);
        buttons[4] = findViewById(R.id.button_pad_b4);
        buttons[5] = findViewById(R.id.button_pad_b5);
        buttons[6] = findViewById(R.id.button_pad_b6);
        buttons[7] = findViewById(R.id.button_pad_b7);
        buttons[8] = findViewById(R.id.button_pad_b8);
        buttons[9] = findViewById(R.id.button_pad_b9);
        buttons[10] = findViewById(R.id.button_pad_b10);
        buttons[11] = findViewById(R.id.button_pad_b11);
        buttons[12] = findViewById(R.id.button_pad_b12);
        buttons[13] = findViewById(R.id.button_pad_b13);
        buttons[14] = findViewById(R.id.button_pad_b14);
        buttons[15] = findViewById(R.id.button_pad_b15);

        for (int i = 0; i < count; i++) {
            keys[i] = initKeyConfigWithIndex(i);
            setupButtonListener(buttons[i], keys[i]);
        }

        mKeys = keys;
    }


    private final static class KeyConfig {
        public Note note;
    }


    private KeyConfig initKeyConfigWithIndex(int index) {
        int i = (index & 0x0f) + 60;
        KeyConfig k = new KeyConfig();
        k.note = Note.forNote(i);
        return k;
    }


    private void setupButtonListener(View v, KeyConfig kc) {
        v.setOnTouchListener((view, event) -> {
            handleOnTouchEvent(view, event, kc);
            return true;
        });
    }

    private void handleOnTouchEvent(View view, MotionEvent me, KeyConfig kc) {
        int action = me.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                view.setBackgroundColor(Color.RED);
                onTouchDown(kc);
                break;
            case MotionEvent.ACTION_UP:
                view.setBackgroundColor(0);
                onTouchUp(kc);
                break;
            default:
                break;
        }
    }


    private void handleMidiNoteEvent(MidiNoteEvent event) {
        MidiNoteEvent.Listener l = mListener;
        if (l == null || event == null) {
            return;
        }
        l.onMidiNoteEvent(event);
    }


    private void onTouchDown(KeyConfig kc) {
        MidiNoteEvent event = makeMidiNoteEvent(kc);
        if (event == null) {
            return;
        }
        event.on = true;
        event.velocity = 99; // todo ...
        handleMidiNoteEvent(event);
    }

    private void onTouchUp(KeyConfig kc) {
        MidiNoteEvent event = makeMidiNoteEvent(kc);
        if (event == null) {
            return;
        }
        event.on = false;
        event.velocity = 0;
        handleMidiNoteEvent(event);
    }

    private MidiNoteEvent makeMidiNoteEvent(KeyConfig kc) {
        if (kc == null) {
            return null;
        }
        Note note = kc.note;
        if (note == null) {
            return null;
        }
        MidiNoteEvent e = new MidiNoteEvent();
        e.note = (byte) note.midi;
        e.channel = 0; // todo ...
        return e;
    }

    public void setOnMidiNoteListener(MidiNoteEvent.Listener l) {
        mListener = l;
    }
}
