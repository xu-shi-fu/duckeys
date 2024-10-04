package com.github.xushifustudio.libduckeys.midi;

import androidx.annotation.NonNull;

import com.github.xushifustudio.libduckeys.helper.Hex;

public class MidiEvent {

    public byte[] data;
    public int offset;
    public int count;
    public long timestamp;


    @NonNull
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[MidiEvent");
        b.append(" time:").append(this.timestamp);
        b.append(" data:");
        int end = offset + count;
        for (int i = offset; i < end; i++) {
            String hex = Hex.stringify(data[i]);
            b.append(' ').append(hex);
        }
        b.append("]");
        return b.toString();
    }
}
