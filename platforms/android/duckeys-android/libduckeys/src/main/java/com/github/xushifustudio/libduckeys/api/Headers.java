package com.github.xushifustudio.libduckeys.api;

import java.util.HashMap;
import java.util.Map;

public class Headers {

    private final Map<String, String> table = new HashMap<>();

    public Headers() {
    }

    public void put(String name, String value) {
        if (name != null && value != null) {
            table.put(name, value);
        }
    }

    public String get(String name, String defaultValue) {
        String value = table.get(name);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public String getRequired(String name) {
        String value = table.get(name);
        if (value == null) {
            throw new RuntimeException("no header with name: " + name);
        }
        return value;
    }
}
