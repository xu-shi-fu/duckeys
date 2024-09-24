package com.bitwormhole.libduckeys.settings;

import android.content.Context;

import com.bitwormhole.libduckeys.context.BaseLife;

public final class SettingsManager extends BaseLife {

    private final SettingsContext mContext;

    public SettingsManager(Context ctx) {
        mContext = new SettingsContext(ctx);
    }

    public void load() {
        SettingsLoader loader = new SettingsLoader();
        loader.load(mContext);
    }

    public void save() {
        mContext.dirty = false;
        SettingsSaver saver = new SettingsSaver();
        saver.save(mContext);
    }

    public void markAsDirty() {
        mContext.dirty = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        this.load();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mContext.dirty) {
            this.save();
        }
    }
}
