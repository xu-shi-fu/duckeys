package com.bitwormhole.libduckeys.midi;

import java.io.IOException;

public interface MidiRouterBinder extends MERT {

    String hello(String msg);

    MidiConfiguration configuration();

    void connect(MidiConfiguration cfg) throws IOException;

    void disconnect() throws IOException;

    int state();
}
