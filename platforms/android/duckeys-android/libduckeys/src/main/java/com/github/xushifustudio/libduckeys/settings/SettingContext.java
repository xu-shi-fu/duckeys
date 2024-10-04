package com.github.xushifustudio.libduckeys.settings;

import android.content.Context;

import com.github.xushifustudio.libduckeys.settings.apps.CurrentUserSettings;

import java.util.List;

public final class SettingContext {

    public SettingBank[] banks;
    public SettingProperty[] properties;
    public CurrentUserSettings current;
    public Context context;

    public SettingContext() {
        banks = new SettingBank[0];
        properties = new SettingProperty[0];
    }

    public void setBanks(List<SettingBank> list) {
        if (list == null) {
            return;
        }
        int size = list.size();
        banks = list.toArray(new SettingBank[size]);
    }

    public void setProperties(List<SettingProperty> list) {
        if (list == null) {
            return;
        }
        int size = list.size();
        properties = list.toArray(new SettingProperty[size]);
    }
}
