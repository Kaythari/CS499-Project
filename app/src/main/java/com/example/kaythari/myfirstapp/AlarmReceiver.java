package com.example.kaythari.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kaythari on 4/23/2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        //get the indicating string
        String getString = intent.getExtras().getString("extra");
        Intent serviceIntent = new Intent(context,AlarmRingtone.class);

        //relay string from main to ringtone
        serviceIntent.putExtra("extra", getString);
        context.startService(serviceIntent);
    }
}
