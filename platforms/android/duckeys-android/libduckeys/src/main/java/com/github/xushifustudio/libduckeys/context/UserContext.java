package com.github.xushifustudio.libduckeys.context;

import com.github.xushifustudio.libduckeys.settings.apps.CurrentUserSettings;



// UserContext 表示一个用户的上下文
public class UserContext {

    private CurrentUserSettings settings;

    private  DuckContext duck ;

    public DuckContext getDuck() {
        return duck;
    }

    public void setDuck(DuckContext duck) {
        this.duck = duck;
    }
}
