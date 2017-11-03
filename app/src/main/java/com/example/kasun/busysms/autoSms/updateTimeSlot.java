package com.example.kasun.busysms.autoSms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;

/**
 * Created by Kasun Somadasa
 * This is the activity which update existing time slot in db
 */

public class updateTimeSlot extends AppCompatActivity {

    static final int DILOG_FROM = 0;
    static final int DILOG_TO = 1;
    EditText msg, state;
    ArrayList<String> selectedItems = new ArrayList<String>();
    public String selections = "";
    Button updateBtn;
    TextView timeFromText, timeToText, repeatText;
    int noOfHour, noOfminute;
    Database_Helper db;
    AlertDialog alert;
    CheckBox checkBoxCall, checkBoxSms;
    String code, from, to, type, day, messege, call, sms, activation;
    String checkSms, checkCall,checkActive,checkTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_time_slot);

        checkBoxCall = (CheckBox) findViewById(R.id.for_call);
        checkBoxSms = (CheckBox) findViewById(R.id.for_sms);
        db = new Database_Helper(this);
        msg = (EditText) findViewById(R.id.messege);
        state = (EditText) findViewById(R.id.status);
        timeFromText = (TextView) findViewById(R.id.time_from);
        timeToText = (TextView) findViewById(R.id.time_to);
        repeatText = (TextView) findViewById(R.id.repeat);
        updateBtn = (Button) findViewById(R.id.updateTimeSlot);

        showDialogTimeFrom();
        showDialogTimeTo();
        showDialogdays();
        addListenerForCheckBox();

        repeatText.setText("Choose your days");
        UpData();
        //hold data parsed from timeSlotsList activity and set it to variables
        code = getIntent().getStringExtra("code");
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        type = getIntent().getStringExtra("type");
        day = getIntent().getStringExtra("day");
        messege = getIntent().getStringExtra("msg");
        call = getIntent().getStringExtra("call");
        sms = getIntent().getStringExtra("sms");
        activation = getIntent().getStringExtra("activation");
        timeFromText.setText(from);
        timeToText.setText(to);
        repeatText.setText(day);
        state.setText(type);
        msg.setText(messege);
        setCheck();
        //enable action bar back btn
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
public boolean checkIconShow(){
    /*
     * check is notification show on notification bar
     */
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
        /*
         * show notification with app icon on mobile notification bar
         */
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)//R.mipmap.ic_launcher-->for app icon
                .setContentTitle("Auto SMS Activated");
        Intent resultIntent = new Intent(this, smsHome.class); //when user click on notification then directly comes to smsHome activity
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


    public void showDialogTimeFrom() {
     /*
      * show time picker dialog for 'From'
      */
        timeFromText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG_FROM);
                    }
                }
        );
    }

    public void showDialogTimeTo() {
     /*
      * show time picker dialog for 'To'
      */
        timeToText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG_TO);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id) {
     /*
      * get user choosen hour and minute to variables
      */
        if (id == DILOG_FROM)
            return new TimePickerDialog(this, 2, timePikerListnerFrom, noOfHour, noOfminute, false);
        else if (id == DILOG_TO)
            return new TimePickerDialog(this, 2, timePikerListnerTo, noOfHour, noOfminute, false);
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePikerListnerFrom
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            noOfHour = hourOfDay;
            noOfminute = minute;

            timeFromText.setText(noOfHour + ":" + noOfminute + ":00");

        }


    };
    private TimePickerDialog.OnTimeSetListener timePikerListnerTo
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            noOfHour = hourOfDay;
            noOfminute = minute;

            timeToText.setText(noOfHour + ":" + noOfminute + ":00");

        }


    };

    public void showDialogdays() {
        repeatText.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selections="";
                        alert.show();
                    }
                }
        );

        final String[] items = getResources().getStringArray(R.array.my_date_choose);

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
                for (String ms : selectedItems) {
                    if(selections==""){
                        selections=ms;
                    }else{
                        selections=selections+","+ms;
                    }
                }
                //if selection is empty then display Choose your days" again
                if(selections.equals("")){
                    repeatText.setText("Choose your days");
                }else{
                    repeatText.setText(selections);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        alert = builder.create();
    }


    public void UpData() {
        /*
         * update existing time slot in db
         */
        updateBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkCall.equals("false") && checkSms.equals("false")) {
                            checkTest = "false";
                        } else {
                            checkTest = "true";
                        }

                        if (!repeatText.equals("Choose your days") && !msg.equals("") && !state.equals("") && checkTest.equals("true")) {

                            boolean isUpdated = db.updateData(code, timeFromText.getText().toString(), timeToText.getText().toString(), state.getText().toString(), repeatText.getText().toString(), msg.getText().toString(), checkCall, checkSms, checkActive);

                            if(checkIconShow()){
                                showIcon();
                            }else{
                                disapperIcon();
                            }


                            if (isUpdated == true) {
                                Toast.makeText(updateTimeSlot.this, "Changers Are Saved", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(updateTimeSlot.this, "Changers Are Not Saved", Toast.LENGTH_LONG).show();
                            }

                            Intent i = new Intent(updateTimeSlot.this, smsHome.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        } else {
                            Toast.makeText(updateTimeSlot.this, "Some Required Field Are Missing !!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void setCheck() {
        //set check box according to db values
        if (call.equals("true")) {
            checkBoxCall.setChecked(true);
            checkCall = "true";
        } else {
            checkBoxCall.setChecked(false);
            checkCall = "false";
        }
        if (sms.equals("true")) {
            checkBoxSms.setChecked(true);
            checkSms = "true";
        } else {
            checkBoxSms.setChecked(false);
            checkSms = "false";
        }
    }

    public void addListenerForCheckBox() {
        //get check box values
        checkBoxCall = (CheckBox) findViewById(R.id.for_call);
        checkBoxSms = (CheckBox) findViewById(R.id.for_sms);

        checkBoxCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is Call CheckBox checked?
                if (((CheckBox) v).isChecked()) {
                    checkCall = "true";
                } else {
                    checkCall = "false";
                }

            }
        });
        checkBoxSms.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is SMS CheckBox checked?
                if (((CheckBox) v).isChecked()) {
                    checkSms = "true";
                } else {
                    checkSms = "false";
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * set switch to action bar for detect activation on time slot
         */
        getMenuInflater().inflate(R.menu.switch_menu,menu);

        MenuItem menuItem =menu.findItem(R.id.switchView);
        menuItem.setActionView(R.layout.use_switch);
        final Switch sw= (Switch) menu.findItem(R.id.switchView).getActionView().findViewById(R.id.action_switch);


        if(activation.equals("Active")){
            sw.setChecked(true);
            checkActive="Active";
        }else {
            sw.setChecked(false);
            checkActive="Deactive";
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkActive="Active";
                    Toast.makeText(updateTimeSlot.this, "Active", Toast.LENGTH_LONG).show();
                } else {
                    checkActive="Deactive";
                    Toast.makeText(updateTimeSlot.this, "Deactive", Toast.LENGTH_LONG).show();
                }
            }
        });
        return true;
    }
}
