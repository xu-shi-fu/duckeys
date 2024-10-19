package com.github.xushifustudio.libduckeys.ui.box2;

import java.util.ArrayList;
import java.util.List;

public class B2PropertyQuery {

    // public String name; // the main name
    public final List<String> names;
    public B2State state; // null as normal (default)
    public B2Property property;

    public boolean ok;

    public B2PropertyQuery() {
        this.names = new ArrayList<>();
    }

}
