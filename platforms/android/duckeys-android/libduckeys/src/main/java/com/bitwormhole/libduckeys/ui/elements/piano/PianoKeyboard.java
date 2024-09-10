package com.bitwormhole.libduckeys.ui.elements.piano;

import com.bitwormhole.libduckeys.ui.boxes.Container;
import com.bitwormhole.libduckeys.ui.boxes.Layout;
import com.bitwormhole.libduckeys.ui.boxes.LayoutContext;
import com.bitwormhole.libduckeys.ui.boxes.RenderingContext;
import com.bitwormhole.libduckeys.ui.boxes.RenderingItem;
import com.bitwormhole.libduckeys.ui.boxes.Viewport;
import com.bitwormhole.libduckeys.ui.layouts.NOPLayout;

/***
 * PianoKeyboard 表示一个钢琴键盘
 */
public class PianoKeyboard extends Container {

    public final PianoKeyGroup[] groups;
    public final PianoKey[] allKeys; // 全部 128 个按键

    public float blackKeyHeightPercent; // 黑键高度的百分比 (1.0=100%)

    private final Container mInnerClient; // 各个 group 实际上放在这个容器里面

    public int countKeysInView; // 视图内可见的按键个数
    public int firstKeysInView; // 视图内的第一个按键的 index (0~127)


    public PianoKeyboard() {
        blackKeyHeightPercent = 0.6f;
        groups = this.makeGroups();
        allKeys = this.make128KeysArray(groups);
        mInnerClient = new Container();
        firstKeysInView = 0;
        countKeysInView = 61;

        for (PianoKeyGroup g : groups) {
            mInnerClient.addChild(g);
        }
        mInnerClient.setLayout(NOPLayout.getInstance());
        this.addChild(mInnerClient);
        this.setLayout(new MyLayout());
    }

    private class MyLayout implements Layout {
        @Override
        public void apply(LayoutContext lc, Container can) {

            final PianoKeyboard keyboard = PianoKeyboard.this;
            float widthKeyboard = keyboard.width;
            float widthPerKey = widthKeyboard / countKeysInView;

            // groups
            for (int i = 0; i < groups.length; i++) {
                PianoKeyGroup group = groups[i];
                group.x = (int) (widthPerKey * i * 12);
                group.y = 0;
                group.width = (int) (widthPerKey * 12);
                group.height = keyboard.height;
            }

            // client
            mInnerClient.width = (int) (128 * widthPerKey);
            mInnerClient.height = keyboard.height;
            mInnerClient.y = 0;
            mInnerClient.x = 0 - (int) (widthPerKey * firstKeysInView);
        }
    }

    @Override
    public void render(RenderingContext rc, RenderingItem item) {

        // just for try
        for (PianoKey key : allKeys) {
            int i = key.note.index;
            if (i % 5 == 0) {
                key.colorCurrent = key.colorKeyDown;
            }
        }

        super.render(rc, item);
    }

    private PianoKey[] make128KeysArray(PianoKeyGroup[] groups) {
        PianoKey lastKey = null;
        PianoKey[] dest = new PianoKey[128];
        for (PianoKeyGroup group : groups) {
            PianoKey[] list12 = group.keys12;
            for (PianoKey key : list12) {
                int index = key.note.index;
                if ((0 <= index) && (index < dest.length)) {
                    dest[index] = key;
                    lastKey = key;
                }
            }
        }
        // 检查是否有空值
        for (int i = 0; i < dest.length; i++) {
            if (dest[i] == null) {
                dest[i] = lastKey;
            }
        }
        return dest;
    }

    private PianoKeyGroup[] makeGroups() {
        PianoKeyGroup[] array = new PianoKeyGroup[11];
        for (int i = 0; i < array.length; i++) {
            int gid = i - 2;
            PianoKeyGroup group = new PianoKeyGroup(this, gid);
            array[i] = group;
        }
        return array;
    }
}
