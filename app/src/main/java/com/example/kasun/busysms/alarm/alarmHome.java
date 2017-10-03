package com.example.kasun.busysms.alarm;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kasun.busysms.R;

public class alarmHome extends AppCompatActivity {
    private static Button btnAddTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_home);

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
