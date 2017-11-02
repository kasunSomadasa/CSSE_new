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
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;
import com.google.android.gms.common.api.GoogleApiClient;

public class timeSlotsList extends AppCompatActivity {
    Database_Helper db;
    ListView dataList;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot_list);
        dataList = (ListView) findViewById(R.id.list);
        db = new Database_Helper(this);
        db.open();
        populateListView();

        dataList.setOnItemClickListener(onItemClickListener);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {

                final Cursor cursor = (Cursor) dataList.getItemAtPosition(position);

                new AlertDialog.Builder(timeSlotsList.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                String code1 = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                                Integer value=db.deleteData(code1);

                                if(checkIconShow()){
                                    showIcon();
                                }else{
                                    disapperIcon();
                                }

                                if(value>0){
                                    Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                                    populateListView();
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
        Cursor c = db.getData();
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
        Intent resultIntent = new Intent(this, smsHome.class);
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



    public void populateListView() {

        Cursor cursor = db.getListOfData();

        String[] fromFiledNames = new String[]{Database_Helper.COL2, Database_Helper.COL3, Database_Helper.COL9,Database_Helper.COL5};
        int[] toViewIds = new int[]{R.id.from, R.id.to ,R.id.activate,R.id.day};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.time_slot_item_list, cursor, fromFiledNames, toViewIds, 0);

        dataList.setAdapter(simpleCursorAdapter);
        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Any SMS Record To Display", Toast.LENGTH_LONG).show();
            return;
        }

    }


    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Cursor cursor = (Cursor) dataList.getItemAtPosition(position);

            String code = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
            String table_from = cursor.getString(cursor.getColumnIndexOrThrow("TIME_FROM"));
            String table_to = cursor.getString(cursor.getColumnIndexOrThrow("TIME_TO"));
            String table_type = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
            String table_day = cursor.getString(cursor.getColumnIndexOrThrow("DAY"));
            String table_msg = cursor.getString(cursor.getColumnIndexOrThrow("MSG"));
            String table_call = cursor.getString(cursor.getColumnIndexOrThrow("CALL_T"));
            String table_sms = cursor.getString(cursor.getColumnIndexOrThrow("SMS_T"));
            String table_activation = cursor.getString(cursor.getColumnIndexOrThrow("ACTIVATION"));

            Intent i = new Intent(timeSlotsList.this, updateTimeSlot.class);
            i.putExtra("code", String.valueOf(code));
            i.putExtra("from", String.valueOf(table_from));
            i.putExtra("to", String.valueOf(table_to));
            i.putExtra("type", String.valueOf(table_type));
            i.putExtra("day", String.valueOf(table_day));
            i.putExtra("msg", String.valueOf(table_msg));
            i.putExtra("call", String.valueOf(table_call));
            i.putExtra("sms", String.valueOf(table_sms));
            i.putExtra("activation", String.valueOf(table_activation));
            startActivity(i);


    }
    };


    public void populatelistViewSearch(String key) {

        Cursor cursor = db.searchData(key);

        String[] fromFileDnames = new String[]{Database_Helper.COL2, Database_Helper.COL3, Database_Helper.COL9,Database_Helper.COL5};
        int[] toViewIds = new int[]{R.id.from, R.id.to ,R.id.activate,R.id.day};

        simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.time_slot_item_list, cursor, fromFileDnames, toViewIds, 0);

        dataList.setAdapter(simpleCursorAdapter);
        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Any SMS Record To Display", Toast.LENGTH_LONG).show();
            return;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){


            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                populatelistViewSearch(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
