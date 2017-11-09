package com.example.kasun.busysms.alarm;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.text.SimpleDateFormat;

public class alarmHome extends AppCompatActivity {
    Database_Helper alarmDB;
    ListView alarmListView;
    private static Button btnAddTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        alarmListView = (ListView) findViewById(R.id.alarmList);
        alarmDB = new Database_Helper(this);
        alarmDB.open();
        populateAlarmView();

        btnAddTime = (Button) findViewById(R.id.AddTimeBtn);
        TextClock tclock1 = (TextClock) findViewById(R.id.textClock1);
        TextClock tclock2 = (TextClock) findViewById(R.id.textClock2);
        TextView dateView = (TextView) findViewById(R.id.textday);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd");
        String dateString = sdf.format(date);
        dateView.setText(dateString);

        OnClickBtnListner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.alarm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_alarm_btn) {
            Intent intent = new Intent(alarmHome.this,setAlarm.class);
            startActivity(intent);
            return true;

        } else if(id == R.id.delete_alarm_btn){
            viewALarmData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public  void OnClickBtnListner(){

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.kasun.busysms.alarm.setAlarm");
                startActivity(intent);
            }
        });
    }

//    display alar data
    public void populateAlarmView(){
        Cursor cursor = alarmDB.getAlarmData();

        String[] from_field_name = new String[] {Database_Helper.ALARM_TIME_COL, Database_Helper.ALARM_REPEAT_COL};
        int[] to_View_ID= new int[]{R.id.textView18, R.id.textView19};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.alarm_item_list, cursor, from_field_name, to_View_ID, 0);
        alarmListView.setAdapter(simpleCursorAdapter);
    }

    public void viewALarmData(){
        Cursor cursor = alarmDB.getAlarmData();
        if (cursor.getCount() == 0) {
//            Toast.makeText(this, "No Any Record To Display ", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer bufferAlarmData = new StringBuffer();
        while (cursor.moveToNext()){
            bufferAlarmData.append(cursor.getString(1)+"\n");
            bufferAlarmData.append(cursor.getString(2)+"\n");
            bufferAlarmData.append("\n");
        }

        showAlarmDataMsg("            Alarm History",bufferAlarmData.toString());
    }

    public void showAlarmDataMsg(String title, String message){
        AlertDialog.Builder alrmBuilder = new AlertDialog.Builder(this);
        alrmBuilder.setCancelable(true);
        alrmBuilder.setTitle(title);
        alrmBuilder.setMessage(message);
        alrmBuilder.show();
    }
}
