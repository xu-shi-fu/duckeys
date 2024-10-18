package com.github.xushifustudio.libduckeys.api.controllers;

import android.util.Log;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.MidiReaderService;
import com.github.xushifustudio.libduckeys.context.ComponentContext;
import com.github.xushifustudio.libduckeys.context.ComponentLife;
import com.github.xushifustudio.libduckeys.context.ComponentRegistration;
import com.github.xushifustudio.libduckeys.context.ComponentRegistrationBuilder;
import com.github.xushifustudio.libduckeys.context.DuckContext;
import com.github.xushifustudio.libduckeys.helper.DefaultWaiter;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.helper.Waiter;
import com.github.xushifustudio.libduckeys.midi.MidiEvent;
import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class MidiReaderController implements Controller, ComponentLife {

    private final static String URI = MidiReaderService.URI;

    private MyReaderBuffer mBuffer;
    private DuckContext mDC;

    public MidiReaderController() {
    }


    private static class MyReaderBuffer implements MidiEventHandler {

        private final List<MidiEvent> buffer;
        private final Waiter waiter;
        private final String waiterTag = "MidiReaderController";

        MyReaderBuffer() {
            buffer = new ArrayList<>();
            waiter = new DefaultWaiter();
        }

        public MidiEvent read(int timeout) {
            final int timeout_min = 10;
            if (timeout < timeout_min) {
                timeout = timeout_min;
            }
            try {
                for (; ; ) {
                    MidiEvent evt = tryReadOne();
                    if (evt != null) {
                        return evt;
                    }
                    this.waiter.wait(waiterTag, timeout);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private MidiEvent tryReadOne() {
            final MidiReaderService.Response holder = new MidiReaderService.Response();
            this.runSafe(() -> {
                if (buffer.size() > 0) {
                    holder.event = buffer.remove(0);
                }
            });
            return holder.event;
        }

        @Override
        public void handle(MidiEvent me) {
            this.runSafe(() -> {
                this.checkBufferSize();
                buffer.add(me);
            });
            this.waiter.notify(waiterTag);
        }


        private void checkBufferSize() {
            final int size_max = 512;
            final int size_old = buffer.size();
            if (size_old < size_max) {
                return;
            }
            final int size_want = size_max / 2;
            Log.w(DuckLogger.TAG, "MidiReaderController: trim buffer size from:" + size_old + " to:" + size_want);
            while (size_want < buffer.size()) {
                buffer.remove(0);
            }
        }


        private synchronized void runSafe(Runnable r) {
            r.run();
        }
    }


    private synchronized MyReaderBuffer getBuffer() {
        MyReaderBuffer b = mBuffer;
        if (b == null) {
            b = loadBuffer();
            mBuffer = b;
        }
        return b;
    }

    private MyReaderBuffer loadBuffer() {
        MyReaderBuffer b = new MyReaderBuffer();
        mDC.getMidiRouter().setRx(b);
        return b;
    }


    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {
        hr.register(Want.GET, URI, this::handleRead);
        hr.register(Want.POST, URI, this::handleRead);
        hr.register(Want.DELETE, URI, this::h1);
    }

    private Have h1(Want w) {
        return null;
    }

    private Have handleRead(Want w) {
        try {
            MidiReaderService.Request req = MidiReaderService.decode(w);
            MyReaderBuffer buff = getBuffer();
            MidiEvent event = buff.read(req.timeout);
            MidiReaderService.Response resp = new MidiReaderService.Response();
            resp.event = event;
            return MidiReaderService.encode(resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void onCreate(ComponentContext cc) {
        mDC = cc.components.find(DuckContext.class);
    }

    @Override
    public ComponentRegistration init(ComponentContext cc) {
        ComponentRegistrationBuilder b = new ComponentRegistrationBuilder();
        b.setType(this.getClass());
        b.setInstance(this);
        b.onCreate(() -> {
            onCreate(cc);
        });
        return b.create();
    }
}
