package com.example.kasun.busysms.autoSms;


import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;

import com.example.kasun.busysms.R;

public class SmsHome extends AppCompatActivity {
    Database_Helper mydb;
    ListView mylist1;

    Button btn2,btn3,btn;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_home);

        t1=(TextView)findViewById(R.id.textView9);
        t1.setVisibility(View.INVISIBLE);
        mylist1 = (ListView) findViewById(R.id.main_list);
        mydb = new Database_Helper(this);
        mydb.open();
        populatelistView1();
        mylist1.setOnItemClickListener(onItemClickListener);

        OnClickButtonListener1();
        OnClickButtonListener2();
        OnClickButtonListener3();
        changeVolumeMode();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_btn) {

            Intent intent = new Intent(SmsHome.this,addTimeSlot.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.log_btn){
            Intent intent = new Intent(SmsHome.this,timeSlotsList.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void OnClickButtonListener1() {
        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SmsHome.this,timeSlotsList.class);
                        startActivity(intent);
                    }
                }
        );

    }
    public void OnClickButtonListener2() {
        btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SmsHome.this,addTimeSlot.class);
                        startActivity(intent);
                    }
                }
        );

    }

    public void changeVolumeMode(){
        AudioManager audioManager =(AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);

        switch( audioManager.getRingerMode() ){
            case AudioManager.RINGER_MODE_NORMAL:
                btn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_vibration_white_24dp, 0, 0, 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                btn.setText("VIBRATE");
                break;
            case AudioManager.RINGER_MODE_SILENT:
                btn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_volume_up_white_24dp, 0, 0, 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                btn.setText("NORMAL");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                btn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_volume_off_white_24dp, 0, 0, 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                btn.setText("SILENT");
                break;
        }
    }

    public void OnClickButtonListener3() {
        btn = (Button) findViewById(R.id.silent_btn);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeVolumeMode();

                    }
                }
        );

    }

    public void populatelistView1() {

        //  Toast.makeText(this, "Call From:", Toast.LENGTH_LONG).show();
        Cursor cursor = mydb.getlistofData();

        // startManagingCursor(cursor);
        String[] fromfilednames = new String[]{Database_Helper.COL2, Database_Helper.COL3, Database_Helper.COL9,Database_Helper.COL5};
        int[] toviewIds = new int[]{R.id.textView3, R.id.textView12 ,R.id.textView7,R.id.textView4};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.time_slot_item_list, cursor, fromfilednames, toviewIds, 0);

        mylist1.setAdapter(simpleCursorAdapter);
        if (cursor == null) {
            // wordtest1.setText("null");
            // Toast.makeText(this, "Call From: null", Toast.LENGTH_LONG).show();
            return;
        }
        if (cursor.getCount() == 0) {
            t1.setVisibility(View.VISIBLE);
            t1.setText("No Any SMS Record To Display");
            Toast.makeText(this, "No Any SMS Record To Display ", Toast.LENGTH_LONG).show();

            //  wordtest1.setText("zero");
            return;
        }

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(SmsHome.this, "Please Go To The LOG", Toast.LENGTH_LONG).show();
        }
    };
}
