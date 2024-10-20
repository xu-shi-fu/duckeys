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

    public B2OnTouchPointer(int myId, float x, float y) {
        this.id = myId;
        this.globalStartedAtX = x;
        this.globalStartedAtY = y;
        this.globalX = x;
        this.globalY = y;
    }

}
