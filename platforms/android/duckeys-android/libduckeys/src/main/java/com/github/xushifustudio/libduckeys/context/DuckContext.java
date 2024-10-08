package com.github.xushifustudio.libduckeys.context;

import com.github.xushifustudio.libduckeys.midi.MidiEventHandler;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

public class DuckContext {

    private UserContext currentUser;
    private MidiUriConnection currentConnection;
    private MidiEventHandler mainMidiEventHandler;

    public DuckContext() {
    }

    public MidiUriConnection getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(MidiUriConnection currentConnection) {
        this.currentConnection = currentConnection;
    }

    public MidiEventHandler getMainMidiEventHandler() {
        return mainMidiEventHandler;
    }

    public void setMainMidiEventHandler(MidiEventHandler mainMidiEventHandler) {
        this.mainMidiEventHandler = mainMidiEventHandler;
    }

    public UserContext getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserContext currentUser) {
        this.currentUser = currentUser;
    }
}
