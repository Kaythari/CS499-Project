package com.example.kaythari.myfirstapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton;
    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView updateAlarm;
    Context context;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarmclock);

        //initialize
        toggleButton = (ToggleButton) findViewById(R.id.alarmOnOff);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        updateAlarm = (TextView) findViewById(R.id.updateAlarm);
        this.context = this;

        //create instance of calender
        final Calendar calender = Calendar.getInstance();

        //create intent to AlarmReceiver Class
        final Intent alarmIntent = new Intent(this.context, AlarmReceiver.class);


        //Alarm on and off listener
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleButton.isChecked())
                {
                    calender.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calender.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                    int hour = timePicker.getCurrentHour();
                    int minute = timePicker.getCurrentMinute();
                    //convert to string
                    String s_hour = String.valueOf(hour);
                    String s_min = String.valueOf(minute);
                    //convert hours
                    if(hour > 12)
                    {
                       s_hour = String.valueOf(hour-12);
                    }
                    if(hour == 0)
                    {
                        s_hour = "12";
                    }
                    if(minute < 10)
                    {
                        s_min = "0" + String.valueOf(minute);
                    }

                    //setting AM or PM
                    String AM_PM;
                    if(hour < 12)
                    {
                        AM_PM = "AM";
                    }
                    else
                    {
                        AM_PM="PM";
                    }

                    update_Alarm("Alarm set to " + s_hour + ":" + s_min + AM_PM);

                    //pending intent that delays intent to specified time
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    //set alarm manager
                    alarmManager.set(alarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                }
                else
                {
                    update_Alarm("Alarm off");
                    alarmManager.cancel(pendingIntent); //cancel alarm
                    //off button doesn't work yet...
                }
            }
        });
    }
    private void update_Alarm(String s) {
      updateAlarm.setText(s);
    }
}

