package com.bitwormhole.libduckeys.ui.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bitwormhole.libduckeys.R;
import com.bitwormhole.libduckeys.context.LifeActivity;
import com.bitwormhole.libduckeys.context.LifeManager;
import com.bitwormhole.libduckeys.settings.SettingsManager;

public class HistoryDeviceActivity extends LifeActivity {

    private SettingsManager mSettingsManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        mSettingsManager = new SettingsManager(this);

        LifeManager lm = getLifeManager();
        lm.add(mSettingsManager);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history_device_list);
    }

}
