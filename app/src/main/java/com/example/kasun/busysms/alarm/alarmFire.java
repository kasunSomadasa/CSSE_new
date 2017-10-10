package com.example.kasun.busysms.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_fire);

        ImageButton alarmOffImg = (ImageButton) findViewById(R.id.imageButton_AlarmOff);

        alarm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Intent Alarm_intent = new Intent(this,alarmReceiver.class);


        alarmOffImg.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
//                Toast.makeText(alarmFire.this,"Alarm Off!!!!",Toast.LENGTH_SHORT).show();
//                Alarm_intent.putExtra("extra","off");
//                sendBroadcast(Alarm_intent);

                Intent intent = new Intent(alarmFire.this,home.class);
                startActivity(intent);

                android.os.Process.killProcess(android.os.Process.myPid());
//               cancel the activity
//                System.exit(0);
                finishAffinity();
               // finish();
    //
            }
        });
    }
}
