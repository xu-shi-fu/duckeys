package com.bitwormhole.libduckeys.settings;

import android.content.SharedPreferences;
import android.util.Log;

import com.bitwormhole.libduckeys.helper.DuckLogger;
import com.google.gson.Gson;

public class SettingsLoader {

    public void load(SettingsContext sc) {
        try {
            SettingsLS ls = new SettingsLS(sc);
            String text = ls.load();
            Gson gs = new Gson();
            sc.dom = gs.fromJson(text, SettingsDOM.class);
        } catch (Exception e) {
            e.printStackTrace();
            sc.dom = loadDefaultSettings();
        }
    }

    private SettingsDOM loadDefaultSettings() {
        Log.w(DuckLogger.TAG, "load default settings");
        return new SettingsDOM();
    }

}
