package com.github.xushifustudio.libduckeys.settings.apps;

import com.github.xushifustudio.libduckeys.settings.SettingProperty;
import com.github.xushifustudio.libduckeys.settings.SettingScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentDeviceSettings implements Serializable, SettingProperty {

    private DeviceInfo device;
    private boolean Enabled;

    public CurrentDeviceSettings() {
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    public DeviceInfo getDevice() {
        return device;
    }

    public void setDevice(DeviceInfo device) {
        this.device = device;
    }

    @Override
    public int scope() {
        return SettingScope.APP;
    }
}
