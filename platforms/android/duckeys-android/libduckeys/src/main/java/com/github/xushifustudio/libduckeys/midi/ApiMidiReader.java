package com.github.xushifustudio.libduckeys.midi;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.services.MidiReaderService;

import java.io.Closeable;
import java.io.IOException;

public class ApiMidiReader implements MidiReader, Closeable {

    private final API api;

    public ApiMidiReader(API api) {
        this.api = api;
    }

    @Override
    public MidiEvent read(int timeout) throws IOException {
        MidiReaderService.Request req = new MidiReaderService.Request();
        req.timeout = timeout;
        try {
            Want want = MidiReaderService.encode(req);
            want.method = Want.GET;
            want.url = MidiReaderService.URI;
            Have have = api.invoke(want);
            MidiReaderService.Response resp = MidiReaderService.decode(have);
            return resp.event;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
    }
}
