package com.bitwormhole.libduckeys.settings;

import java.io.Serializable;
import java.util.List;

public class DeviceSettings implements Serializable {

    private List<DeviceInfo> history;
    private DeviceInfo current;

    public DeviceSettings() {
    }

    public List<DeviceInfo> getHistory() {
        return history;
    }

    public void setHistory(List<DeviceInfo> history) {
        this.history = history;
    }

    public DeviceInfo getCurrent() {
        return current;
    }

    public void setCurrent(DeviceInfo current) {
        this.current = current;
    }
}
