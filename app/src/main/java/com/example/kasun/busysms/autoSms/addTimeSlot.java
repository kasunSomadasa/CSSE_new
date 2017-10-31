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

    static final int DILOGFROM=0;
    static final int DILOGTO=1;
    EditText msg,state;
    ArrayList<String> selectedItems=new ArrayList<String>();
    public String selections;
    Button saveBtn;
    TextView fromTimeText,toTimeText,displayText;
    int noOfHour,noOfminute;
    Database_Helper db;
    AlertDialog alert;
    CheckBox checkBoxCall,checkBoxSms;
    String checkSms="false",checkCall="false",testCheck;

    Calendar now = Calendar.getInstance();


    int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_slot);

        db = new Database_Helper(this);
        msg=(EditText)findViewById(R.id.messege);
        state=(EditText)findViewById(R.id.status);
        fromTimeText=(TextView)findViewById(R.id.time_from);
        toTimeText=(TextView) findViewById(R.id.time_to);
        displayText=(TextView) findViewById(R.id.repeat);
        saveBtn=(Button) findViewById(R.id.addTimeSlot);
        fromTimeText.setText(hour + ":" + minute+":"+second);
        toTimeText.setText(hour + ":" + minute+":"+second);
        displayText.setText("Choose your days");
        showDialogTimeFrom();
        showDialogTimeTo();
        showDialogdays();
        addData();
        addListenerForCheckBox();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


public void showDialogTimeFrom(){
    fromTimeText.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DILOGFROM);
                }
            }
    );
}
    public void showDialogTimeTo(){
        toTimeText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOGTO);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DILOGFROM )
            return new TimePickerDialog(this,2,timePikerListnerFrom,noOfHour,noOfminute,false);
        else if(id==DILOGTO)
            return new TimePickerDialog(this,2,timePikerListnerTo,noOfHour,noOfminute,false);
        return  null;
    }

private  TimePickerDialog.OnTimeSetListener timePikerListnerFrom
        =new TimePickerDialog.OnTimeSetListener(){
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        noOfHour=hourOfDay;
        noOfminute=minute;

        fromTimeText.setText(noOfHour+":"+noOfminute+":00");
    }


};
    private  TimePickerDialog.OnTimeSetListener timePikerListnerTo
            =new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            noOfHour=hourOfDay;
            noOfminute=minute;

            toTimeText.setText(noOfHour+":"+noOfminute+":00");
        }


    };

    public void showDialogdays() {
        displayText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selections="";
                        alert.show();
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
                                    selectedItems.add(items[which]);
                                } else if (selectedItems.contains(items[which])) {
                                    // Else, if the item is already in the array, remove it
                                   selectedItems.remove(items[which]);
                                }
                            }
                        });
                // Set the action buttons
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        selections="";
                        for (String ms:selectedItems) {
                            if(selections==""){
                                selections=ms;
                            }else{
                                selections=selections+","+ms;
                            }

                        }
                     //   Toast.makeText(addTimeSlot.this,selections, Toast.LENGTH_LONG).show();
                        if(selections.equals("")){
                            displayText.setText("Choose your days");
                        }else{
                            displayText.setText(selections);
                        }

}
                });
        builder .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        alert =  builder.create();

    }

    public void addData() {
        saveBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if (checkCall.equals("false") && checkSms.equals("false")) {
                          testCheck = "false";
                        } else {
                          testCheck = "true";
                        }

                       if (!displayText.getText().toString().equals("Choose your days") && !msg.getText().toString().equals("") && !state.getText().toString().equals("") && testCheck.equals("true")) {
                            boolean isInserted = db.insertData(fromTimeText.getText().toString(), toTimeText.getText().toString(), state.getText().toString(), displayText.getText().toString(), msg.getText().toString(), checkCall, checkSms, "Active");
                            if (isInserted == true) {
                                Toast.makeText(addTimeSlot.this, "Record Saved", Toast.LENGTH_LONG).show();
                                showIcon();
                            } else {
                                Toast.makeText(addTimeSlot.this, "Record Not Saved", Toast.LENGTH_LONG).show();
                            }
                            Intent i = new Intent(addTimeSlot.this, smsHome.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        } else {
                           Toast.makeText(addTimeSlot.this, "Some Required Field Are Missing !!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }




    public void addListenerForCheckBox() {

        checkBoxCall = (CheckBox) findViewById(R.id.for_call);
        checkBoxSms = (CheckBox) findViewById(R.id.for_sms);

        checkBoxCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is CheckBox checked?
                if (((CheckBox) v).isChecked()) {
                    checkCall="true";
                }else {
                    checkCall="false";
                }

            }
        });
        checkBoxSms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is CheckBox checked?
                if (((CheckBox) v).isChecked()) {
                    checkSms="true";
                }else {
                    checkSms="false";
                }

            }
        });

    }
 }

