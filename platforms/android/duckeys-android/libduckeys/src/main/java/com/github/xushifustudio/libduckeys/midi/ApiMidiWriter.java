package com.github.xushifustudio.libduckeys.midi;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.services.MidiWriterService;

import java.io.Closeable;
import java.io.IOException;

public class ApiMidiWriter implements MidiWriter, Closeable {

    private final API api;

    public ApiMidiWriter(API api) {
        this.api = api;
    }

    @Override
    public void write(MidiEvent me) throws IOException {
        MidiWriterService.Request req = new MidiWriterService.Request();
        req.event = me;
        try {
            Want want = MidiWriterService.encode(req);
            want.method = Want.POST;
            want.url = MidiWriterService.URI;

            Have have = api.invoke(want);
            MidiWriterService.Response resp = MidiWriterService.decode(have);

        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {

    }
}
