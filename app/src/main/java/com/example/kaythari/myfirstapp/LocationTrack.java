package com.example.kaythari.myfirstapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Kaythari on 4/29/2017.
 */

public class LocationTrack extends AppCompatActivity{

    private TextView textview;
    Location lastLocation = null;
    double distance;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.distancetrack);

        //Custom Font
        Typewriter t = (Typewriter)findViewById(R.id.typewriter);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/Buried Bones.ttf");
        t.setTypeface(custom);
        t.setCharacterDelay(150);
        t.animateText("Walk your life out!");

        textview = (TextView) findViewById(R.id.stepcount);

        LocationListener locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if(lastLocation == null)
                {
                    lastLocation = location;
                }
                else
                {
                    double latitude = location.getLatitude() + lastLocation.getLatitude();
                    latitude *= latitude;
                    double longitude = location.getLongitude() + lastLocation.getLongitude();
                    longitude *= longitude;
                    double altitude = location.getAltitude() + lastLocation.getAltitude();
                    altitude *= altitude;
                    distance += Math.sqrt(latitude + longitude + altitude);
                    textview.setText(Double.toString(distance));

                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationlistener);
    }


    }

