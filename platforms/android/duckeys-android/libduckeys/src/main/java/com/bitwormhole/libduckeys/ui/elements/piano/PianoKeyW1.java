package com.bitwormhole.libduckeys.ui.elements.piano;

import com.bitwormhole.libduckeys.ui.boxes.Container;
import com.bitwormhole.libduckeys.ui.boxes.RenderingContext;
import com.bitwormhole.libduckeys.ui.boxes.RenderingItem;

/***
 * PianoKeyW1 表示白键的上半部分
 */
public class PianoKeyW1 extends Container {

    private final PianoKeyW mParentKey;

    public PianoKeyW1(PianoKeyW parent) {
        mParentKey = parent;
    }

    @Override
    public void render(RenderingContext rc, RenderingItem item) {
        super.render(rc, item);
        // draw led(s)
        PianoKeyLEDRenderer.renderLEDBar(rc, item, mParentKey.leds);
    }
}
