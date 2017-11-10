package com.example.kasun.busysms.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.kasun.busysms.R;

public class AlarmFire extends AppCompatActivity {

    AlarmManager alarm_Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_fire);

        ImageButton alarmOffImg = (ImageButton) findViewById(R.id.imageButton_AlarmOff);

        alarmOffImg.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();

                String get_silent_state = getIntent().getExtras().getString("silentExtra");
                Log.e("fire in intent ",get_silent_state);

                android.os.Process.killProcess(android.os.Process.myPid());
                finishAffinity();
//                finish();
            }
        });
    }


}
