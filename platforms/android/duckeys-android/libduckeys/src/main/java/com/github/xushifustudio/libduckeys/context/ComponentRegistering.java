package com.github.xushifustudio.libduckeys.context;

public interface ComponentRegistering {


    public class Holder {

        public String name;
        public String[] aliases;
        public Class<?> type;
        public Object instance;

    }

    void register(Holder h);

    void register(String name, Object inst);

    void register(Class<?> t, Object inst);

}
