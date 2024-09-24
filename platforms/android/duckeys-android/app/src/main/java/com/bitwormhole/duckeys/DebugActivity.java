package com.bitwormhole.duckeys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitwormhole.libduckeys.ui.activities.BluetoothScanningActivity;
import com.bitwormhole.libduckeys.ui.activities.CurrentDeviceActivity;
import com.bitwormhole.libduckeys.ui.activities.MidiConnectionActivity;
import com.bitwormhole.libduckeys.ui.activities.PianoKeyboardActivity;

public class DebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        findViewById(R.id.button_piano_keyboard).setOnClickListener((v) -> {
            handleClickButtonPianoKeyboard();
        });
        findViewById(R.id.button_bluetooth_scan).setOnClickListener((v) -> {
            handleClickButtonBluetoothScan();
        });
        findViewById(R.id.button_midi_connection).setOnClickListener((v) -> {
            handleClickButtonMidiConnection();
        });
        findViewById(R.id.button_show_current_device).setOnClickListener((v) -> {
            Intent i = new Intent(this, CurrentDeviceActivity.class);
            startActivity(i);
        });
    }

    private void handleClickButtonPianoKeyboard() {
        Intent i = new Intent(this, PianoKeyboardActivity.class);
        startActivity(i);
    }

    private void handleClickButtonBluetoothScan() {
        Intent i = new Intent(this, BluetoothScanningActivity.class);
        startActivity(i);
    }

    private void handleClickButtonMidiConnection() {
        Intent i = new Intent(this, MidiConnectionActivity.class);
        startActivity(i);
    }

}
