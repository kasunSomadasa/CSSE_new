package com.example.kasun.busysms.taskCalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.kasun.busysms.R;

import java.util.Date;

public class TaskCalendarHomeActivity extends AppCompatActivity {
    Button btnToday, btnViewTask;
    FloatingActionButton fabAddTask;
    CalendarView calendarView_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_calendar_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Controllers assignments
        fabAddTask = (FloatingActionButton) findViewById(R.id.fabAddTask);
        btnToday = (Button) findViewById(R.id.btnToday);
        btnViewTask = (Button) findViewById(R.id.btnViewTask);
        calendarView_main = (CalendarView) findViewById(R.id.calendarView_main);



        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskCalendarHomeActivity.this, AddTaskActivity.class);
                intent.putExtra("selectedDate", calendarView_main.getDate());
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnViewTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intentViewTask = new Intent(TaskCalendarHomeActivity.this, ViewTaskActivity.class);
                startActivity(intentViewTask);
                return false;
            }
        });
    }

    public void showToday(View view){
        calendarView_main.setDate(new Date().getTime(), true, true);
    }

    public void viewTask(View view){
        Intent intentViewTask = new Intent(TaskCalendarHomeActivity.this, ViewTaskActivity.class);
        intentViewTask.putExtra("selectedDate", calendarView_main.getDate());
        startActivity(intentViewTask);
    }
}
