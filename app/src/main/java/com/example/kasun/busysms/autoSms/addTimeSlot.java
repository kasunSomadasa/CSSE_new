package com.example.kasun.busysms.autoSms;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.Calendar;

public class addTimeSlot extends AppCompatActivity {
    EditText e1,e2;
    ArrayList<String> mSelectedItems=new ArrayList<String>();
    public String selections;
    Button btn;
    TextView tx1,tx2,tx3;
    static final int DILOG1=0;
    static final int DILOG2=1;
    int hour1,minte;
    Database_Helper mydb;
    AlertDialog ad;
    CheckBox c_call,c_sms;
    String checksms="false",checkcall="false",test_check;

    Calendar now = Calendar.getInstance();


    int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_slot);

        mydb = new Database_Helper(this);
        e1=(EditText)findViewById(R.id.messege);
        e2=(EditText)findViewById(R.id.status);
        tx1=(TextView)findViewById(R.id.time_from);
        tx2=(TextView) findViewById(R.id.time_to);
        tx3=(TextView) findViewById(R.id.repeat);
        btn=(Button) findViewById(R.id.addTimeSlot);
        showDialogTime1();
        showDialogTime2();
        showDialogdays();
        tx1.setText(hour + ":" + minute+":"+second);
        tx2.setText(hour + ":" + minute+":"+second);
        tx3.setText("Choose your days");
        addData();
        addListenerOnChkIos();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


public void showDialogTime1(){
    tx1.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DILOG1);
                }
            }
    );
}
    public void showDialogTime2(){
        tx2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG2);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DILOG1 )
            return new TimePickerDialog(this,2,tpikerListner1,hour1,minte,false);
        else if(id==DILOG2)
            return new TimePickerDialog(this,2,tpikerListner2,hour1,minte,false);
        return  null;
    }

private  TimePickerDialog.OnTimeSetListener tpikerListner1
        =new TimePickerDialog.OnTimeSetListener(){
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
      hour1=hourOfDay;
        minte=minute;

        tx1.setText(hour1+":"+minte+":00");
      //  Toast.makeText(addTimeSlot.this,hour1+":"+minte+":00", Toast.LENGTH_LONG).show();
    }


};
    private  TimePickerDialog.OnTimeSetListener tpikerListner2
            =new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour1=hourOfDay;
            minte=minute;

            tx2.setText(hour1+":"+minte+":00");
         //   Toast.makeText(addTimeSlot.this,hour1+":"+minte+":00", Toast.LENGTH_LONG).show();
        }


    };

    public void showDialogdays() {
        tx3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selections="";
                        ad.show();
                }
                }
        );

        final  String[] items=getResources().getStringArray(R.array.my_date_choose);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle("Choose your days");
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
        builder.setMultiChoiceItems(R.array.my_date_choose, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(items[which]);
                                } else if (mSelectedItems.contains(items[which])) {
                                    // Else, if the item is already in the array, remove it
                                   mSelectedItems.remove(items[which]);
                                }
                            }
                        });
                // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selections="";
                        for (String ms:mSelectedItems) {
                            if(selections==""){
                                selections=ms;
                            }else{
                                selections=selections+","+ms;
                            }

                        }
                     //   Toast.makeText(addTimeSlot.this,selections, Toast.LENGTH_LONG).show();
                        if(selections.equals("")){
                            tx3.setText("Choose your days");
                        }else{
                            tx3.setText(selections);
                        }

}
                });
        builder .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

      ad =  builder.create();

    }

    public void addData() {
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if (checkcall.equals("false") && checksms.equals("false")) {
                            test_check = "false";
                        } else {
                            test_check = "true";
                        }

                       if (!tx3.getText().toString().equals("Choose your days") && !e1.getText().toString().equals("") && !e2.getText().toString().equals("") && test_check.equals("true")) {
                            boolean isInserted = mydb.insertData(tx1.getText().toString(), tx2.getText().toString(), e2.getText().toString(), tx3.getText().toString(), e1.getText().toString(), checkcall, checksms, "Active");
                            if (isInserted == true) {
                                Toast.makeText(addTimeSlot.this, "Record Saved", Toast.LENGTH_LONG).show();
                                showIcon();
                            } else {
                                Toast.makeText(addTimeSlot.this, "Record Not Saved", Toast.LENGTH_LONG).show();
                            }
                            Intent i = new Intent(addTimeSlot.this, autoSmsHome.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                           Toast.makeText(addTimeSlot.this, "Some Required Field Are Missing !!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }




    public void addListenerOnChkIos() {

        c_call = (CheckBox) findViewById(R.id.for_call);
        c_sms = (CheckBox) findViewById(R.id.for_sms);

        c_call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    checkcall="true";
                }else {
                    checkcall="false";
                }

            }
        });
        c_sms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    checksms="true";
                }else {
                    checksms="false";
                }

            }
        });

    }
 }

