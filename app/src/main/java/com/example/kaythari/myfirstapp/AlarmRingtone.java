package com.example.kaythari.myfirstapp;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Kaythari on 4/11/2017.
 */

public class AlarmRingtone extends Service  {

    MediaPlayer alarmRingtone;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Receive start id " + startId + ": " + intent);
        alarmRingtone = MediaPlayer.create(this, R.raw.dope);
        alarmRingtone.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "On destroy called", Toast.LENGTH_SHORT).show();
    }
}
