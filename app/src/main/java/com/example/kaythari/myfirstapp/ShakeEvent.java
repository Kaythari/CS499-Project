package com.example.kaythari.myfirstapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.List;


/**
 * Created by Kaythari on 4/28/2017.
 */

public class ShakeEvent extends AppCompatActivity implements SensorEventListener{

    private SensorManager sM;
    private Sensor s;
    private float accelShake; // shake acceleration
    private float accelCurrent; // current acceleration including gravity
    private float accelLast; // last acceleration and gravity
    int count;
    private TextView counter;
    private boolean b;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.shakephone);

        //Custom Font
        Typewriter t = (Typewriter)findViewById(R.id.typewriter);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/Buried Bones.ttf");
        t.setTypeface(custom);
        t.setCharacterDelay(150);
        t.animateText("Shake your sense out, you mortal!");
        count = 0;

        //Progressbar
        progressBar = (ProgressBar) findViewById(R.id.CircleBar);

        //check device compatibility with shake
        counter = (TextView) findViewById(R.id.counter);
        sM = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> list = sM.getSensorList(Sensor.TYPE_ALL);
        for (int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getType() == Sensor.TYPE_ACCELEROMETER)
            {
                b = false;
                s = sM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sM.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);

                accelCurrent = SensorManager.GRAVITY_EARTH;
                accelLast = SensorManager.GRAVITY_EARTH;
                accelShake = 0.0f;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        accelLast = accelCurrent;
        accelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = accelCurrent - accelLast;
        accelShake = accelShake * 0.9f + delta;

        if(accelShake > 13)
        {
            count++;
            counter.setText(Integer.toString(count));
            progressBar.setProgress(count * 5);

            if(count == 20)
            {
                Intent intent = new Intent(ShakeEvent.this, LocationTrack.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onResume() {
        super.onResume();
        sM.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sM.unregisterListener(this);
    }

    //disable back button
    @Override
    public void onBackPressed() {
    }
}

