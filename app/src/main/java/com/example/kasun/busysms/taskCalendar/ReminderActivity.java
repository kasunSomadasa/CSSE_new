package com.example.kasun.busysms.taskCalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Helper.ReminderActivator;
import com.example.kasun.busysms.taskCalendar.Model.Task;

/**
 * @author Nishan
 * @version 1.0
 */
public class ReminderActivity extends AppCompatActivity {

    TextView txtTaskName, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        txtTaskName = (TextView) findViewById(R.id.txtTaskName);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        Task task = (Task)getIntent().getSerializableExtra("task");
        txtTaskName.setText(task.getTask_name().toUpperCase());
        String description = task.getTask_description()+
                "\nLocation: "+task.getTask_location()+
                "\nOn: "+ DateEx.getDateString(task.getTask_date());
        if(task.is_all_day_task()){
            description = description
                    +"\nIn whole day.";
        }else{
            description = description
                    +"\nFrom: "+DateEx.getTimeString(task.getTask_start())+" to "+DateEx.getTimeString(task.getTask_end())+".";
        }
        if(task.getTask_participants().length() > 0){
            description = description
                    +"\n"+task.getTask_participants()+" will be with you!";
        }
        txtDescription.setText(description);
    }

    public void RemindMeAgain(View view){
        Task task = (Task)getIntent().getSerializableExtra("task");
        ReminderActivator.postponeReminder(ReminderActivity.this, task, 1);
        finish();
    }

    public void StopReminder(View view){
        Task task = (Task)getIntent().getSerializableExtra("task");
        ReminderActivator.suspendReminder(ReminderActivity.this, task);
        finish();
    }
}
