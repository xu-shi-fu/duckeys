package com.github.xushifustudio.libduckeys.conn.bleconn;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import androidx.annotation.NonNull;

import com.github.xushifustudio.libduckeys.helper.DuckLogger;

public class BleGattCallback extends BluetoothGattCallback {

    private final BleConnContext mContext;

    public BleGattCallback(BleConnContext cc) {
        mContext = cc;
    }


    private void onBleDeviceConnected(BluetoothGatt gatt) {
        mContext.gatt = gatt;
        gatt.discoverServices();
    }


    private void onBleDeviceDisconnected(BluetoothGatt gatt) {
        mContext.rtx.setTx(null);
        mContext.gatt = null;
    }


    private void checkoutBleMidi1p0(BluetoothGatt gatt) throws SecurityException {
        try {
            BleAdapter adapter = BleAdapter.getInstance(mContext, gatt);
            mContext.adapter = adapter;
            mContext.ready = true;
        } catch (SecurityException e) {
            e.printStackTrace();
            mContext.error = e;
        } catch (Exception e) {
            // e.printStackTrace();
            mContext.error = e;
        }
    }


    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i(DuckLogger.TAG, "onConnectionStateChange(STATE_CONNECTED)");
                    onBleDeviceConnected(gatt);
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.i(DuckLogger.TAG, "onConnectionStateChange(STATE_DISCONNECTED)");
                    onBleDeviceDisconnected(gatt);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.i(DuckLogger.TAG, "onServicesDiscovered()");
            checkoutBleMidi1p0(gatt);
        }
    }

    @Override
    public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
        super.onCharacteristicRead(gatt, characteristic, value, status);
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
        if (status == BluetoothGatt.GATT_SUCCESS) {
            mContext.runWithinTransaction(() -> {
                BleTxContext prev = mContext.dispatching;
                mContext.dispatching = null;
                if (prev != null) {
                    prev.done = true;
                }
            });
            mContext.forAdapter((ada) -> {
                ada.flush();
            });
        }
    }

    @Override
    public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value) {
        super.onCharacteristicChanged(gatt, characteristic, value);
    }
}
