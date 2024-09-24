package com.bitwormhole.libduckeys.settings;

import com.google.gson.Gson;

public class SettingsSaver {

    public void save(SettingsContext sc) {
        SettingsDOM dom = sc.dom;
        if (dom == null) {
            return;
        }
        try {
            SettingsLS ls = new SettingsLS(sc);
            Gson gs = new Gson();
            String text = gs.toJson(dom);
            ls.save(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
