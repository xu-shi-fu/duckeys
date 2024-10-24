package com.github.xushifustudio.libduckeys.helper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.xushifustudio.libduckeys.context.BaseLife;
import com.github.xushifustudio.libduckeys.instruments.SensorBuffer;

public class AccelerometerController extends BaseLife {

    private final Context context;
    private Sensor mSensor;
    private SensorManager mSensorManager;
    private final MySensorListener mSensorListener;
    private final SensorBuffer mBuffer;

    public AccelerometerController(Context ctx, SensorBuffer buffer) {
        this.context = ctx;
        this.mSensorListener = new MySensorListener();
        this.mBuffer = buffer;
    }

    private class MySensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];

            //    StringBuilder msg = new StringBuilder();
            //    msg.append("onSensorChanged");
            //    msg.append(" x:").append(x);
            //    msg.append(" y:").append(y);
            //    msg.append(" z:").append(z);
            // Log.d(DuckLogger.TAG, msg.toString());

            SensorBuffer.Sample sample = new SensorBuffer.Sample();
            sample.time = System.currentTimeMillis();
            sample.x = x;
            sample.y = y;
            sample.z = z;
            mBuffer.push(sample);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorManager sm = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        this.mSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mSensorManager = sm;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSensor != null && mSensorManager != null) {
            mSensorManager.registerListener(mSensorListener, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSensor != null && mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorListener, mSensor);
        }
    }
}
