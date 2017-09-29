package com.example.kasun.busysms.taskCalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.taskCalendar.Database.TaskDB;
import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Model.Task;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {
    private static final String TAG = "AddTaskActivity";

    EditText txtTaskName, txtTaskLocation, txtTaskDate, txtStartTime, txtEndTime;
    EditText txtTaskDescription, txtTaskParticipants;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener startTimeSetListener, endTimeSetListener;
    Spinner spinnerSounds, spinnerTaskNotificationTime;
    ArrayAdapter<CharSequence> soundList;
    CheckBox chkAllDay;
    Button btnAddTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Controllers Definition
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
        btnAddTask = (Button) findViewById(R.id.btnAddTask);



        //Variables Definition
        final long selectedDate = (long)getIntent().getExtras().get("selectedDate");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(selectedDate));
        //TODO: need to set date automatically
        txtTaskDate.setText("2017-10-10");

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setTask_name("Test task");
                task.setTask_location("Kurunegala");
                try {
                    task.setTask_date(DateEx.getDateOfDate("2017-10-10"));
                    task.setTask_start(DateEx.getDateOfTime("10:03"));
                    task.setTask_end(DateEx.getDateOfTime("11:04"));
                } catch (ParseException e) {
                    Log.e(TAG, "Error while parsing date", e);
                }
                TaskDB taskDB = new TaskDB(AddTaskActivity.this);
                if(taskDB.insert(task)){
                    Toast.makeText(AddTaskActivity.this, "Task saved", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddTaskActivity.this, "Task not saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
