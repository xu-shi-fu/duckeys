package com.github.xushifustudio.libduckeys.settings;

import java.io.Closeable;

public interface SettingSession extends Closeable {

    void put(SettingProperty sp);

    void put(SettingProperty sp, int scope);

    <T> T get(Class<T> t);

    <T> T get(Class<T> t, T defaultValue);

    void commit();

    void rollback();

}
