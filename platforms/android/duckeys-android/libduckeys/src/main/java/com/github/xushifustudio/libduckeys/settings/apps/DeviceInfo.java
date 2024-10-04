package com.github.xushifustudio.libduckeys.settings.apps;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

    private String name;
    private String url;
    private String scheme; // [usb|ble|wifi|virtual]

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
