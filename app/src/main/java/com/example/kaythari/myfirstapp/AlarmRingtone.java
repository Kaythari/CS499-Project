package com.example.kaythari.myfirstapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
            //custom ringtone
            Uri notification1 = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
            alarmRingtone = MediaPlayer.create(this, notification1);
            alarmRingtone.start();
            alarmRingtone.setLooping(true);

            //when alarm triggers, screen change to next page
            Intent intent2 = new Intent(this, TouchToNext.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent2);
            this.isPlaying = true;
            this.id = 0;

            //app notification service
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent nfIntent = new Intent(this.getApplicationContext(), TouchToNext.class);
            nfIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pdI = PendingIntent.getActivity(this, 0, nfIntent, 0);

            //notification popping up
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Wake up, you mortal!")
                    .setContentText("How are you still asleep?")
                    .setContentIntent(pdI)
                    .setSmallIcon(R.drawable.notif_icon)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 }) //added
                    .setDefaults(Notification.FLAG_SHOW_LIGHTS)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .build();
            nm.notify(0, notification);

            //lock screen wake up
            PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if(isScreenOn==false)
            {
                PowerManager.WakeLock wake1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MyLock");
                wake1.acquire(10000);
                PowerManager.WakeLock wake = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyCpuLock");
                wake.acquire(10000);
            }
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
