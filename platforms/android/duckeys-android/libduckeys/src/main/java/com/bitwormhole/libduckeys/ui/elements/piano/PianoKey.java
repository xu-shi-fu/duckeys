package com.bitwormhole.libduckeys.ui.elements.piano;

import android.graphics.Color;

import com.bitwormhole.libduckeys.midi.Note;
import com.bitwormhole.libduckeys.ui.boxes.Container;

/****
 * PianoKey 表示钢琴键盘上的一个键
 * */
public class PianoKey extends Container {

    PianoKeyboard ownerKeyboard;
    PianoKeyGroup ownerGroup;
    boolean enabled;
    public final Note note;

    public int colorNormal;
    public int colorCurrent;
    public int colorKeyDown;


    public PianoKey(Note n) {
        note = n;
    }

}
