package com.github.xushifustudio.libduckeys.conn.androidmidi;

import android.media.midi.MidiReceiver;

import com.github.xushifustudio.libduckeys.midi.MidiEvent;

import java.io.IOException;

public class AndroidMidiRx extends MidiReceiver {

    private final AndroidMidiContext mContext;

    private AndroidMidiRx(AndroidMidiContext ctx) {
        mContext = ctx;
    }

    public static AndroidMidiRx newInstance(AndroidMidiContext ctx) {

        if (ctx == null) {
            return null;
        }
        if (ctx.output == null) {
            return null;
        }
        if (ctx.handler == null) {
            return null;
        }

        AndroidMidiRx rx = new AndroidMidiRx(ctx);
        ctx.output.connect(rx);
        return rx;
    }


    @Override
    public void onSend(byte[] b, int off, int count, long time) throws IOException {

        MidiEvent evt = new MidiEvent();
        evt.data = b;
        evt.offset = off;
        evt.count = count;
        evt.timestamp = time;
        evt.flush = false;

        mContext.handler.handle(evt);
    }
}
