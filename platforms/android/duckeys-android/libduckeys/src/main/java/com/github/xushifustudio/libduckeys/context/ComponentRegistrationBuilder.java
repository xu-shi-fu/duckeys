package com.github.xushifustudio.libduckeys.context;

public class ComponentRegistrationBuilder {

    private final LifeProxy lp = new LifeProxy();
    private final ComponentRegistration cr = new ComponentRegistration();

    public ComponentRegistrationBuilder() {
    }

    public static ComponentRegistrationBuilder newInstance(ComponentContext cc) {
        return new ComponentRegistrationBuilder();
    }

    public ComponentRegistrationBuilder onCreate(Runnable r) {
        lp.setOnCreate(r);
        return this;
    }

    public ComponentRegistrationBuilder onStart(Runnable r) {
        lp.setOnStart(r);
        return this;
    }

    public ComponentRegistrationBuilder onPause(Runnable r) {
        lp.setOnPause(r);
        return this;
    }

    public ComponentRegistrationBuilder onResume(Runnable r) {
        lp.setOnResume(r);
        return this;
    }

    public ComponentRegistrationBuilder onRestart(Runnable r) {
        lp.setOnRestart(r);
        return this;
    }

    public ComponentRegistrationBuilder onStop(Runnable r) {
        lp.setOnStop(r);
        return this;
    }

    public ComponentRegistrationBuilder onDestroy(Runnable r) {
        lp.setOnDestroy(r);
        return this;
    }

    public ComponentRegistrationBuilder setName(String name) {
        cr.name = name;
        return this;
    }

    public ComponentRegistrationBuilder setOrder(int order) {
        cr.order = order;
        return this;
    }

    public ComponentRegistrationBuilder setType(Class<?> t) {
        cr.type = t;
        return this;
    }

    public ComponentRegistrationBuilder setInstance(Object inst) {
        cr.instance = inst;
        return this;
    }

    public ComponentRegistration create() {

        // cr.name = "";
        // cr. order = "";
        // cr. type  = "";
        // cr. instance = "";

        cr.life = lp;
        return cr;
    }
}
