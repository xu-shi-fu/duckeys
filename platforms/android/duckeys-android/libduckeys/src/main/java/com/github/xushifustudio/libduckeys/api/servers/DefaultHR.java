package com.github.xushifustudio.libduckeys.api.servers;

import android.util.Log;

import com.github.xushifustudio.libduckeys.api.API;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.helper.DuckLogger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultHR implements HandlerRegistry {

    private final Map<String, Holder> table;

    DefaultHR() {
        HashMap<String, Holder> t1 = new HashMap<>();
        table = Collections.synchronizedMap(t1);
    }


    private static class Holder {
        String method;
        String url;
        API api;
    }

    private String keyFor(Holder h) {
        String m = h.method;
        String u = h.url + "";
        int idx = u.indexOf('?');
        if (idx >= 0) {
            u = u.substring(0, idx);
        }
        return m + "(" + u + ")";
    }

    private API innerFind(Holder h1) {
        String key = keyFor(h1);
        Holder h2 = table.get(key);
        if (h2 == null) {
            throw new RuntimeException("no handler for api, key=" + key);
        }
        if (h2.api == null) {
            throw new RuntimeException("handler api is nil, key=" + key);
        }
        return h2.api;
    }


    @Override
    public void register(String method, String url, API api) {
        Holder h = new Holder();
        h.api = api;
        h.url = url;
        h.method = method;
        String key = keyFor(h);
        Holder older = table.get(key);
        if (older != null) {
            Log.w(DuckLogger.TAG, "api handlers are duplicated with key: " + key);
        }
        table.put(key, h);
    }


    @Override
    public API findAPI(String method, String url) {
        Holder h = new Holder();
        h.url = url;
        h.method = method;
        return innerFind(h);
    }

    @Override
    public API findAPI(Want want) {
        Holder h = new Holder();
        h.url = want.url;
        h.method = want.method;
        return innerFind(h);
    }
}
