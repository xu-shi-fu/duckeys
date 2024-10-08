package com.github.xushifustudio.libduckeys.api.servers;

import android.content.Context;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.context.Components;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

public class ServerContext {

    public Server server;

    public Context context;

    public HandlerRegistry hr;

    public Components components;

}
