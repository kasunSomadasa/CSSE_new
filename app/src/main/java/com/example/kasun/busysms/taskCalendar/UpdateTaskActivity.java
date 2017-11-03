package com.example.kasun.busysms.taskCalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Helper.ReminderActivator;
import com.example.kasun.busysms.taskCalendar.Model.Task;

import java.text.ParseException;

public class UpdateTaskActivity extends AppCompatActivity {

    int oldTaskId;
    private static final String TAG = "UpdateTaskActivity";

    Button btnDone;
    EditText txtTaskName, txtTaskLocation, txtTaskDate, txtStartTime, txtEndTime;
    EditText txtTaskDescription, txtTaskParticipants;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener startTimeSetListener, endTimeSetListener;
    Spinner spinnerSounds, spinnerTaskNotificationTime;
    ArrayAdapter<CharSequence> soundList;
    CheckBox chkAllDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Controllers Definition
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        btnDone = (Button) findViewById(R.id.btnAddTask);
        txtTaskName = (EditText) findViewById(R.id.txtTaskName);
        txtTaskLocation = (EditText) findViewById(R.id.txtLocation);
        txtTaskDate = (EditText) findViewById(R.id.txtTaskDate);
        txtStartTime = (EditText) findViewById(R.id.txtStartTime);
        txtEndTime = (EditText) findViewById(R.id.txtEndTime);
        txtTaskDescription = (EditText) findViewById(R.id.txtTaskDescription);
        txtTaskParticipants = (EditText) findViewById(R.id.txtTaskParticipants);
        spinnerTaskNotificationTime = (Spinner) findViewById(R.id.spinnerTaskNotificationTime);
        spinnerSounds = (Spinner) findViewById(R.id.spinnerSounds);
        chkAllDay = (CheckBox) findViewById(R.id.chkAllDay);
        soundList = ArrayAdapter.createFromResource(UpdateTaskActivity.this, R.array.sound_list,
                R.layout.support_simple_spinner_dropdown_item);
        soundList.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSounds.setAdapter(soundList);

        //Variable Definition
        oldTaskId = getIntent().getExtras().getInt("oldTaskId");

        //Assign old task data into ui
        Task tempTask = Task.getTaskById(oldTaskId, UpdateTaskActivity.this);
        txtTaskName.setText(tempTask.getTask_name());
        txtTaskDescription.setText(tempTask.getTask_description());
        txtTaskParticipants.setText(tempTask.getTask_participants());
        txtTaskLocation.setText(tempTask.getTask_location());
        txtTaskDate.setText(DateEx.getDateString(tempTask.getTask_date()));
        txtStartTime.setText(DateEx.getTimeString(tempTask.getTask_start()));
        txtEndTime.setText(DateEx.getTimeString(tempTask.getTask_end()));
        txtTaskDate.setText(DateEx.getDateString(tempTask.getTask_date()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateTaskActivity.this,
                        R.style.Theme_AppCompat_DayNight_Dialog,
                        dateSetListener,
                        DateEx.getYearOf(null),
                        DateEx.getMonthOf(null),
                        DateEx.getDayOf(null)
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateTaskActivity.this,
                        startTimeSetListener,
                        12,0,
                        false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UpdateTaskActivity.this,
                        endTimeSetListener,
                        12, 0,
                        false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String date = i+"-"+(i1+1)+"-"+i2;
                txtTaskDate.setText(date);
            }
        };

        startTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = i + ":" + i1;
                txtStartTime.setText(time);
            }
        };

        endTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = i + ":" + i1;
                txtEndTime.setText(time);
            }
        };

        spinnerSounds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MediaPlayer mediaPlayer = null;
                String soundName = adapterView.getItemAtPosition(i).toString().toLowerCase();
                switch (soundName){
                    case "buzz" :
                        mediaPlayer = MediaPlayer.create(UpdateTaskActivity.this, R.raw.buzz);
                        break;
                    case "gets in the way" :
                        mediaPlayer = MediaPlayer.create(UpdateTaskActivity.this, R.raw.gets_in_the_way);
                        break;
                    case "jingle bells" :
                        mediaPlayer = MediaPlayer.create(UpdateTaskActivity.this, R.raw.jingle_bells);
                        break;
                    case "rooster" :
                        mediaPlayer = MediaPlayer.create(UpdateTaskActivity.this, R.raw.rooster);
                        break;
                    case "siren" :
                        mediaPlayer = MediaPlayer.create(UpdateTaskActivity.this, R.raw.siren);
                        break;
                    case "system fault" :
                        mediaPlayer = MediaPlayer.create(UpdateTaskActivity.this, R.raw.system_fault);
                        break;
                }
                if(mediaPlayer != null)
                    mediaPlayer.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        chkAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(chkAllDay.isChecked()) {
                    txtStartTime.setText(DateEx.getTimeString(DateEx.getTodayMorning()));
                    txtEndTime.setText(DateEx.getTimeString(DateEx.getTodayMidNight()));
                    txtStartTime.setEnabled(false);
                    txtEndTime.setEnabled(false);
                }
                else{
                    txtStartTime.setEnabled(true);
                    txtEndTime.setEnabled(true);
                }
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task newTask = new Task();
                newTask.setTask_id(0);
                newTask.setTask_name(txtTaskName.getText().toString().trim());
                newTask.setTask_location(txtTaskLocation.getText().toString().trim());
                newTask.setTask_description(txtTaskDescription.getText().toString().trim());
                try {
                    newTask.setTask_date(DateEx.getDateOfDate(txtTaskDate.getText().toString().trim()));
                    newTask.setTask_start(DateEx.getDateOfTime(txtStartTime.getText().toString().trim()));
                    newTask.setTask_end(DateEx.getDateOfTime(txtEndTime.getText().toString().trim()));
                } catch (ParseException e) {
                    Log.e(TAG, "Error while parsing data into task class", e);
                }
                newTask.setTask_participants(txtTaskParticipants.getText().toString().trim());
                newTask.setIs_all_day_task(chkAllDay.isChecked());
                newTask.setTask_notification_sound(spinnerSounds.getSelectedItem().toString().trim());
                String notify = (String) spinnerTaskNotificationTime.getSelectedItem();
                switch(notify){
                    case "On time" :
                        newTask.setTask_notification_time(newTask.getTask_start());
                        break;
                    case "Before 2 minutes" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 2));
                        break;
                    case "Before 5 minutes" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 5));
                        break;
                    case "Before 10 minutes" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 10));
                        break;
                    case "Before 30 minutes" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 30));
                        break;
                    case "Before 1 hour" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 60));
                        break;
                    case "Before 3 hours" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 180));
                        break;
                    case "Before 1 day" :
                        newTask.setTask_notification_time(DateEx.addMinutesTo(newTask.getTask_start(), 1440));
                        break;
                    default:
                        newTask.setTask_notification_time(newTask.getTask_start());
                        break;
                }

                if(Task.updateTaskInDB(UpdateTaskActivity.this, newTask, oldTaskId)){
                    ReminderActivator.suspendReminder(UpdateTaskActivity.this,
                            Task.getTaskById(oldTaskId, UpdateTaskActivity.this));
                    ReminderActivator.runActivator(UpdateTaskActivity.this, newTask);
                    Toast.makeText(UpdateTaskActivity.this, "Reminder updated successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
                else{
                    Toast.makeText(UpdateTaskActivity.this, "Error while updating..", Toast.LENGTH_LONG).show();
                    Log.w(TAG, "Error got while updating taskId: "+oldTaskId);
                }
            }
        });
    }
}
