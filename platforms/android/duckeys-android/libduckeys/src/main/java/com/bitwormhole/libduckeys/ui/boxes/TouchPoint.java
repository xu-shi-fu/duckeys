package com.bitwormhole.libduckeys.ui.boxes;

public class TouchPoint {

    public long id;
    public int index;
    public boolean done;
    public boolean cancelld;
    public boolean handled;

    public interface Handler {
        void handlePoint(TouchPoint tp);
    }
}
