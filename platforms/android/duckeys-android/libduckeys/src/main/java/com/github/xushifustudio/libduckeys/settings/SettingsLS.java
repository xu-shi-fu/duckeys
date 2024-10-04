package com.github.xushifustudio.libduckeys.settings;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public final class SettingsLS {

    public SharedPreferences sp;
    public SharedPreferences.Editor editor;

    public SettingsLS() {
    }

    public void save(SettingProperty p) {
        try {
            String key = p.getClass().getName();
            Gson gs = new Gson();
            String value = gs.toJson(p);
            editor.putString(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> T load(Class<T> t) {
        try {
            String key = t.getName();
            Gson gs = new Gson();
            String value = sp.getString(key, "{}");
            return gs.fromJson(value, t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return t.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
