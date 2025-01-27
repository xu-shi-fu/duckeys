package com.github.xushifustudio.duckeys.demo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.github.xushifustudio.libduckeys.ui.activities.BluetoothScanningActivity;
import com.github.xushifustudio.libduckeys.ui.activities.CurrentDeviceActivity;
import com.github.xushifustudio.libduckeys.ui.activities.MidiCCConsoleActivity;
import com.github.xushifustudio.libduckeys.ui.activities.MidiConnectionActivity;
import com.github.xushifustudio.libduckeys.ui.activities.MidiDeviceActivity;
import com.github.xushifustudio.libduckeys.ui.activities.MidiRxMonitorActivity;
import com.github.xushifustudio.libduckeys.ui.activities.PianoKeyboardActivity;
import com.github.xushifustudio.libduckeys.ui.activities.SimplePadActivity;

public class DemoDebugActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_debug);

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
        findViewById(R.id.button_show_simple_midi_pad).setOnClickListener((v) -> {
            Intent i = new Intent(this, SimplePadActivity.class);
            startActivity(i);
        });
        findViewById(R.id.button_show_midi_cc_console).setOnClickListener((v) -> {
            Intent i = new Intent(this, MidiCCConsoleActivity.class);
            startActivity(i);
        });
        findViewById(R.id.button_show_midi_devices).setOnClickListener((v) -> {
            Intent i = new Intent(this, MidiDeviceActivity.class);
            startActivity(i);
        });
        findViewById(R.id.button_show_midi_rx_monitor).setOnClickListener((v) -> {
            Intent i = new Intent(this, MidiRxMonitorActivity.class);
            startActivity(i);
        });
        findViewById(R.id.button_show_about).setOnClickListener((v) -> {
            Intent i = new Intent(this, DemoAboutActivity.class);
            startActivity(i);
        });
        findViewById(R.id.button_debug_multi_touch).setOnClickListener((v) -> {
            Intent i = new Intent(this, DemoDebugMultiTouchActivity.class);
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
