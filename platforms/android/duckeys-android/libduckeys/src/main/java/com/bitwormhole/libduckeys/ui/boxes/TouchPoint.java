package com.bitwormhole.libduckeys.ui.boxes;

public class TouchPoint {

    // attr
    public long id;
    public int index;

    // state
    public boolean done;
    public boolean cancelled;
    public boolean handled;

    public interface Handler {
        void handlePoint(TouchPoint tp);
    }

    public void setHandled() {
        handled = true;
        done = true;
    }

    public void setCancelled() {
        cancelled = true;
        done = true;
    }
}
