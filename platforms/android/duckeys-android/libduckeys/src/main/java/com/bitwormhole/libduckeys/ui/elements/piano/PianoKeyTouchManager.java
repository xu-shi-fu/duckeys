package com.bitwormhole.libduckeys.ui.elements.piano;

import com.bitwormhole.libduckeys.helper.DuckLogger;
import com.bitwormhole.libduckeys.ui.boxes.TouchContext;
import com.bitwormhole.libduckeys.ui.boxes.TouchEventAdapter;

import android.util.Log;
import android.view.MotionEvent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PianoKeyTouchManager {

    private final Map<Long, Tracking> trackings;

    public PianoKeyTouchManager() {
        trackings = new HashMap<>();
    }

    private class Tracking {

        long id;
        PianoKey current;

        public void handleEvent(PianoKey newerKey, TouchEventAdapter ada) {
            PianoKey olderKey = current;
            if (olderKey != null && newerKey != null) {
                handleKeyMove(olderKey, newerKey, ada);
            } else if (olderKey == null && newerKey != null) {
                handleKeyStart(newerKey, ada);
            } else if (olderKey != null && newerKey == null) {
                handleKeyStop(olderKey, ada);
            } else {
                // NOP
            }
            current = newerKey;
        }

        public void handleKeyStart(PianoKey key, TouchEventAdapter ada) {
            key.colorCurrent = key.colorKeyDown;
        }

        public void handleKeyMove(PianoKey older, PianoKey newer, TouchEventAdapter ada) {
            if (older.note.index == newer.note.index) {
                // 无变化
                return;
            }
            older.colorCurrent = older.colorNormal;
            newer.colorCurrent = newer.colorKeyDown;
        }

        public void handleKeyStop(PianoKey key, TouchEventAdapter ada) {
            key.colorCurrent = key.colorNormal;
        }
    }


    public Tracking findTracking(TouchEventAdapter ada) {
        final TouchContext ctx = ada.context;
        final Long id = ada.point.id;
        Tracking t1 = trackings.get(id);
        if (t1 == null) {
            int action = ctx.event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    t1 = new Tracking();
                    t1.id = id;
                    trackings.put(id, t1);
                    break;
                default:
                    return null;
            }
        }
        return t1;
    }

    private void logKeys() {
        Set<Long> keys1 = this.trackings.keySet();
        List<Long> keys2 = new ArrayList<>(keys1);
        keys2.sort((a, b) -> {
            return (int) (a - b);
        });
        StringBuilder msg = new StringBuilder();
        msg.append("PianoKeyTouchManager.trackings.keys: ");
        for (Long n : keys2) {
            msg.append(n).append(',');
        }
        Log.d(DuckLogger.TAG, msg.toString());
    }

    public void handleTouchEvent(PianoKey key, TouchEventAdapter ada) {
        Tracking tr = findTracking(ada);
        if (tr == null) {
            return;
        }
        tr.handleEvent(key, ada);
        this.logKeys();
    }
}
