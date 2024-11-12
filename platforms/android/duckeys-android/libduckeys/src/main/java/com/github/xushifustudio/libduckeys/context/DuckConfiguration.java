package com.github.xushifustudio.libduckeys.context;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DuckConfiguration {

    private final List<ComponentRegistry> registryList;

    public DuckConfiguration() {
        this.registryList = new ArrayList<>();
    }

    public void add(ComponentRegistry cr) {
        if (cr == null) {
            return;
        }
        this.registryList.add(cr);
    }

    public ComponentRegistry[] all() {
        return this.registryList.toArray(new ComponentRegistry[0]);
    }
}
