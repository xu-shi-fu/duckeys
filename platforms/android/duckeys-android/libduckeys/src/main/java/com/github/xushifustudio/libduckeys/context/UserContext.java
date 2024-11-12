package com.github.xushifustudio.libduckeys.context;

import com.github.xushifustudio.libduckeys.settings.apps.CurrentUserSettings;


// UserContext 表示一个用户的上下文
public final class UserContext {

    private final DuckContext parent;
    private UserAPI api;
    private CurrentUserSettings settings;


    public UserContext(DuckContext dc) {
        this.parent = dc;
    }


    public UserAPI getApi() {
        return api;
    }

    public void setApi(UserAPI api) {
        this.api = api;
    }

    public CurrentUserSettings getSettings() {
        return settings;
    }

    public void setSettings(CurrentUserSettings settings) {
        this.settings = settings;
    }

    public DuckContext getParent() {
        return parent;
    }
}
