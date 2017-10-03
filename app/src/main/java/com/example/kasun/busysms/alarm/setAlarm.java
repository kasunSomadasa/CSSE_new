package com.example.kasun.busysms.alarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.Calendar;

public class setAlarm extends AppCompatActivity {
    private TextView update_txt,settimeTxt,setRepeatTxt,showRepeatTxt,show_timetxt;
    ArrayList<String> mSelectedItems=new ArrayList<String>();
    static final int dialog_id = 0;
    public String selections;
    PendingIntent pending_intent;
    AlarmManager alarm_Manager;
    AlertDialog ad;
    Context context;
    int Cal_hour,Cal_minute;
    String hour,min;
    int choose_ringtone;
    SeekBar sb;
    MediaPlayer mp;
    AudioManager am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        this.context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //initialze the alarm manager
        alarm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize the update text box
        update_txt = (TextView) findViewById(R.id.alarm_status);

        //create calendar instance
        final Calendar calendar = Calendar.getInstance();
        Cal_hour = calendar.get(Calendar.HOUR_OF_DAY);
        Cal_minute = calendar.get(Calendar.MINUTE);


        //create an intent to the alam receiver class
        final Intent Alarm_intent = new Intent(this.context,alarmReceiver.class);

        //get id from time
        settimeTxt = (TextView) findViewById(R.id.AddTimeText);
        show_timetxt = (TextView) findViewById(R.id.alarm_status);

        //get ids of repeat days textboxes
        setRepeatTxt = (TextView) findViewById(R.id.repeat_days);
        showRepeatTxt = (TextView) findViewById(R.id.show_repeats);

        //set volume seekbar value
        sb = (SeekBar) findViewById(R.id.seekBar);
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb.setMax(maxVolume);
        sb.setProgress(currVolume);

       
        showTimeDialog();
        showDialogAlarmdays();

        //start alarm
        Button alarm_start = (Button) findViewById(R.id.alarm_on);

        alarm_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(setAlarm.this,hour+" : "+min, Toast.LENGTH_SHORT).show();

                calendar.set(Calendar.HOUR_OF_DAY,Cal_hour);
                calendar.set(Calendar.MINUTE,Cal_minute);

                //put extra string into Alarm_intent
                //tells the clock that you pressed the 'OK' button
                Alarm_intent.putExtra("extra","on");

                //put extra int into Alarm_intent
                //tells the clock that you want to certain value from spinner
                Alarm_intent.putExtra("ringtoneChoice",choose_ringtone);

                Log.e("Ringtone id : ", String.valueOf(choose_ringtone));

                //create a pending intent that delay the intent until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(setAlarm.this.getApplicationContext(),0,
                        Alarm_intent,pending_intent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_Manager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),
                        pending_intent);

                Toast.makeText(setAlarm.this,"Alarm is set...!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //stop alarm
        Button alarm_stop = (Button) findViewById(R.id.alarm_off);

        alarm_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(setAlarm.this,"cancel Alarm", Toast.LENGTH_SHORT).show();

                //cancel the alarm
                alarm_Manager.cancel(pending_intent);

                //put extra string into ALarm_intent
                //tells the clock that you pressed the 'cancel' button
                Alarm_intent.putExtra("extra","off");

                //put extra int into Alarm_intent
                //tells the clock that you pressd 'cancel' button
                //to avoid exceptions
                Alarm_intent.putExtra("ringtoneChoice",choose_ringtone);

                //cancel the ringtone service
                sendBroadcast(Alarm_intent);

                finish();
            }
        });


        //create the spinner to get ringtone elements
        Spinner spinner = (Spinner) findViewById(R.id.RingToneSpinner);
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

                String ringtSound = String.valueOf(parent.getItemAtPosition(position));
                //set ringtone options
                switch (ringtSound) {
                    case "alarm Sound 1":
                         mp = MediaPlayer.create(setAlarm.this, R.raw.wake_up);
                        break;
                    case "alarm Sound 2":
                        // mp.release();
                        mp = MediaPlayer.create(setAlarm.this, R.raw.alarm);
                        break;
                    case "alarm Sound 3":
                        //mp.release();
                        mp = MediaPlayer.create(setAlarm.this, R.raw.wake_up_tone);

                        break;
                    case "alarm Sound 4":
                        //mp.release();
                        mp = MediaPlayer.create(setAlarm.this, R.raw.sweet_alarm);

                        break;
                    case "alarm Sound 5":
                        //mp.release();
                        mp = MediaPlayer.create(setAlarm.this, R.raw.morning_alarm);

                        break;
                    default:
                        break;

                }if(mp != null ){
                        mp.start();
                    }



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

             hour = String.valueOf(Cal_hour);
             min = String.valueOf(Cal_minute);

            if(Cal_hour > 12){
                hour = String.valueOf(Cal_hour - 12);

            }if(Cal_minute < 10){
                min = "0"+String.valueOf(Cal_minute);
            }
            //show_timetxt.setText(hour+":"+min);

            set_Alarm_status(hour+":"+min);
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
                            mSelectedItems.add(items[which]);
                        } else if (mSelectedItems.contains(items[which])) {
                            // Else, if the item is already in the array, remove it
                            mSelectedItems.remove(items[which]);
                        }
                    }
                });
        // Set the action buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                    showRepeatTxt.setText("Choose your days");
                }else{
                    showRepeatTxt.setText(selections);
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

    //create a method for change the state
    private void set_Alarm_status(String status) {
        show_timetxt.setText(status);

    }
}
