package com.bitwormhole.duckeys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitwormhole.libduckeys.ui.activities.PianoKeyboardActivity;

public class DebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findViewById(R.id.button_piano_keyboard).setOnClickListener((v) -> {
            handleClickButtonPianoKeyboard();
        });
    }

    private void handleClickButtonPianoKeyboard() {
        Intent i = new Intent(this, PianoKeyboardActivity.class);
        startActivity(i);
    }

}
