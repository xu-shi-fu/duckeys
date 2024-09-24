package com.bitwormhole.libduckeys.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsLS {

    private final SettingsContext sc;

    public SettingsLS(SettingsContext sc) {
        this.sc = sc;
    }

    public SharedPreferences getSP() {
        int mode = Context.MODE_PRIVATE;
        return sc.context.getSharedPreferences("settings.json", mode);
    }

    public void save(String text) {

        if (text == null) {
            return;
        }
        if (text.equals("")) {
            return;
        }

        SharedPreferences.Editor editor = getSP().edit();
        editor.putString("settings", text);
        editor.commit();
    }

    public String load() {
        return getSP().getString("settings", "{}");
    }
}
