package com.github.xushifustudio.libduckeys.ui.box2;

public class B2OnTouchPointer {

    public final int id;
    public final float globalStartedAtX;
    public final float globalStartedAtY;


    public boolean started;
    public boolean stopped;
    public long startedAt; // timestamp
    public long stoppedAt; // timestamp
    public long updatedAt; // timestamp
    public float globalX;
    public float globalY;

    private Binding mCurrentBinding;

    public B2OnTouchPointer(int myId, float x, float y) {
        this.id = myId;
        this.globalStartedAtX = x;
        this.globalStartedAtY = y;
        this.globalX = x;
        this.globalY = y;
    }


    public static class Binding {
        private final B2OnTouchPointer pointer;

        private Binding(B2OnTouchPointer ptr) {
            this.pointer = ptr;
        }

        public boolean isAlive() {
            if (pointer.stopped) {
                return false;
            }
            return equal(this, pointer.mCurrentBinding);
        }
    }

    private static boolean equal(Binding a, Binding b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    private Binding innerBind(Binding older) {
        if (equal(older, mCurrentBinding)) {
            return older;
        }
        Binding b = new Binding(this);
        mCurrentBinding = b;
        return b;
    }

    public Binding bind(Binding older) {
        return this.innerBind(older);
    }

    public Binding bind() {
        return this.innerBind(null);
    }
}
