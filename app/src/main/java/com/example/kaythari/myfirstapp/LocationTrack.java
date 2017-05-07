package com.example.kaythari.myfirstapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Kaythari on 4/29/2017.
 */

public class LocationTrack extends AppCompatActivity implements LocationListener {

    private TextView textview;
    private TextView textview2;
    private ProgressBar progressBar;
    double distanceTraveled = 0.0;
    double latitude = 0.0;
    double longitude = 0.0;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.distancetrack);

        //Custom Font
        Typewriter t = (Typewriter) findViewById(R.id.typewriter);
        Typeface custom = Typeface.createFromAsset(getAssets(), "fonts/Buried Bones.ttf");
        t.setTypeface(custom);
        t.setCharacterDelay(150);
        t.animateText("Walk your life out!");

        textview = (TextView) findViewById(R.id.stepcount);
        textview2 = (TextView) findViewById(R.id.walked);
        progressBar = (ProgressBar) findViewById(R.id.CircleBar);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double earthKm = 6371000; //in meters
        if(latitude == 0.0 && longitude == 0.0) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            //previous latitude and longitude
            double pLat = latitude;
            double pLon = longitude;

            //nw latitude and longitude
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //change in latitude and longitude
            double cLat = Math.toRadians(latitude - pLat);
            double cLon = Math.toRadians(longitude - pLon);

            //equation to calculate the distance
            double a = Math.sin(cLat/2) * Math.sin(cLat/2) +
                    Math.cos(Math.toRadians(pLat)) * Math.cos(Math.toRadians(latitude)) *
                    Math.sin(cLon/2) * Math.sin(cLon/2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            distanceTraveled += (earthKm * c);
            textview2.setText("You have walked " + Math.round(distanceTraveled) + " meter(s).");

            count = 0;
            if(earthKm * c > 0.5)
            {
                count++;
                textview.setText(Integer.toString(count));
                progressBar.setProgress(count * 10);
            }
            if(count == 10)
            {
                finish();
            }
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "GPS is turned on!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(this, "GPS is turned off!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}

