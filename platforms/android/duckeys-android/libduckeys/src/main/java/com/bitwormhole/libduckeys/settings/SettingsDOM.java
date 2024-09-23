package com.bitwormhole.libduckeys.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SettingsDOM implements Serializable {

    private DeviceSettings devices;


    public DeviceSettings getDevices() {
        return devices;
    }

    public void setDevices(DeviceSettings devices) {
        this.devices = devices;
    }
}
