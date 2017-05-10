package com.example.kaythari.myfirstapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    ToggleButton toggleButton;
    AlarmManager alarmManager;
    TimePicker timePicker;
    TextView updateAlarm;
    Context context;
    PendingIntent pendingIntent;

//    //detect if home button is pressed
//    protected void onUserLeaveHint()
//    {
//        Toast.makeText(MainActivity.this, "Don't you dare leave!", Toast.LENGTH_LONG).show();
//        super.onUserLeaveHint();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.alarmclock);

        //request permission for write setting for custom ringtone
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        //initialize
        toggleButton = (ToggleButton) findViewById(R.id.alarmOnOff);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        updateAlarm = (TextView) findViewById(R.id.updateAlarm);
        this.context = this;

        //custom ringtone
        Button button = (Button) findViewById(R.id.ringtone);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Intent ringtone = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select alarm ringtone");
                ringtone.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, (Uri) null);
                startActivityForResult(ringtone, 0);
            }
        });

        //custom your own music
        Button button2 = (Button) findViewById(R.id.music);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
                picker.setType("audio/mpeg");
                Intent intent = Intent.createChooser(picker, "Select your music");
                startActivityForResult(intent, 1);
            }
        });

        final Calendar calender = Calendar.getInstance();
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
                        s_hour = String.valueOf(hour - 12);
                    if(hour == 0)
                        s_hour = "12";
                    if(minute < 10)
                        s_min = "0" + String.valueOf(minute);

                    String AM_PM = (hour < 12) ? "AM" : "PM";
                    update_Alarm("Alarm set to " + s_hour + ":" + s_min + AM_PM + ".");

                    //indicate clock on is pressed
                    alarmIntent.putExtra("extra", "on");

                    //pending intent that delays intent to specified time
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    //set alarm manager
                    alarmManager.set(alarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pendingIntent);
                }
                else
                {
                    update_Alarm("Alarm off");
                    alarmManager.cancel(pendingIntent); //cancel alarm
                    //tell clock off is pressed
                    alarmIntent.putExtra("extra", "off");
                    sendBroadcast(alarmIntent);
                }
            }
        });
    }
    private void update_Alarm(String s) {
        updateAlarm.setText(s);
    }

    //part of custom ringtone
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            final Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if(uri != null) {
                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, uri);
            }
        } else if(requestCode == 1 && resultCode == RESULT_OK) {
            if(data.getData() != null) {
                Toast.makeText(this, "Music selected", Toast.LENGTH_SHORT).show();
                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, data.getData());
            }
        }
    }
}

