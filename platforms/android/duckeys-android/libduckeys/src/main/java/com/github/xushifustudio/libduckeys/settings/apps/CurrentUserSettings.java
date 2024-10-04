package com.github.xushifustudio.libduckeys.settings.apps;

import com.github.xushifustudio.libduckeys.settings.SettingProperty;
import com.github.xushifustudio.libduckeys.settings.SettingScope;

public class CurrentUserSettings implements SettingProperty {

    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int scope() {
        return SettingScope.APP;
    }
}
