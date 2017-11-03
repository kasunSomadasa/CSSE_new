package com.example.kasun.busysms.taskCalendar.Helper;

import android.widget.EditText;

/**
 * Created by Nishan on 11/1/2017.
 * @author Nishan
 * @version 1.0
 */

public class ValidationHelper {
    EditText txtTaskName;
    EditText txtTaskLocation;
    EditText txtTaskDate;
    EditText txtStartTime;
    EditText txtEndTime;
    EditText txtTaskDescription;

    public void setAddTaskActivityValidator(EditText txtTaskName, EditText txtTaskLocation, EditText txtTaskDate,
                                            EditText txtStartTime, EditText txtEndTime, EditText txtTaskDescription){
        this.txtTaskName = txtTaskName;
        this.txtTaskLocation = txtTaskLocation;
        this.txtTaskDate = txtTaskDate;
        this.txtStartTime = txtStartTime;
        this.txtEndTime = txtEndTime;
        this.txtTaskDescription = txtTaskDescription;
    }

    public boolean validateAddTask(){
        boolean isValid = true;
        if(txtTaskName.getText().toString().equals("")){
            txtTaskName.setError("Enter a name for task");
            isValid = false;
        }
        if(txtTaskLocation.getText().toString().equals("")){
            txtTaskLocation.setError("Enter location where task hold");
            isValid = false;
        }
        if(txtTaskDate.getText().toString().equals("")){
            txtTaskDate.setError("Enter date of task");
            isValid = false;
        }
        if(!txtTaskDate.getText().toString().matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")){
            txtTaskDate.setError("Enter date in yyyy-mm-dd format");
            isValid = false;
        }
        if(txtStartTime.getText().toString().equals("")){
            txtStartTime.setError("Enter time of task start");
            isValid = false;
        }
        if(!txtStartTime.getText().toString().matches("[0-9]{2}:[0-9]{2}")){
            txtStartTime.setError("Enter time in HH:mm 24hr format");
            isValid = false;
        }
        if(txtEndTime.getText().toString().equals("")){
            txtEndTime.setError("Enter time of task end");
            isValid = false;
        }
        if(!txtEndTime.getText().toString().matches("[0-9]{2}:[0-9]{2}")){
            txtEndTime.setError("Enter time in HH:mm 24hr format");
            isValid = false;
        }
        return isValid;
    }
}
