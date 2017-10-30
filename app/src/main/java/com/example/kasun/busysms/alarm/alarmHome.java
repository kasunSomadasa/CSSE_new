package com.example.kasun.busysms.alarm;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.kasun.busysms.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class alarmHome extends AppCompatActivity {
    private static Button btnAddTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_home);

        TextClock tclock1 = (TextClock) findViewById(R.id.textClock1);
        TextClock tclock2 = (TextClock) findViewById(R.id.textClock2);
        TextView dateView = (TextView) findViewById(R.id.textday);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd");
        String dateString = sdf.format(date);
        dateView.setText(dateString);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        OnClickBtnListner();
    }


    public  void OnClickBtnListner(){
        btnAddTime = (Button) findViewById(R.id.AddTimeBtn);
        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.kasun.busysms.alarm.setAlarm");
                startActivity(intent);
            }
        });
    }
}
