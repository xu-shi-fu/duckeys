package com.github.xushifustudio.libduckeys.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComponentManager implements Components, ComponentRegistering {

    private final Map<String, Holder> table; // map<[id|name|alias|class],holder>

    public ComponentManager() {
        this.table = new HashMap<>();
    }

    private synchronized void trans(Runnable r) {
        r.run();
    }

    private static class MyRegistry {

        final Set<String> keys;
        Holder value;

        MyRegistry() {
            this.keys = new HashSet<>();
            this.value = new Holder();
        }

        void addKey(String name) {
            if (name == null) {
                return;
            }
            this.value.name = name;
            keys.add(name);
        }

        void addKeys(String[] aliases) {
            if (aliases == null) {
                return;
            }
            for (String alias : aliases) {
                if (alias == null) {
                    continue;
                }
                keys.add(alias);
            }
            this.value.aliases = aliases;
        }

        void addKey(Class<?> t) {
            if (t == null) {
                return;
            }
            this.value.type = t;
            keys.add(t.getName());
        }

        void setInstance(Object inst) {
            if (inst == null) {
                return;
            }
            if (this.value.name == null) {
                this.addKey(inst.getClass().getName());
            }
            if (this.value.type == null) {
                this.addKey(inst.getClass());
            }
            this.value.instance = inst;
            this.value.type = inst.getClass();
        }

        void registerTo(Map<String, Holder> dst) {
            if (value == null) {
                return;
            }
            if (value.instance == null) {
                return;
            }
            for (String key : keys) {
                dst.put(key, value);
            }
        }
    }


    @Override
    public void register(Holder h) {
        MyRegistry reg = new MyRegistry();
        reg.addKey(h.type);
        reg.addKey(h.name);
        reg.addKeys(h.aliases);
        reg.setInstance(h.instance);
        this.trans(() -> {
            reg.registerTo(table);
        });
    }

    @Override
    public void register(String name, Object inst) {
        MyRegistry reg = new MyRegistry();
        reg.addKey(inst.getClass());
        reg.addKey(name);
        reg.setInstance(inst);
        this.trans(() -> {
            reg.registerTo(table);
        });
    }

    @Override
    public void register(Class<?> t, Object inst) {
        MyRegistry reg = new MyRegistry();
        reg.addKey(t);
        reg.setInstance(inst);
        this.trans(() -> {
            reg.registerTo(table);
        });
    }

    @Override
    public <T> T find(Class<T> t) {
        final Holder want = new Holder();
        want.name = t.getName();
        this.trans(() -> {
            Holder have = table.get(want.name);
            if (have != null) {
                want.instance = have.instance;
            }
        });
        return (T) want.instance;
    }

    @Override
    public <T> T findWithFilter(Filter f, Class<T> t) {
        final Holder want = new Holder();
        this.trans(() -> {
            Collection<Holder> all = table.values();
            for (Holder have : all) {
                if (f.accept(have.name, have.instance)) {
                    want.instance = have.instance;
                    break;
                }
            }
        });
        return (T) want.instance;
    }

    @Override
    public <T> T findById(String id, Class<T> t) {
        final Holder want = new Holder();
        want.name = id;
        this.trans(() -> {
            Holder have = table.get(want.name);
            if (have != null) {
                want.instance = have.instance;
            }
        });
        return (T) want.instance;
    }

    @Override
    public <T> List<T> listWithFilter(Filter f, Class<T> t) {
        List<T> res = new ArrayList<>();
        Set<String> names = new HashSet<>();
        this.trans(() -> {
            Collection<Holder> all = table.values();
            for (Holder have : all) {
                String id = have.name;
                if (names.contains(id)) {
                    continue;
                } else {
                    names.add(id);
                }
                if (f.accept(id, have.instance)) {
                    res.add((T) have.instance);
                }
            }
        });
        return res;
    }

    @Override
    public List<Object> listAll() {
        return listWithFilter((a1, a2) -> {
            return true;
        }, Object.class);
    }
}
