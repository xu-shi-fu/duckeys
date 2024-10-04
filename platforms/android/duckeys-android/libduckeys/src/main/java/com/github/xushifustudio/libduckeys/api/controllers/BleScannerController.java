package com.github.xushifustudio.libduckeys.api.controllers;

import com.github.xushifustudio.libduckeys.api.Have;
import com.github.xushifustudio.libduckeys.api.Want;
import com.github.xushifustudio.libduckeys.api.servers.Controller;
import com.github.xushifustudio.libduckeys.api.servers.HandlerRegistry;
import com.github.xushifustudio.libduckeys.api.servers.ServerContext;
import com.github.xushifustudio.libduckeys.api.services.BleScannerService;
import com.github.xushifustudio.libduckeys.ble.BLEDeviceScanner;

public final class BleScannerController implements Controller {

    private BLEDeviceScanner mScanner;

    public BleScannerController() {
      //  mScanner = new BLEDeviceScanner(null);
    }

    @Override
    public void registerSelf(ServerContext sc, HandlerRegistry hr) {

        hr.register(Want.POST, BleScannerService.URI_BLE_SCANNER_START, this::startScanning);
        hr.register(Want.POST, BleScannerService.URI_BLE_SCANNER_STOP, this::stopScanning);

    }

    private Have startScanning(Want w) {
        mScanner.start();
        return new Have();
    }

    private Have stopScanning(Want w) {
        mScanner.stop();
        return new Have();
    }
}
