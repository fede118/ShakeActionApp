package com.example.shaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {
//    const fuerza G a supperar
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
//    const intervalo entre shakes
    private static final int SHAKE_BLOCK_TIME_MS = 500;

    private OnShakeListener listener;
    private long shakeTimestamp;

    public void setOnShakeListener(OnShakeListener listener) {
        this.listener = listener;
    }

    public interface OnShakeListener {
        void onShake();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (listener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (shakeTimestamp + SHAKE_BLOCK_TIME_MS > now) {
                    return;
                }

                shakeTimestamp = now;

                listener.onShake();
            }
        }
    }
}
