package com.bitwormhole.libduckeys.midi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.conn.MockMidiConnection;
import com.bitwormhole.libduckeys.helper.DuckLogger;

import java.io.IOException;

public class MidiRouterService extends Service {

    private final MyEventRT mEventRT = new MyEventRT();
    private final MyBinder mBinder = new MyBinder();
    private MidiUriConnection mCurrentConn;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationChannel channel = makeChannel();
        String channel_id = channel.getId();
        final long now = System.currentTimeMillis();
        final int the_notification_id = 9527;

        Context ctx = getApplicationContext();
        Notification.Builder nb = new Notification.Builder(ctx, channel_id);

        nb.setContentTitle("midi_router_title")
                .setContentText("midi_router_text")
                .setShowWhen(true).setWhen(now);

        startForeground(the_notification_id, nb.build());
        return super.onStartCommand(intent, flags, startId);
    }


    private NotificationChannel makeChannel() {
        final String channel_id = "1";
        final String channel_name = "duckeys_ch_name";
        NotificationChannel nc = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_MIN);
        nc.setDescription("duckeys_desc");
        NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(nc);
        return nc;
    }

    private class MyEventRT extends MidiEventRT {

        @Override
        public void dispatch(MidiEvent me) {
            super.dispatch(me);
            String msg = "MidiRouterService.MyTxStream.write: " + me;
            Log.i(DuckLogger.TAG, msg);
            this.handle(me);
        }

        @Override
        public void handle(MidiEvent me) {
            super.handle(me);
        }
    }


    private class MyBinder extends Binder implements MidiRouterBinder {

        @Override
        public String hello(String msg) {
            return "hello, " + msg;
        }

        @Override
        public MidiEventDispatcher getTx() {
            return mEventRT;
        }

        @Override
        public void setRx(MidiEventHandler rx) {
            mEventRT.setRx(rx);
        }

        @Override
        public MidiConfiguration configuration() {
            return null;
        }

        @Override
        public void connect(MidiConfiguration cfg) throws IOException {

            this.disconnect();

            Context ctx = MidiRouterService.this.getApplicationContext();
            MidiUriAgent agent = new MidiUriAgent(ctx);
            MidiUriConnection conn = agent.open(cfg.uri);

            setCurrentConnection(conn);
        }

        @Override
        public void disconnect() {
            MidiUriConnection mock = new MockMidiConnection();
            setCurrentConnection(mock);
        }

        @Override
        public int state() {
            return 0;
        }
    }


    private void setCurrentConnection(MidiUriConnection next) {
        MidiUriConnection prev = mCurrentConn;
        mCurrentConn = next;

        releaseConnection(prev);

        if (next == null) return;
        next.setRx(mEventRT);
        mEventRT.setTx(next.getTx());
    }

    private void releaseConnection(MidiUriConnection conn) {
        if (conn == null) return;
        conn.setRx(null);
        mEventRT.setTx(null);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
