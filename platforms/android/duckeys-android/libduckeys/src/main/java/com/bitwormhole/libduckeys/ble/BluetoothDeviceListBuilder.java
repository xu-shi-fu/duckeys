package com.bitwormhole.libduckeys.ble;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BluetoothDeviceListBuilder {

    private final Map<String, BluetoothScanningResult> mTable; // map<address,result>

    public BluetoothDeviceListBuilder() {
        mTable = new HashMap<>();
    }

    public BluetoothScanningResult[] results(boolean complete) {
        Collection<BluetoothScanningResult> all = mTable.values();
        BluetoothScanningResult[] array = all.toArray(new BluetoothScanningResult[all.size()]);
        if (complete) {
            Arrays.sort(array, (a, b) -> {
                String n1 = a.name + "";
                String n2 = b.name + "";
                return n1.compareTo(n2);
            });
        } else {
            Arrays.sort(array, (a, b) -> {
                return (a.index - b.index);
            });
        }
        return array;
    }

    public void append(BluetoothScanningResult item) {

        if (item == null) return;

        String key = keyFor(item);
        BluetoothScanningResult older = mTable.get(key);
        if (older == null) {
            item.index = nextIndex();
            mTable.put(key, item);
        } else {
            this.updateItem(older, item);
        }
    }

    private int nextIndex() {
        int count = mTable.size();
        return count;
    }

    private void updateItem(BluetoothScanningResult dest, BluetoothScanningResult src) {
        dest.address = src.address;
        dest.name = src.name;
        dest.ble1 = src.ble1;
        dest.ble2 = src.ble2;
        dest.device = src.device;
    }


    public String keyFor(BluetoothScanningResult item) {
        String addr = item.address + "";
        return addr.toUpperCase();
    }

    public void reset() {
        mTable.clear();
    }
}
