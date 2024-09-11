package com.bitwormhole.libduckeys.ui.elements.piano;

import java.util.ArrayList;
import java.util.List;

public class PianoKeyLEDBar {

    private final List<PianoKeyLED> mList;
    private PianoKeyLED[] mListCache;
    private PianoKeyLED mMockLED;

    public PianoKeyLEDBar() {
        mList = new ArrayList<>();
    }

    private interface ListLocker {
        void OnLock(List<PianoKeyLED> list);
    }


    private synchronized void lockList(ListLocker callback) {
        callback.OnLock(mList);
    }

    private PianoKeyLED[] loadCache() {
        List<PianoKeyLED> tmp = new ArrayList<>();
        lockList((list) -> {
            tmp.addAll(list);
        });
        int size = tmp.size();
        return tmp.toArray(new PianoKeyLED[size]);
    }

    private PianoKeyLED[] getCache() {
        PianoKeyLED[] cache = mListCache;
        if (cache == null) {
            cache = loadCache();
            mListCache = cache;
        }
        return cache;
    }

    private PianoKeyLED getMock() {
        PianoKeyLED led = mMockLED;
        if (led == null) {
            led = new PianoKeyLED();
            mMockLED = led;
        }
        return led;
    }

    public PianoKeyLED getLEDAt(int index, boolean no_null) {
        PianoKeyLED[] cache = getCache();
        PianoKeyLED led = null;
        if (0 <= index && index < cache.length) {
            led = cache[index];
        }
        if (led == null && no_null) {
            led = getMock();
        }
        return led;
    }

    public void add(PianoKeyLED led) {
        lockList((list) -> {
            list.add(led);
        });
        mListCache = null;
    }

    public int count() {
        PianoKeyLED[] cache = getCache();
        return cache.length;
    }
}
