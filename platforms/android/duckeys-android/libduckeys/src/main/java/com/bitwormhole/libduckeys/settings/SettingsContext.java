package com.bitwormhole.libduckeys.settings;

import android.content.Context;

public class SettingsContext {

    public final Context context;
    public boolean dirty;
    public SettingsDOM dom;

    public SettingsContext(Context ctx) {
        context = ctx;
    }

}
