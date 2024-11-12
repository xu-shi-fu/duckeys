package com.github.xushifustudio.libduckeys.context;

import android.content.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class FrameworkContext {

    private FrameworkAPI api;
    private DuckAPI duck;
    private final Map<String, Object> attributes;

    public FrameworkContext() {
        Map<String, Object> attrs = new HashMap<>();
        this.attributes = Collections.synchronizedMap(attrs);
    }

    public static FrameworkContext getInstance(Context ctx) {
        Context ac = ctx.getApplicationContext();
        FrameworkContext fc = ac.getSystemService(FrameworkContext.class);
        if (fc == null) {
            throw new RuntimeException("no FrameworkContext at android:Context");
        }
        return fc;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public DuckAPI getDuck() {
        return duck;
    }

    public void setDuck(DuckAPI duck) {
        this.duck = duck;
    }

    public FrameworkAPI getApi() {
        return api;
    }

    public void setApi(FrameworkAPI api) {
        this.api = api;
    }
}
