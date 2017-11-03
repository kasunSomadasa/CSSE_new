package com.example.kasun.busysms.autoSms;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

/**
 * Created by Kasun Somadasa
 * This is home activity of auto sms funtion
 * Using this activity navigate to other activitys
 */

public class smsHome extends AppCompatActivity {

    static final String TAG ="INFO_SMS_HOME";
    /** Constant for permission request */
    private static final int PERMISSION_REQUEST_CODE =123;
    //Databese referance
    Database_Helper db;
    ListView homeLogList;
    Button newBtn,logBtn,soundBtn;
    TextView dbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_home);

        //Before going to activitys check api 23 runtime permissions
        if(!hasPermissions()){
            //if not then request runtime permissions
            requestPermission();
        }

        dbStatus=(TextView)findViewById(R.id.displayText);
        dbStatus.setVisibility(View.INVISIBLE);
        homeLogList = (ListView) findViewById(R.id.main_list);
        db = new Database_Helper(this);
        db.open();
        populatelistView();
        homeLogList.setOnItemClickListener(onItemClickListener);

        //btn click methods
        onClickButtonListenerForLog();
        onClickButtonListenerForNewTimeSlot();
        onClickButtonListenerForControlSound();
        changeVolumeMode();

        //enable action bar back btn and other action btns
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        //navigate to activity according to item click
        if (id == R.id.add_btn) {
            Intent intent = new Intent(smsHome.this,addTimeSlot.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.log_btn){
            Intent intent = new Intent(smsHome.this,timeSlotsList.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickButtonListenerForLog(){
        /*
         * open addtime slot activity
         */
        newBtn = (Button) findViewById(R.id.newButton);
        newBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(smsHome.this,addTimeSlot.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
        );

    }
    public void onClickButtonListenerForNewTimeSlot() {
         /*
         * open log display activity
         */
        logBtn = (Button) findViewById(R.id.logButton);
        logBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(smsHome.this,timeSlotsList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }
        );

    }

    public void changeVolumeMode(){
        /*
         * check current mobile audio status and change btn according to it
         */
        AudioManager audioManager =(AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);

        switch( audioManager.getRingerMode() ){

            case AudioManager.RINGER_MODE_NORMAL:
                soundBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_vibration_white_24dp, 0, 0, 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                soundBtn.setText("VIBRATE");
                break;
            case AudioManager.RINGER_MODE_SILENT:
                soundBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_volume_up_white_24dp, 0, 0, 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                soundBtn.setText("NORMAL");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                soundBtn.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_volume_off_white_24dp, 0, 0, 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                soundBtn.setText("SILENT");
                break;
        }
    }

    public void onClickButtonListenerForControlSound() {
         /*
         * change mobile audio status
         */
        soundBtn = (Button) findViewById(R.id.silentButton);
        soundBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeVolumeMode();
                    }
                }
        );

    }

    public void populatelistView() {
         /*
         * load database data to listView
         */
        Cursor cursor = db.getListOfData();

        //get only from,to,day and activation data from db
        String[] fromFiledNames = new String[]{Database_Helper.COL2, Database_Helper.COL3, Database_Helper.COL9,Database_Helper.COL5,Database_Helper.COL4};
        int[] toViewIds = new int[]{R.id.from, R.id.to ,R.id.activate,R.id.day,R.id.state};

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.time_slot_item_list, cursor, fromFiledNames, toViewIds, 0);

        homeLogList.setAdapter(simpleCursorAdapter);
        if (cursor == null) {

            return;
        }
        if (cursor.getCount() == 0) {
            // if data is not available
            dbStatus.setVisibility(View.VISIBLE);
            dbStatus.setText("No Any SMS Record To Display");
            Log.i(TAG,"No Any SMS Record To Display ");
            return;
        }

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        /*
        * give direction when user click on listview item
        */
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Snackbar.make(view, "Please go to the LOG !!!", Snackbar.LENGTH_LONG)
                    .setAction("OK", null).show();
        }
    };

    private  boolean hasPermissions(){
         /*
         * checking runtime permissions are available or not
         * and return boolean
         */
        int res=0;
        String[] permissions = new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
        };

        for (String permission : permissions){
            res = checkCallingOrSelfPermission(permission);
            if(!(res == PackageManager.PERMISSION_GRANTED)){
                return  false;
            }
        }
        return  true;
    }

    private  void  requestPermission(){
         /*
         * If mobile api level 23 or above versions and
         * if permission is not available request that permissions form user at runtime
         */
        String[] permissions = new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         /*
         * check wheather user accept our permission request
         * if not display error toast
         */

        boolean allow =true;

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                for(int res:grantResults){
                    allow = allow && (res==PackageManager.PERMISSION_GRANTED);

                }
                break;
            default:
                allow = false;
                break;
        }

        //display error toast
        if(!allow){
             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                 if(shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)){
                     Toast.makeText(this,"Permissions Denied.Your app is not working correctly",Toast.LENGTH_LONG).show();
                 }
             }
        }
    }




}
