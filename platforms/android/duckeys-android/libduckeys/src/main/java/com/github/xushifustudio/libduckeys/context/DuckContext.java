package com.github.xushifustudio.libduckeys.context;

import com.github.xushifustudio.libduckeys.midi.MERT;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

public class DuckContext {

    private UserContext currentUser;
    private MidiUriConnection currentConnection;
    private MidiEventRT midiRouter;


    public DuckContext() {
        this.midiRouter = new MidiEventRT();
    }

    public MidiEventRT getMidiRouter() {
        return midiRouter;
    }

    public void setMidiRouter(MidiEventRT midiRouter) {
        this.midiRouter = midiRouter;
    }

    public MidiUriConnection getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(MidiUriConnection currentConnection) {
        this.currentConnection = currentConnection;
    }

    public UserContext getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserContext currentUser) {
        this.currentUser = currentUser;
    }
}
