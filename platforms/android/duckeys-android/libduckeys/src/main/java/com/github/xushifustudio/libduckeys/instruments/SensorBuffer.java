package com.github.xushifustudio.libduckeys.instruments;

public class SensorBuffer {

    private final Sample[] buffer;
    private final Sample na;
    public long position;


    public SensorBuffer(int size) {
        this.buffer = new Sample[size];
        this.na = new Sample();
        for (int i = 0; i < size; i++) {
            buffer[i] = new Sample();
        }
    }

    public void push(Sample sample) {
        if (sample == null) {
            return;
        }
        long i = this.position++;
        Sample dst = getSample(i);
        dst.time = sample.time;
        dst.x = sample.x;
        dst.y = sample.y;
        dst.z = sample.z;
    }

    public static class Sample {
        public long time;
        public float x;
        public float y;
        public float z;
    }

    public int size() {
        return this.buffer.length;
    }

    public Sample getSample(long index) {
        if (0 <= index && index < buffer.length) {
            return buffer[(int) index];
        } else if (index > 0) {
            return buffer[(int) (index % buffer.length)];
        }
        return na;
    }
}
