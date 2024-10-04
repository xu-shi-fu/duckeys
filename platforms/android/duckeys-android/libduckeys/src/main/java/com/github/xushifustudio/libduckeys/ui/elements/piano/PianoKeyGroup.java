package com.github.xushifustudio.libduckeys.ui.elements.piano;

import com.github.xushifustudio.libduckeys.midi.Note;
import com.github.xushifustudio.libduckeys.ui.boxes.Container;
import com.github.xushifustudio.libduckeys.ui.boxes.Layout;
import com.github.xushifustudio.libduckeys.ui.boxes.LayoutContext;

import java.util.List;

/***
 * PianoKeyGroup 表示钢琴键盘上的一组（12个）琴键
 */
public class PianoKeyGroup extends Container {

    public final int groupId;
    public final PianoKeyboard ownerKeyboard;


    // the 12 keys
    public final PianoKey[] keys12;
    private final PianoKeyW[] keysW7; // 白键 x 7
    private final PianoKeyB[] keysB5; // 黑键 x 5

    // white keys
    public final PianoKeyW keyC;
    public final PianoKeyW keyD;
    public final PianoKeyW keyE;
    public final PianoKeyW keyF;
    public final PianoKeyW keyG;
    public final PianoKeyW keyA;
    public final PianoKeyW keyB;

    // black keys
    public final PianoKeyB keyCS;// the "C#"
    public final PianoKeyB keyDS;// the "D#"
    public final PianoKeyB keyFS;// the "F#"
    public final PianoKeyB keyGS;// the "G#"
    public final PianoKeyB keyAS; // the "A#"


    public PianoKeyGroup(PianoKeyboard parent, int group) {

        groupId = group;
        ownerKeyboard = parent;

        final boolean normal = false;
        final boolean sharp = true;

        keyC = new PianoKeyW(Note.forNote('C', normal, group));
        keyD = new PianoKeyW(Note.forNote('D', normal, group));
        keyE = new PianoKeyW(Note.forNote('E', normal, group));
        keyF = new PianoKeyW(Note.forNote('F', normal, group));
        keyG = new PianoKeyW(Note.forNote('G', normal, group));
        keyA = new PianoKeyW(Note.forNote('A', normal, group));
        keyB = new PianoKeyW(Note.forNote('B', normal, group));

        keyCS = new PianoKeyB(Note.forNote('C', sharp, group));
        keyDS = new PianoKeyB(Note.forNote('D', sharp, group));
        keyFS = new PianoKeyB(Note.forNote('F', sharp, group));
        keyGS = new PianoKeyB(Note.forNote('G', sharp, group));
        keyAS = new PianoKeyB(Note.forNote('A', sharp, group));

        keys12 = new PianoKey[]{
                keyC, keyCS, keyD, keyDS, keyE,
                keyF, keyFS, keyG, keyGS, keyA, keyAS, keyB};

        keysW7 = new PianoKeyW[]{keyC, keyD, keyE, keyF, keyG, keyA, keyB};
        keysB5 = new PianoKeyB[]{keyCS, keyDS, keyFS, keyGS, keyAS};

        setLayout(new MyLayout());
        initChildren();
    }

    private final class MyLayout implements Layout {

        private void rebuildLayoutWhiteKeys(LayoutContext lc, PianoKeyGroup parent) {

            final int groupH = parent.height;
            final float groupW = parent.width;
            final float keyW = groupW / 7; // 每个白键的宽度
            final int blackKeyH = getBlackKeyHeight();
            final int whiteLowerH = groupH - blackKeyH; // 白键下半高度

            float x = 0;
            PianoKeyW[] children = parent.keysW7;
            for (PianoKeyW child : children) {
                // KeyW
                child.x = (int) x;
                child.y = 0;
                child.width = (int) keyW;
                child.height = groupH;
                // KeyW2
                PianoKeyW2 lower = child.lower;
                lower.x = 0;
                lower.y = blackKeyH;
                lower.width = (int) keyW;
                lower.height = whiteLowerH;
                // next
                x += keyW;
            }
        }

        // rebuildLayoutBlackKeys 重新布局黑键 （以及白键的上半部分）
        private void rebuildLayoutBlackKeys(LayoutContext lc, PianoKeyGroup parent) {

            final int groupH = parent.height;
            final float groupW = parent.width;
            final float keyW = groupW / 12; // 每个黑键的宽度
            final int keyH = getBlackKeyHeight();

            float x = 0;
            PianoKey[] children = parent.keys12;
            for (PianoKey child : children) {
                if (child instanceof PianoKeyW) {
                    PianoKeyW wk = (PianoKeyW) child;
                    PianoKeyW1 upper = wk.upper;
                    upper.x = (int) x;
                    upper.y = 0;
                    upper.width = (int) keyW;
                    upper.height = keyH;
                } else if (child instanceof PianoKeyB) {
                    PianoKeyB bk = (PianoKeyB) child;
                    bk.x = (int) x;
                    bk.y = 0;
                    bk.width = (int) keyW;
                    bk.height = keyH;
                }
                x += keyW;
            }
        }

        @Override
        public void apply(LayoutContext lc, Container can) {
            if (can instanceof PianoKeyGroup) {
                PianoKeyGroup pkg = (PianoKeyGroup) can;
                this.rebuildLayoutBlackKeys(lc, pkg);
                this.rebuildLayoutWhiteKeys(lc, pkg);
            }
        }
    }

    private void initChildren() {
        for (PianoKeyW white : keysW7) {
            addChild(white);
            addChild(white.upper);
            white.z = 1;
            white.upper.z = 2;
        }
        for (PianoKeyB black : keysB5) {
            addChild(black);
            black.z = 2;
        }
        for (PianoKey pk : keys12) {
            pk.ownerKeyboard = this.ownerKeyboard;
        }
    }

    private int getBlackKeyHeight() {
        final int groupH = this.height;
        float percent = this.ownerKeyboard.blackKeyHeightPercent;
        return (int) (groupH * percent);
    }
}
