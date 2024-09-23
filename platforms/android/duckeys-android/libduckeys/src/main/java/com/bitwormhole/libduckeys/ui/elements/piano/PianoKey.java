package com.bitwormhole.libduckeys.ui.elements.piano;

import android.graphics.Color;
import android.view.MotionEvent;

import com.bitwormhole.libduckeys.midi.Note;
import com.bitwormhole.libduckeys.ui.boxes.Container;
import com.bitwormhole.libduckeys.ui.boxes.TouchContext;
import com.bitwormhole.libduckeys.ui.boxes.TouchEventAdapter;

/****
 * PianoKey 表示钢琴键盘上的一个键
 * */
public class PianoKey extends Container {

    public PianoKeyboard ownerKeyboard;
    public PianoKeyGroup ownerGroup;
    public boolean enabled;
    public final Note note;
    public final PianoKeyLEDBar leds;

    public int colorNormal;
    public int colorCurrent;
    public int colorKeyDown;


    public PianoKey(Note n) {
        note = n;
        leds = new PianoKeyLEDBar();
        initLEDs();
    }

    private void initLEDs() {

        int c0 = Color.GREEN;
        int c1 = Color.GRAY;

        PianoKeyLED led0 = new PianoKeyLED();
        led0.text = note.fullname;
        led0.colorCurrent = c0;
        led0.colorNormal = c0;
        led0.colorText = c1;
        leds.add(led0);
    }

    @Override
    public void onTouch(TouchContext ctx, TouchEventAdapter ada) {
        super.onTouch(ctx, ada);
        PianoKeyTouchManager ktm = this.ownerKeyboard.keyTouchManager;
        ktm.handleTouchEvent(this, ada);
        ada.point.setHandled();
    }

    private void onKeyDown() {
        this.colorCurrent = this.colorKeyDown;
        PianoKeyLED led = this.leds.getLEDAt(0, true);
        led.colorCurrent = Color.BLUE;
    }

    private void onKeyUp() {
        this.colorCurrent = this.colorNormal;
        PianoKeyLED led = this.leds.getLEDAt(0, true);
        led.colorCurrent = led.colorNormal;
    }
}
