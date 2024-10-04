package com.github.xushifustudio.libduckeys.context;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.DefaultServerBuilderFactory;
import com.github.xushifustudio.libduckeys.api.servers.Server;
import com.github.xushifustudio.libduckeys.api.servers.ServerBuilder;
import com.github.xushifustudio.libduckeys.api.servers.ServerBuilderFactory;
import com.github.xushifustudio.libduckeys.api.servers.ServerHolder;

public class DuckService extends LifeService {

    private final MyBinder mBinder; // facade
    private final ServerHolder mHolder; // inner

    public DuckService() {
        mBinder = new MyBinder();
        mHolder = new ServerHolder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.innerStartForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void innerStartForeground() {
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


    @Override
    public void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        LifeManager lm = getLifeManager();
        lm.add(mHolder);
        loadServer(mHolder);
    }


    private void loadServer(ServerHolder h) {
        Application app = this.getApplication();
        ComponentRegistry reg = (ComponentRegistry) app;
        ComponentContext cc = new ComponentContext();
        ComponentsLoader loader = new ComponentsLoader(cc);
        cc.context = app;
        loader.loadComponents(reg::registerComponents);
        Server ser = cc.components.find(Server.class);
        h.setServer(ser);
        h.setComponents(cc.components);
        h.setComponentContext(cc);
    }


    private class MyBinder extends Binder implements Server, DuckBinder {

        private Server getInner() {
            Server ser = mHolder.getServer();
            if (ser == null) {
                throw new RuntimeException("getServer without init");
            }
            return ser;
        }

        @Override
        public Have invoke(Want want) throws Exception {
            return getInner().invoke(want);
        }

        @Override
        public API findAPI(Want want) {
            return getInner().findAPI(want);
        }

        @Override
        public API getMidiWriterAPI() {
            return getInner().getMidiWriterAPI();
        }

        @Override
        public API getMidiReaderAPI() {
            return getInner().getMidiReaderAPI();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
