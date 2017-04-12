package com.example.kaythari.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Kaythari on 4/10/2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context,AlarmRingtone.class);
        context.startService(serviceIntent);
    }
}
