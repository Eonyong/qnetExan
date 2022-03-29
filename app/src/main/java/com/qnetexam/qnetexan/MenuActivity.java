package com.qnetexam.qnetexan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    TextView Hours = findViewById(R.id.check_hour),
            Minutes = findViewById(R.id.check_min),
            amPmText = findViewById(R.id.check_ampm);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        @SuppressLint("UnspecifiedImmutableFlag") final PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        //timePick
        final TimePicker timePicker = findViewById(R.id.timePick);
        timePicker.setIs24HourView(true);

        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        final Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(millis);

        Date currentTime = nextNotifyTime.getTime();
        final SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat minFormat = new SimpleDateFormat("mm", Locale.getDefault());

        final int hour = Integer.parseInt(hourFormat.format(currentTime));
        int minute = Integer.parseInt(minFormat.format(currentTime));

        timePicker.setHour(hour);
        timePicker.setMinute(minute);


        //Button
        final Button button = findViewById(R.id.timeAddButton);
        final Calendar calendar = Calendar.getInstance();

        timePicker.setEnabled(false);
        button.setEnabled(false);

        //toggle

        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch clickButton = findViewById(R.id.clickButton);
        final SharedPreferences clickState = getSharedPreferences("clickButton", MODE_PRIVATE);
        final SharedPreferences.Editor editor = clickState.edit();

        if (clickState.getBoolean("clickable", true)){
            clickButton.setChecked(true);
            timePicker.setEnabled(true);
            button.setEnabled(true);
        }
        else{
            clickButton.setChecked(false);
            timePicker.setEnabled(false);
            button.setEnabled(false);
        }

        clickButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!clickButton.isChecked()) {
                    timePicker.setEnabled(false);
                    button.setEnabled(false);
                    editor.putBoolean("clickable", false);
                    pendingIntent.cancel();

                } else {
                    timePicker.setEnabled(true);
                    button.setEnabled(true);
                    editor.putBoolean("clickable", true);
                }
                editor.apply();
            }
        });


        button.setOnClickListener(v -> {

            int setHour, setMinute, allHour;

            String amOrPm;

            allHour = timePicker.getHour();
            setMinute = timePicker.getMinute();

            //현재시간으로 지정
            calendar.set(Calendar.HOUR_OF_DAY, allHour);
            calendar.set(Calendar.MINUTE, setMinute);
            calendar.set(Calendar.SECOND, 0);
            diaryNotification(calendar);

            if (allHour > 12){
                amOrPm = "PM";
                setHour = allHour - 12;
            }
            else{
                amOrPm = "AM";
                setHour = allHour;
            }

            SharedPreferences.Editor editor1 = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor1.putLong("nextNotifyTime", calendar.getTimeInMillis());
            editor1.apply();


            amPmText.setText(amOrPm);
            Hours.setText(String.valueOf(setHour));
            Minutes.setText(String.valueOf(setMinute));

        });


    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void diaryNotification(Calendar calendar) {

        boolean dailyNotify;


        PackageManager packageManager = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch clickButton = findViewById(R.id.clickButton);

        dailyNotify = clickButton.isChecked();

        if (dailyNotify){

            if (alarmManager != null){

                PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            }

            packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        }

        else{
            if (PendingIntent.getBroadcast(this, 0, alarmIntent, 0) != null && alarmManager != null)
                alarmManager.cancel(pendingIntent);
            packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

    }

}
