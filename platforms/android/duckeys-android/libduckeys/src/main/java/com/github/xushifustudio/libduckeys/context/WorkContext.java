package com.github.xushifustudio.libduckeys.context;


// WorkContext 表示一个作品的上下文
public class WorkContext {

    private UserContext owner;
    private IWork api;

    public UserContext getOwner() {
        return owner;
    }

    public void setOwner(UserContext owner) {
        this.owner = owner;
    }

    public IWork getApi() {
        return api;
    }

    public void setApi(IWork api) {
        this.api = api;
    }
}
