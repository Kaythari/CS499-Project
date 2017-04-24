package com.example.kaythari.myfirstapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Kaythari on 4/23/2017.
 */

public class AlarmRingtone extends Service  {

    MediaPlayer alarmRingtone;
    int id;
    boolean isPlaying;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //retrieve string value
        String status = intent.getExtras().getString("extra");

        //converting indicating string value to on/off states
        assert status != null;
        switch (status) {
            case "on":
                id = 1;
                break;
            case "off":
                id = 0;
                break;
            default:
                id = 0;
                break;
        }

        //no music playing, user pressed on, music plays
        if(!this.isPlaying && id == 1)
        {
            alarmRingtone = MediaPlayer.create(this, R.raw.dope);
            alarmRingtone.start();
            this.isPlaying = true;
            this.id = 0;
        }
        //music playing, user pressed off, music stops
        else if(this.isPlaying && id == 0)
        {
            alarmRingtone.stop();
            alarmRingtone.reset();
            this.isPlaying = false;
            this.id = 0;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isPlaying = false;
    }
}
