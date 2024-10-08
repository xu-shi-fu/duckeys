package com.github.xushifustudio.libduckeys.settings.apps;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

    private String name;
    private String url;
    private String scheme; // [usb|ble|wifi|virtual]
    private long connectedAt; // 上一次建立连接的时间

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

    public long getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(long connectedAt) {
        this.connectedAt = connectedAt;
    }
}
