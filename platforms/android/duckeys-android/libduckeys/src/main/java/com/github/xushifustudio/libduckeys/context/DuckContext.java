package com.github.xushifustudio.libduckeys.context;

public class DuckContext {

    private UserContext currentUser;

    public UserContext getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserContext currentUser) {
        this.currentUser = currentUser;
    }
}
