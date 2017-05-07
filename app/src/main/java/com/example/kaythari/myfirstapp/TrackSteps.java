package com.example.kaythari.myfirstapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Kaythari on 4/28/2017.
 */

public class TrackSteps extends Activity implements SensorEventListener {

    private SensorManager sM;
    private Sensor s;
    private TextView textView;

    //values to calculate steps
    private float previous;
    private float current;
    private int numSteps;
    private int triggerpoint;
    private float acceleration;

    ProgressBar progressBar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.distancetrack);

        //custom font
        Typewriter t = (Typewriter)findViewById(R.id.typewriter);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/Buried Bones.ttf");
        t.setTypeface(custom);
        t.setCharacterDelay(150);
        t.animateText("Shake your sense out, you mortal!");

        //progress bar
        progressBar = (ProgressBar) findViewById(R.id.CircleBar);

        sM = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        s = sM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sM.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

        textView = (TextView) findViewById(R.id.stepcount);

        previous = 0;
        current = 0;
        numSteps = 0;
        acceleration = 0.00f;
        triggerpoint = 10;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//method1
//        Sensor sensor = event.sensor;
//        float[] values = event.values;
//        int value = -1;
//
//        if (values.length > 0) {
//            value = (int) values[0];
//        }
//
//        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//            textView.setText("Steps: " + value);
//        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
//            // For test only. Only allowed value is 1.0 i.e. for step taken
//            textView.setText("Steps: " + value);
//        }

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        current = y;
        if(Math.abs(current - previous) > triggerpoint)
        {
            numSteps++;
            textView.setText("Steps: " + (String.valueOf(numSteps)));
            progressBar.setProgress(numSteps * 2);

            if(numSteps == 50)
            {
//                Intent intent = new Intent(TrackSteps.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        }
        previous = y;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        sM.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        sM.unregisterListener(this, s);
    }

    //disable back button
    @Override
    public void onBackPressed() {
    }
}
