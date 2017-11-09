package com.example.kasun.busysms.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.Calendar;

public class setAlarm extends AppCompatActivity {
    private TextView update_txt,settimeTxt,setRepeatTxt,showRepeatTxt,show_timetxt;
    ArrayList<String> mSelectedDaysItems=new ArrayList<String>();
    static final int dialog_id = 0;
    public String selections;
    PendingIntent pending_intent;
    AlarmManager alarm_Manager;
    AlertDialog ad;
    Context context;
    int Cal_hour,Cal_minute,Cal_sec,Cal_day,Cal_ampm;
    String hour,min;
    int choose_ringtone;
    SeekBar sb;
    MediaPlayer mp;
    AudioManager am;
    PowerManager pm;
    Switch switch_silent;
    CheckBox checkSound;
    Database_Helper alarmDB;
    String ringtSound;
    String isSoundChecked;
    String progessVolume;
    String silentMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        this.context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ringtSound = "Select a Ringtone";
        isSoundChecked = "false";
        silentMode = "off";

        //database instance
        alarmDB = new Database_Helper(this);

        //initialze the alarm manager
        alarm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize the update text box
        update_txt = (TextView) findViewById(R.id.alarm_status);

        //create calendar instance
        final Calendar calendar = Calendar.getInstance();

        Cal_hour = calendar.get(Calendar.HOUR_OF_DAY);
        Cal_minute = calendar.get(Calendar.MINUTE);
        Cal_sec = calendar.get(Calendar.SECOND);
        Cal_ampm = calendar.get(Calendar.AM_PM);
        Cal_day = calendar.get(Calendar.DAY_OF_WEEK); //sunday =1 , saturday =7

        String ampm = "AM";
        hour = String.valueOf(Cal_hour);
        min = String.valueOf(Cal_minute);
        if(Cal_hour > 12){
            hour = String.valueOf(Cal_hour - 12);
            ampm = "PM";

        }if(Cal_minute < 10){
            min = "0"+String.valueOf(Cal_minute);
        }

        final String day = String.valueOf(Cal_day);
        String time = hour+":"+min+" "+ampm;
        Log.e("Day of the week",day);
        Log.e("Today time",time);

//        create an intent to the alam receiver class
        final Intent Alarm_intent = new Intent(this.context,alarmReceiver.class);

//        get id from time
        settimeTxt = (TextView) findViewById(R.id.AddTimeText);
        show_timetxt = (TextView) findViewById(R.id.alarm_status);
        show_timetxt.setText(time);



        //get ids of repeat days textboxes
        setRepeatTxt = (TextView) findViewById(R.id.repeat_days);
        showRepeatTxt = (TextView) findViewById(R.id.show_repeats);

//        set volume seek bar value
        sb = (SeekBar) findViewById(R.id.seekBar);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb.setMax(maxVolume);
        sb.setProgress(currVolume);

//        preview sound
        checkSound = (CheckBox) findViewById(R.id.check_sound);

//        switch button set
        switch_silent = (Switch) findViewById(R.id.silent_on_off);

//        display time picker dialog and repeat days checkbox
        showTimeDialog();
        showDialogAlarmdays();



//        switch method to enable silent mode or not
//        Default one is off
        Alarm_intent.putExtra("silentExtra","off");
        switch_silent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(setAlarm.this,"Silent mode",Toast.LENGTH_SHORT).show();
                    Alarm_intent.putExtra("silentExtra","on");
                    silentMode = "on";

                }else{
                    Alarm_intent.putExtra("silentExtra","off");
                    silentMode = "off";
                }
            }
        });

        //start alarm
        Button alarm_start = (Button) findViewById(R.id.alarm_on);

        alarm_start.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
//               Toast.makeText(setAlarm.this,day,Toast.LENGTH_SHORT).show();

                if(mSelectedDaysItems.isEmpty()){
                    Toast.makeText(setAlarm.this,"Alarm not set !!!!!\n Select the days",Toast.LENGTH_SHORT).show();
                }
                else {
                    for (String s : mSelectedDaysItems) {
                        if (s.equals("Sunday")) {
                            String sunday = String.valueOf(Calendar.SUNDAY);
                            if (sunday.equals(day)) {
//                                Toast.makeText(setAlarm.this,"Today is Sunday", Toast.LENGTH_SHORT).show();

                                //check the date is equal
                                Log.e("Today is ", "Sunday");

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);

                                //put extra string into Alarm_intent
                                //tells the clock that you pressed the 'OK' button
                                Alarm_intent.putExtra("extra", "on");

                                //put extra int into Alarm_intent
                                //tells the clock that you want to certain value from spinner
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));


                                //create a pending intent that delay the intent until the specified calendar time
                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 0,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);


                                //set the alarm manager
                                // alarm_Manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                                //               pending_intent);

                                //                            alarm_Manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                                //                                    AlarmManager.INTERVAL_DAY * 7,pending_intent);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);

                            }

                        }
                        if (s.equals("Monday")) {
                            String monday = String.valueOf(Calendar.MONDAY);
                            if (monday.equals(day)) {
                                Log.e("Today is ", "Monday");//2

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);

                                Alarm_intent.putExtra("extra", "on");
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id ", String.valueOf(choose_ringtone));

                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 1,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);


                            }
                        }
                        if (s.equals("Tuesday")) {
                            String tuesday = String.valueOf(Calendar.TUESDAY);
                            if (tuesday.equals(day)) {
                                Log.e("Today is ", "Tuesday");//3

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);

                                Alarm_intent.putExtra("extra", "on");
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));

                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 2,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);

                            }

                        }
                        if (s.equals("Wednesday")) {
                            String wednesday = String.valueOf(Calendar.WEDNESDAY);
                            if (wednesday.equals(day)) {
                                Log.e("Today is ", "Wendesday");//4

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);

                                Alarm_intent.putExtra("extra", "on");
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));

                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 3,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);

                            }
                        }
                        if (s.equals("Thursday")) {
                            String thursday = String.valueOf(Calendar.THURSDAY);
                            if (thursday.equals(day)) {
                                Log.e("Today is ", "Thursday");

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);


                                Alarm_intent.putExtra("extra", "on");
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));

                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 4,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);

                            }
                        }
                        if (s.equals("Friday")) {
                            String friday = String.valueOf(Calendar.FRIDAY);
                            if (friday.equals(day)) {
                                Log.e("Today is ", "friday");

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);

                                Alarm_intent.putExtra("extra", "on");
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));

                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 5,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);

                            }
                        }
                        if (s.equals("Saturday")) {
                            String saturday = String.valueOf(Calendar.SATURDAY);
                            if (saturday.equals(day)) {
                                Log.e("Today is ", "saturday");

                                //celender set time
                                calendar.set(Calendar.HOUR_OF_DAY, Cal_hour);
                                calendar.set(Calendar.MINUTE, Cal_minute);
                                calendar.set(Calendar.SECOND,0);

                                Alarm_intent.putExtra("extra", "on");
                                Alarm_intent.putExtra("ringtoneChoice", choose_ringtone);
                                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));

                                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(), 6,
                                        Alarm_intent, pending_intent.FLAG_UPDATE_CURRENT);

                                alarm_Manager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                        pending_intent);

                            }
                        }
                    }
                    if(mp!=null){
                        mp.stop();
                    }

                    boolean isInserted =  alarmDB.insertAlarmData(show_timetxt.getText().toString(),showRepeatTxt.getText().toString(),
                            ringtSound,isSoundChecked,progessVolume,silentMode);
                    if(isInserted = true){
                        Toast.makeText(setAlarm.this, "Alarm set!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(setAlarm.this, "Not set Alarm", Toast.LENGTH_SHORT).show();
                    }

//                    Toast.makeText(setAlarm.this, "Alarm set..!", Toast.LENGTH_SHORT).show();
//                    finish();

//                    set the alarm notification
                    alarmSetNotification();

                    Intent intent = new Intent(setAlarm.this, alarmHome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        //stop alarm
        Button alarm_stop = (Button) findViewById(R.id.alarm_off);

        alarm_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
//                Toast.makeText(setAlarm.this,"Alarm cancelled!!!", Toast.LENGTH_SHORT).show();

                //cancel the alarm
                alarm_Manager.cancel(pending_intent);

                //put extra string into ALarm_intent
                //tells the clock that you pressed the 'cancel' button
                Alarm_intent.putExtra("extra","off");

                //put extra int into Alarm_intent
                //tells the clock that you pressd 'cancel' button
                //to avoid exceptions
                Alarm_intent.putExtra("ringtoneChoice",choose_ringtone);

                //put extra string into Alarm_intent
                //tells the swich to off
                Alarm_intent.putExtra("silentExtra","off");

                //cancel the ringtone service
                sendBroadcast(Alarm_intent);

                if(mp !=null) {
                    mp.stop();
                }

//                // back to the previous activity
                Intent intent = new Intent(setAlarm.this, alarmHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                finish();
            }
        });

        //create the spinner to get ringtone elements
        final Spinner spinner = (Spinner) findViewById(R.id.RingToneSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ringtone_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //set onClickListner to the onItem SelectedListner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                choose_ringtone = (int) id;
               // Toast.makeText(setAlarm.this,"The selected choice is "+ id,Toast.LENGTH_SHORT).show();
                ringtSound = String.valueOf(parent.getItemAtPosition(position));

//                check whether media player working or not
                if (mp != null && mp.isPlaying()) {
                    mp.stop();
                }
                //set ringtone options
                switch (ringtSound) {
                    case "alarm Sound 1":
                        mp = MediaPlayer.create(setAlarm.this, R.raw.wake_up);
                        break;
                    case "alarm Sound 2":
                        mp = MediaPlayer.create(setAlarm.this, R.raw.alarm);
                        break;
                    case "alarm Sound 3":
                        mp = MediaPlayer.create(setAlarm.this, R.raw.wake_up_tone);
                        break;
                    case "alarm Sound 4":
                        mp = MediaPlayer.create(setAlarm.this, R.raw.sweet_alarm);
                        break;
                    case "alarm Sound 5":
                        mp = MediaPlayer.create(setAlarm.this, R.raw.morning_alarm);
                        break;
                    default:
                        break;
//                    here ringtone start
                }
                if(mp!=null)
                    mp.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            //seekbar methods for control volumes
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                progessVolume = String.valueOf(progress);
                //Toast.makeText(setAlarm.this,progress+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(setAlarm.this,progess_value+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        checkSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
//                    Toast.makeText(setAlarm.this,"checked",Toast.LENGTH_SHORT).show();
                    isSoundChecked = "true";
                    mp.stop();
                    spinner.setEnabled(false);
                }else{
//                    Toast.makeText(setAlarm.this,"Unchecked",Toast.LENGTH_SHORT).show();
                    // mp.stop();
                    isSoundChecked = "false";
                    spinner.setEnabled(true);
                }
            }
        });

//        cancel set alarm when click the notification
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                //Cry about not being clicked on
                Log.e("notify","default option");

                PackageManager pm  = setAlarm.this.getPackageManager();
                ComponentName componentName = new ComponentName(setAlarm.this, alarmReceiver.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
//                Toast.makeText(getApplicationContext(), "activated", Toast.LENGTH_LONG).show();

            }
            else if (extras.getBoolean("AlrmSetExtra"))
            {
                //Do your stuff here mate :)
//                Alarm_intent.putExtra("extra","off");
                Log.e("notify","cancel the receiver");

                PackageManager pm  = setAlarm.this.getPackageManager();
                ComponentName componentName = new ComponentName(setAlarm.this, alarmReceiver.class);
                pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.cancel(6);

                Toast.makeText(getApplicationContext(), "Alarm cancelled", Toast.LENGTH_LONG).show();

            }

        }


    }

    //create a method for get time from timepicker dialog
    public void showTimeDialog(){
        settimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(dialog_id);
            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id){
         if (id==dialog_id){
            return new TimePickerDialog(this,tpickerListner,Cal_hour,Cal_minute,false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener tpickerListner
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Cal_hour = hourOfDay;
            Cal_minute = minute;
            String ampm = "AM";

             hour = String.valueOf(Cal_hour);
             min = String.valueOf(Cal_minute);

            if(Cal_hour > 12){
                hour = String.valueOf(Cal_hour - 12);
                ampm = "PM";

            }if(Cal_minute < 10){
                min = "0"+String.valueOf(Cal_minute);
            }

            set_Alarm_status(hour+":"+min+" "+ampm);
            //Toast.makeText(setAlarm.this,hour+":"+minut, Toast.LENGTH_SHORT).show();
        }
    };



    //set alarm repeat days method
    public void showDialogAlarmdays() {

        setRepeatTxt.setOnClickListener(
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
                            mSelectedDaysItems.add(items[which]);
                        } else if (mSelectedDaysItems.contains(items[which])) {
                            // Else, if the item is already in the array, remove it
                            mSelectedDaysItems.remove(items[which]);
                        }
                    }
                });

        // Set the action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                selections="";
                for (String ms:mSelectedDaysItems) {
                    if(selections==""){
                        selections=ms;
                    }else{
                        selections=selections+","+ms;
                    }
                }

                //   Toast.makeText(setAlarm.this,selections, Toast.LENGTH_LONG).show();
                if(selections.equals("")){
                    showRepeatTxt.setText("Choose your days");
                }else{
                    showRepeatTxt.setText(selections);
                }
                   //Toast.makeText(setAlarm.this,selections, Toast.LENGTH_LONG).show();
            }
        });
        builder .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                showRepeatTxt.setText("No Repeat");
            }
        });

        ad =  builder.create();
    }

    //create a method for change the state
    private void set_Alarm_status(String status) {
        show_timetxt.setText(status);

    }

    //notification on when alarm is Set.
    public void alarmSetNotification(){

        //set up an intent that goes to the setAlarm activity
        Intent notifySetAlarmIntent = new Intent(this,setAlarm.class);

        notifySetAlarmIntent.putExtra("AlrmSetExtra",true);
        //set up a pending intent
        PendingIntent pending_setAlarm_activity;
        pending_setAlarm_activity = PendingIntent.getActivity(this,0,notifySetAlarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //make the notification parameters
        NotificationCompat.Builder notifyAlarmBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_alarm_on_white_24dp)
                .setContentTitle("Alarm Activated !!")
                .setAutoCancel(true)
                .addAction(R.drawable.ic_clear_white_24dp,"If you want to cancel",pending_setAlarm_activity);
        notifyAlarmBuilder.setContentIntent(pending_setAlarm_activity);
        Notification alarm_notification = notifyAlarmBuilder.build();
        alarm_notification.flags = Notification.FLAG_AUTO_CANCEL;


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(6,alarm_notification);

    }
}
