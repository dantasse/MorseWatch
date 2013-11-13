package com.dantasse.hellosmartwatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.aef.sensor.Sensor;
import com.sonyericsson.extras.liveware.extension.util.R;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEvent;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEventListener;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorException;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorManager;

public class HelloWatchExtension extends ControlExtension {

    int width;
    int height;

    RelativeLayout layout;
    Canvas canvas;
    Bitmap bitmap;
    TextView textView;

    FileWriter dataCsv;

    // don't think this ever actually does anything...
    private static final int SENSOR_REFRESH_RATE = 100;

    AccessorySensorManager manager;
    AccessorySensor accelerometer;
    private final AccessorySensorEventListener mListener = new AccessorySensorEventListener() {
        @Override
        public void onSensorEvent(AccessorySensorEvent sensorEvent) {
            
            updateVisual(sensorEvent);
//            float[] vals = sensorEvent.getSensorValues();
//            try {
//                dataCsv.append(vals[0] + "," + vals[1] + "," + vals[2] + "," + "Looking_at\n");
//            } catch (IOException ioe) {
//                Log.e(HelloWatchExtensionService.LOG_TAG, "Error opening file");
//            }
        }
    };

    void sendToCsv(float x, float y, float z, String label) {

    }

    public HelloWatchExtension(Context context, String hostAppPackageName) {
        super(context, hostAppPackageName);

        width = getSupportedControlWidth(context);
        height = getSupportedControlHeight(context);

        layout = new RelativeLayout(context);
        textView = new TextView(context);
        textView.setText("Hello watch!");
        textView.setTextSize(9);
        textView.setGravity(Gravity.LEFT);
        textView.setTextColor(Color.WHITE);
        textView.layout(0, 0, width, height);
        layout.addView(textView);

        manager = new AccessorySensorManager(context, hostAppPackageName);
        accelerometer = manager.getSensor(Registration.SensorTypeValue.ACCELEROMETER);
    }

    @Override
    public void onResume() {
        Log.d(HelloWatchExtensionService.LOG_TAG, "Starting control");

        try {
            File file = new File("/sdcard", "WatchAccelData.csv");
            dataCsv = new FileWriter(file, true /*append*/);
        } catch (IOException ioe) {
            Log.e(HelloWatchExtensionService.LOG_TAG, "Error opening file");
            Log.e(HelloWatchExtensionService.LOG_TAG, ioe.getMessage());
            ioe.printStackTrace();
        }
        // Note: Setting the screen to be always on will drain the accessory
        // battery. It is done here solely for demonstration purposes
        setScreenState(Control.Intents.SCREEN_STATE_ON);

        // Start listening for sensor updates.
        register();

        updateVisual(null);
    }

    @Override
    public void onPause() {
        Log.d(HelloWatchExtensionService.LOG_TAG, "Stopping control");
        try {
            dataCsv.close();
        } catch (IOException ioe) {
            Log.e(HelloWatchExtensionService.LOG_TAG, "error closing file");
        }
        unregister();
    }

    @Override
    public void onDestroy() {
        unregisterAndDestroy();
    }

    private void unregisterAndDestroy() {
        unregister();
        accelerometer = null;
    }

    private void register() {
        Log.d(HelloWatchExtensionService.LOG_TAG, "Register listener");
        if (accelerometer != null) {
            try {
                // it seems that you can do this, or do
                // registerInterruptListener (below) but
                // registerFixedRateListener seems to just hang forever.
                // registerListener here
                // seems to have the fastest sampling rate, around 4-5Hz.
                accelerometer.registerListener(mListener,
                        Sensor.SensorRates.SENSOR_DELAY_FASTEST,
                        Sensor.SensorInterruptMode.SENSOR_INTERRUPT_ENABLED);
                // accelerometer.registerInterruptListener(mListener);
            } catch (AccessorySensorException e) {
                Log.d(HelloWatchExtensionService.LOG_TAG, "Failed to register listener");
            }
        }
    }

    private void unregister() {
        if (accelerometer != null) {
            accelerometer.unregisterListener();
        }
    }

    /** where the classification happens */
    private String determineGesture(float x, float y, float z) {
        // janky classification method from a hand-copied SVM from weka
        if (x < -4.285) {
            return "Side";
        } else if (z < 7.806) {
            if (x < 5.511) {
                return "Behind head";
            } else { //x > 5.511
                return "Raising hand";
            }
        } else { // x > -4.28 and z > 7.806)
            return "Looking at";
        }
    }
    
    private void updateVisual(AccessorySensorEvent sensorEvent) {
        if (sensorEvent != null) {
            float[] vals = sensorEvent.getSensorValues();
            String xStr = String.format("%.2f", vals[0]);
            String yStr = String.format("%.2f", vals[1]);
            String zStr = String.format("%.2f", vals[2]);
            String valString = "x: " + xStr + "\ny: " + yStr + "\nz: " + zStr;
//            Log.d(HelloWatchExtensionService.LOG_TAG, valString);
            String gesture = determineGesture(vals[0], vals[1], vals[2]);
            
            
            textView.setText(valString + "\n" + gesture);
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(bitmap);
        layout.draw(canvas);
        showBitmap(bitmap);
    }

    public static int getSupportedControlWidth(Context context) {
        return context.getResources().getDimensionPixelSize(
                R.dimen.smart_watch_control_width);
    }

    public static int getSupportedControlHeight(Context context) {
        return context.getResources().getDimensionPixelSize(
                R.dimen.smart_watch_control_height);
    }
}
