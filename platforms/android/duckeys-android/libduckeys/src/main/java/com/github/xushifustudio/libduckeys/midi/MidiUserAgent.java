package com.github.xushifustudio.libduckeys.midi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.clients.Task;
import com.github.xushifustudio.libduckeys.api.clients.TaskContext;
import com.github.xushifustudio.libduckeys.api.clients.TaskManager;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.context.BaseLife;
import com.github.xushifustudio.libduckeys.context.DuckClient;
import com.github.xushifustudio.libduckeys.context.DuckContext;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;
import com.github.xushifustudio.libduckeys.helper.IO;

import java.io.IOException;

public class MidiUserAgent extends BaseLife implements MERT, MidiEventDispatcher {

    private final MidiEventRT mRT;
    private final Context mContext;
    private final DuckClient mClient;
    private final TaskManager mTasks;
    private boolean mWorking;
    private MidiWriter mWriter;

    public MidiUserAgent(Context ctx, DuckClient dc, TaskManager tm) {
        mRT = new MidiEventRT("owner:MidiUserAgent");
        mContext = ctx;
        mClient = dc;
        mTasks = tm;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mWorking = true;
        bind();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWorking = false;
        unbind();
    }

    private void bind() {
        mRT.setTx(this::write);
        startRxLoop();
    }

    private void startRxLoop() {
        Task task = (tc) -> {
            Server ser = mClient.waitForServerReady();
            this.runMidiEventLoop(ser);
        };
        TaskContext x = mTasks.createNewTask(task);
        x.worker = mTasks.getDefaultWorker();
        x.execute();
    }


    private void runMidiEventLoop(Server ser) {
        API api_w = ser.getMidiWriterAPI();
        API api_r = ser.getMidiReaderAPI();
        ApiMidiWriter writer = null;
        ApiMidiReader reader = null;
        try {
            reader = new ApiMidiReader(api_r);
            writer = new ApiMidiWriter(api_w);
            mWriter = writer;
            while (mWorking) {
                this.tryRead(reader);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWriter = null;
            IO.close(reader);
            IO.close(writer);
        }
    }


    private void unbind() {
        //     MERT router = mDC.getMidiRouter();
        // router.setRx(null);
        mRT.setTx(null);
    }

    private void tryRead(ApiMidiReader reader) {
        try {
            MidiEvent evt = reader.read(1000);
            if (evt == null) {
                return;
            }
            Log.i(DuckLogger.TAG, "" + evt);
            mRT.handle(evt);
        } catch (Exception e) {
            if (isTimeout(e)) {
                String msg = e.getMessage();
                Log.w(DuckLogger.TAG, "MidiUserAgent.tryRead:" + msg);
                return;
            }
            e.printStackTrace();
        }
    }

    private boolean isTimeout(Exception e) {
        if (e == null) {
            return false;
        }
        final String keyword = "timeout";
        final String msg = e.getMessage();
        if (msg == null) {
            return false;
        }
        return msg.contains(keyword);
    }


    private void write(MidiEvent me) {
        MidiWriter to = mWriter;
        if (to == null) {
            return;
        }
        try {
            to.write(me);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public MidiEventDispatcher getTx() {
        return this;
    }

    @Override
    public void setRx(MidiEventHandler rx) {
        mRT.setRx(rx);
    }

    @Override
    public void dispatch(MidiEvent me) {
        Task task = (tc) -> {
            mRT.dispatch(me);
        };
        TaskContext tc1 = mTasks.createNewTask(task);
        tc1.worker = mTasks.getMidiWorker();
        tc1.execute();
    }
}
