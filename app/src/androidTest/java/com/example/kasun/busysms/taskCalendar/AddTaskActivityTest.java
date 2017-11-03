package com.example.kasun.busysms.taskCalendar;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.taskCalendar.Model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotEquals;

/**
 * Created by Nishan on 9/29/2017.
 * @version 1.0
 * @author Nishan
 */
public class AddTaskActivityTest {
    @Rule
    public ActivityTestRule<AddTaskActivity> activityTestRule = new ActivityTestRule<AddTaskActivity>(AddTaskActivity.class);
    private AddTaskActivity addTaskActivity;

    @Before
    public void setUp() throws Exception {
        addTaskActivity = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        addTaskActivity = null;
    }

    @Test
    public void testAddTask() throws Exception{
        int oldTaskCount = Task.getAllTasks(addTaskActivity.getApplicationContext()).size();

        EditText txtTaskName = (EditText) addTaskActivity.findViewById(R.id.txtTaskName);
        EditText txtTaskLocation = (EditText) addTaskActivity.findViewById(R.id.txtLocation);
        EditText txtTaskDate = (EditText) addTaskActivity.findViewById(R.id.txtTaskDate);
        EditText txtStartTime = (EditText) addTaskActivity.findViewById(R.id.txtStartTime);
        EditText txtEndTime = (EditText) addTaskActivity.findViewById(R.id.txtEndTime);
        EditText txtTaskDescription = (EditText) addTaskActivity.findViewById(R.id.txtTaskDescription);
        EditText txtTaskParticipants = (EditText) addTaskActivity.findViewById(R.id.txtTaskParticipants);
        Spinner spinnerTaskNotificationTime = (Spinner) addTaskActivity.findViewById(R.id.spinnerTaskNotificationTime);
        Spinner spinnerSounds = (Spinner) addTaskActivity.findViewById(R.id.spinnerSounds);
        CheckBox chkAllDay = (CheckBox) addTaskActivity.findViewById(R.id.chkAllDay);
        Button btnAddTask = (Button) addTaskActivity.findViewById(R.id.btnAddTask);

        txtTaskName.setText("Upandine");
        txtTaskLocation.setText("Kurunegala");
        txtTaskDate.setText("2018-08-08");
        txtStartTime.setText("08:08");
        txtEndTime.setText("10:08");
        txtTaskDescription.setText("Default description");
        txtTaskParticipants.setText("Spider Man");
        spinnerTaskNotificationTime.setSelection(0);
        spinnerSounds.setSelection(0);
        chkAllDay.setChecked(false);

        btnAddTask.performClick();
        int newTaskCount = Task.getAllTasks(addTaskActivity.getApplicationContext()).size();
        assertNotEquals(oldTaskCount, newTaskCount);
    }

}