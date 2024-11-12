package com.github.xushifustudio.libduckeys.context;

import com.github.xushifustudio.libduckeys.midi.MERT;
import com.github.xushifustudio.libduckeys.midi.MidiEventRT;
import com.github.xushifustudio.libduckeys.midi.MidiUriConnection;

public final class DuckContext {

    private final FrameworkContext parent;
    private DuckAPI api;
    private UserContext currentUser;
    private MidiUriConnection currentConnection;
    private MidiEventRT midiRouter;
    private Components components;


    public DuckContext(FrameworkContext fc) {
        this.parent = fc;
        this.midiRouter = new MidiEventRT("owner:DuckContext");
    }

    public DuckAPI getApi() {
        return api;
    }

    public void setApi(DuckAPI api) {
        this.api = api;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public FrameworkContext getParent() {
        return parent;
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
