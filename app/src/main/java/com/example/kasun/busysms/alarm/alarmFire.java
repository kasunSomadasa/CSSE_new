package com.example.kasun.busysms.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.home;

public class alarmFire extends AppCompatActivity {

    AlarmManager alarm_Manager;


    public void cancelAlarmNotify() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_fire);

        ImageButton alarmOffImg = (ImageButton) findViewById(R.id.imageButton_AlarmOff);

        alarmOffImg.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                alarm_Manager.cancel(pending_intent);


                android.os.Process.killProcess(android.os.Process.myPid());
//               cancel the activity
//                System.exit(0);
                cancelAlarmNotify();

                finishAffinity();
               // finish();
    //
            }
        });
    }


}
