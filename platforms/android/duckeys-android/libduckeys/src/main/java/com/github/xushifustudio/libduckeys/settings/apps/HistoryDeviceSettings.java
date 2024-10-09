package com.github.xushifustudio.libduckeys.settings.apps;

import com.github.xushifustudio.libduckeys.settings.SettingProperty;
import com.github.xushifustudio.libduckeys.settings.SettingScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryDeviceSettings implements Serializable, SettingProperty {

    private List<DeviceInfo> devices;

    public HistoryDeviceSettings() {
    }


    // 向历史列表中添加一条记录
    public void add(DeviceInfo di) {
        List<DeviceInfo> list = devices;
        if (list == null) {
            list = new ArrayList<>();
            devices = list;
        }
        list.add(di);
    }

    // dedup 去除列表中的重复项
    public void dedup() {
        List<DeviceInfo> src = devices;
        Map<String, DeviceInfo> tmp = new HashMap<>();
        if (src != null) {
            for (DeviceInfo item : src) {
                String key = item.getUrl();
                tmp.put(key, item);
            }
        }
        List<DeviceInfo> dst = new ArrayList<>();
        tmp.values().forEach((item) -> {
            dst.add(item);
        });
        devices = dst;
    }

    public List<DeviceInfo> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceInfo> devices) {
        this.devices = devices;
    }

    @Override
    public int scope() {
        return SettingScope.APP;
    }
}
