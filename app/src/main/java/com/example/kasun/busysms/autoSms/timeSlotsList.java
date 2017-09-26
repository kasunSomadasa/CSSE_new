package com.example.kasun.busysms.autoSms;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;
import com.google.android.gms.common.api.GoogleApiClient;

public class timeSlotsList extends AppCompatActivity {
    Database_Helper mydb;
    ListView mylist1;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_list);
        mylist1 = (ListView) findViewById(R.id.list1);
        mydb = new Database_Helper(this);
        mydb.open();
        populatelistView1();
        mylist1.setOnItemClickListener(onItemClickListener);
       //registerForContextMenu(mylist1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

  mylist1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {



                final Cursor cursor = (Cursor) mylist1.getItemAtPosition(position);

                new AlertDialog.Builder(timeSlotsList.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String code1 = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                                Integer value=mydb.DeleteData(code1);

                                if(checkIconShow()){
                                    showIcon();
                                }else{
                                    disapperIcon();
                                }

                                if(value>0){
                                    Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                                    populatelistView1();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                  return true;
            }

        });


    }

    public boolean checkIconShow(){
        Cursor c = mydb.getData();
        boolean test=false;

        if (c.getCount() == 0) {

        } else {
            while (c.moveToNext()) {
                String act_t = c.getString(8);

                if(act_t.equals("Active")){
                    test= true;
                    break;
                }
            }
        }

        return  test;
    }
    public void showIcon(){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)//R.mipmap.ic_launcher-->for app icon
                .setContentTitle("Busy SMS Activated");
        Intent resultIntent = new Intent(this, addTimeSlot.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(1, notification);
    }
    public void disapperIcon(){
        ((NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
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
            Toast.makeText(this, "No Any SMS Record To Display", Toast.LENGTH_LONG).show();

            //  wordtest1.setText("zero");
            return;
        }

    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor cursor = (Cursor) mylist1.getItemAtPosition(position);
            String code = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String t_from = cursor.getString(cursor.getColumnIndexOrThrow("TIME_FROM"));
            String t_to = cursor.getString(cursor.getColumnIndexOrThrow("TIME_TO"));
            String t_type = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
            String t_day = cursor.getString(cursor.getColumnIndexOrThrow("DAY"));
            String t_msg = cursor.getString(cursor.getColumnIndexOrThrow("MSG"));
            String t_call = cursor.getString(cursor.getColumnIndexOrThrow("CALL_T"));
            String t_sms = cursor.getString(cursor.getColumnIndexOrThrow("SMS_T"));
            String t_activation = cursor.getString(cursor.getColumnIndexOrThrow("ACTIVATION"));
           // Toast.makeText(getApplicationContext(), code, Toast.LENGTH_LONG).show();


            Intent i = new Intent(timeSlotsList.this, updateTimeSlot.class);
            i.putExtra("txt1", String.valueOf(code));
            i.putExtra("txt2", String.valueOf(t_from));
            i.putExtra("txt3", String.valueOf(t_to));
            i.putExtra("txt4", String.valueOf(t_type));
            i.putExtra("txt5", String.valueOf(t_day));
            i.putExtra("txt6", String.valueOf(t_msg));
            i.putExtra("txt7", String.valueOf(t_call));
            i.putExtra("txt8", String.valueOf(t_sms));
            i.putExtra("txt9", String.valueOf(t_activation));
            startActivity(i);


    }
    };

/*
  @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete Entry");//groupId, itemId, order, title
        //menu.add(0, v.getId(), 0, "Edit Entry");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete Entry") {

            //  }
            // else if(item.getTitle()=="Edit Entry"){
        } else {
            return false;
        }
        return true;
    }
*/

}
