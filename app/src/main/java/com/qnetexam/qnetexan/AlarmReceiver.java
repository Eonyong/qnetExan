package com.qnetexam.qnetexan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, CardContainer.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setSmallIcon(R.drawable.ic_launcher_background);

            int importance = NotificationManager.IMPORTANCE_HIGH;

            String channelName ="매일 알람 채널";
            String description = "매일 정해진 시간에 알람합니다.";

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
            else{
                builder.setSmallIcon(R.mipmap.ic_launcher);
            }
        }

        builder.setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL).setWhen(System.currentTimeMillis()).setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("문제가 도착했습니다.").setContentText("탭하여 어플 실행하기").setContentInfo("Info").setContentIntent(pendingIntent);

        if (notificationManager != null){
            notificationManager.notify(1234, builder.build());

            Calendar nextNotifyTime = Calendar.getInstance();

            nextNotifyTime.add(Calendar.DATE, 1);

            SharedPreferences.Editor editor = context.getSharedPreferences("daily alarm", Context.MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", nextNotifyTime.getTimeInMillis());
            editor.apply();

            Date currentDate = nextNotifyTime.getTime();
        }

    }

}
