package com.github.xushifustudio.libduckeys.ui.box2;

public class B2PropertyHolder {

    public String name;
    public B2State state;
    public B2Property property;
    public B2PropertyHandler handler;


    public B2PropertyHolder() {
    }

    public B2PropertyHolder(B2PropertyHolder src) {
        if (src != null) {
            this.name = src.name;
            this.state = src.state;
            this.property = src.property;
            this.handler = src.handler;
        }
    }

}
