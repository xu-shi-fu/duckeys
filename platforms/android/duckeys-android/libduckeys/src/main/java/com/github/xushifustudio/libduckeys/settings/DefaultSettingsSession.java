package com.github.xushifustudio.libduckeys.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DefaultSettingsSession implements SettingSession {

    private final SettingContext mSettingContext;
    private final Map<Long, MyBankHolder> mBanks;
    private final Map<String, MyPropertyHolder> mProperties;


    public DefaultSettingsSession(SettingContext sc) {
        mSettingContext = sc;
        mBanks = new HashMap<>();
        mProperties = new HashMap<>();
    }

    private class MyBankHolder {
        long key; // = valueOf(scope)
        SettingBank bank;
        int scope;
        SharedPreferences preferences;
        SharedPreferences.Editor editor;


        SharedPreferences getSP() {
            SharedPreferences sp = preferences;
            if (sp == null) {
                int mode = Context.MODE_PRIVATE;
                String name = bank.getBankName(mSettingContext);
                sp = mSettingContext.context.getSharedPreferences(name, mode);
                preferences = sp;
            }
            return sp;
        }

        SharedPreferences.Editor getEditor() {
            SharedPreferences.Editor spe = editor;
            if (spe == null) {
                SharedPreferences sp = getSP();
                spe = sp.edit();
                editor = spe;
            }
            return spe;
        }
    }


    private static class MyPropertyHolder {
        String key;
        Class<?> type;
        SettingProperty instance;
        int scope;
        boolean dirty;
    }

    private MyPropertyHolder getPropertyHolder(Class<?> t, boolean create) {
        String key = t.getName();
        MyPropertyHolder h = mProperties.get(key);
        if (h == null && create) {
            h = new MyPropertyHolder();
            h.key = key;
            h.type = t;
            mProperties.put(key, h);
        }
        return h;
    }

    private MyBankHolder getBankHolder(long scope, boolean create) {
        MyBankHolder h = mBanks.get(scope);
        if (h == null && create) {
            h = new MyBankHolder();
            h.key = scope;
            h.scope = (int) scope;
            mBanks.put(scope, h);
        }
        return h;
    }


    private List<MyBankHolder> listBanksByScopeSet(int scopeSet) {
        List<MyBankHolder> dst = new ArrayList<>();
        Collection<MyBankHolder> src = mBanks.values();
        for (MyBankHolder h : src) {
            if ((h.scope & scopeSet) != 0) {
                dst.add(h);
            }
        }
        return dst;
    }


    private MyPropertyHolder loadProperty(Class<?> t) {
        MyPropertyHolder h = getPropertyHolder(t, false);
        if (h == null) {
            return null;
        }
        List<MyBankHolder> banks = listBanksByScopeSet(h.scope);
        SettingsLS ls = new SettingsLS();
        for (MyBankHolder bh : banks) {
            ls.sp = bh.getSP();
            Object obj = ls.load(t);
            if (obj != null) {
                h.instance = (SettingProperty) obj;
                return h;
            }
        }
        return h;
    }


    @Override
    public void put(SettingProperty sp) {
        Class<? extends SettingProperty> key = sp.getClass();
        MyPropertyHolder h = getPropertyHolder(key, false);
        if (h == null) {
            throw new RuntimeException("unsupported setting: " + key.getName());
        }
        h.dirty = true;
        h.instance = sp;
    }

    @Override
    public void put(SettingProperty sp, int scope) {
        Class<? extends SettingProperty> key = sp.getClass();
        MyPropertyHolder h = getPropertyHolder(key, false);
        if (h == null) {
            throw new RuntimeException("unsupported setting: " + key.getName());
        }
        h.dirty = true;
        h.instance = sp;
        h.scope |= scope;
    }

    @Override
    public <T> T get(Class<T> t) {
        MyPropertyHolder h = getPropertyHolder(t, false);
        if (h != null) {
            if (h.instance != null) {
                return (T) h.instance;
            }
        }
        h = loadProperty(t);
        if (h != null) {
            if (h.instance != null) {
                mProperties.put(h.key, h);
                return (T) h.instance;
            }
        }
        String key = t.getName();
        throw new RuntimeException("cannot find setting-property with name: " + key);
    }

    @Override
    public <T> T get(Class<T> t, T defaultValue) {
        MyPropertyHolder h = getPropertyHolder(t, false);
        if (h != null) {
            if (h.instance != null) {
                return (T) h.instance;
            }
        }
        h = loadProperty(t);
        if (h != null) {
            if (h.instance != null) {
                mProperties.put(h.key, h);
                return (T) h.instance;
            }
        }
        return defaultValue;
    }

    @Override
    public void commit() {
        Collection<MyBankHolder> banks = this.mBanks.values();
        Collection<MyPropertyHolder> props = this.mProperties.values();
        for (MyBankHolder bh : banks) {
            for (MyPropertyHolder ph : props) {
                this.saveProperty(ph, bh);
            }
            SharedPreferences.Editor editor = bh.editor;
            if (editor != null) {
                editor.commit();
            }
        }
        Log.i(DuckLogger.TAG, "DefaultSettingsSession: committed");
    }

    @Override
    public void rollback() {
        // todo: reset
    }


    private void saveProperty(MyPropertyHolder ph, MyBankHolder bh) {

        int scope1 = bh.scope;
        int scope2 = ph.scope;
        if ((scope1 & scope2) == 0) {
            return; // 交集为0，退出
        }

        if (ph.dirty) {
            ph.dirty = false;
        } else {
            return;
        }

        SettingsLS ls = new SettingsLS();
        ls.sp = bh.getSP();
        ls.editor = bh.getEditor();
        ls.save(ph.instance);
    }


    public void open() {

        SettingBank[] banks1 = mSettingContext.banks;
        SettingProperty[] props1 = mSettingContext.properties;

        for (SettingBank b1 : banks1) {
            MyBankHolder bh = getBankHolder(b1.scope(), true);
            bh.bank = b1;
        }

        for (SettingProperty p1 : props1) {
            Class<? extends SettingProperty> t = p1.getClass();
            MyPropertyHolder ph = getPropertyHolder(t, true);
            ph.scope = p1.scope();
            ph.dirty = false;
            ph.instance = null;
        }

        Log.i(DuckLogger.TAG, "DefaultSettingsSession: open");
    }

    @Override
    public void close() throws IOException {
        Log.i(DuckLogger.TAG, "DefaultSettingsSession: closed");
    }
}
