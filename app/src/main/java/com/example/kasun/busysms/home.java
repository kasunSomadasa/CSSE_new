package com.example.kasun.busysms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.kasun.busysms.callBlock.callBlockerHome;
import com.example.kasun.busysms.alarm.alarmHome;
import com.example.kasun.busysms.taskCalendar.TaskCalendarHomeActivity;
import com.example.kasun.busysms.autoSms.smsHome;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class home extends AppCompatActivity {

    ListView simpleListView;
    int[] animalImages={
            R.drawable.function1,
            R.drawable.function2,
            R.drawable.function3,
            R.drawable.fnew};//function images array
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

           
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        simpleListView=(ListView)findViewById(R.id.homeListView);

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();

        for (int i=0;i<4;i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("image",animalImages[i]+"");
            arrayList.add(hashMap);//add the hashmap into arrayList
        }
        String[] from={"image"};//string array
        int[] to={R.id.homeImage};//int array of views id's
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.home_item_list,from,to);//Create object and set the parameters for simpleAdapter
        simpleListView.setAdapter(simpleAdapter);//sets the adapter for listView

        checkAndRequestPermissions();

        //perform listView item click event
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position == 0){
                    //go to smsHome Activity
                    Intent intent = new Intent(home.this, smsHome.class);
                    startActivity(intent);
                }else if(position ==1){
                    //go to alarmHome Activity
                    Intent intent = new Intent(home.this,alarmHome.class);
                    startActivity(intent);
                }else if(position ==2){
                    //go to callBlockerHome Activity
                    Intent intent = new Intent(home.this,callBlockerHome.class);
                    startActivity(intent);
                }else{
                    //go to TaskCalendarHomeActivity Activity
                    Intent intent = new Intent(home.this,TaskCalendarHomeActivity.class);
                    startActivity(intent);
                }//show the selected image in toast according to position
            }
        });



    }

    //permission requestion
    private  boolean checkAndRequestPermissions() {

        int contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int smsReceive = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int fileWitePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int fileReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int readCallLogPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        int recordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int captureAudioOutput = ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (smsReceive != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (fileWitePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (fileReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (readCallLogPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }
        if(recordAudio != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (captureAudioOutput != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAPTURE_AUDIO_OUTPUT);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }


}
