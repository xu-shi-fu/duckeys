package com.github.xushifustudio.libduckeys.midi;

import java.io.IOException;

public interface MidiWriter {

    void write(MidiEvent me) throws IOException;

}
