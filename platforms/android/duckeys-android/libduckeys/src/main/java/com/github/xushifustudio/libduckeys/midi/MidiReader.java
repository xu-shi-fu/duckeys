package com.github.xushifustudio.libduckeys.midi;

import androidx.annotation.NonNull;

import com.github.xushifustudio.libduckeys.helper.Hex;

import java.io.IOException;

public interface MidiReader {

    MidiEvent read(int timeout) throws IOException;

}
