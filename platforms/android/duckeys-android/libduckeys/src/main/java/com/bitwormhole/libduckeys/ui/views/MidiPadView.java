package com.bitwormhole.libduckeys.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.helper.DuckLogger;

public class MidiPadView extends LinearLayout {


    public MidiPadView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_midi_pad, this);
        Button[] buttons = new Button[16];

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

        setupButtonListeners(buttons);
    }

    private void setupButtonListeners(Button[] buttons) {
        for (Button b : buttons) {
            b.setOnClickListener((v) -> {
                String msg = "click button: " + b.getText();
                Log.w(DuckLogger.TAG, msg);
            });
        }
    }
}
