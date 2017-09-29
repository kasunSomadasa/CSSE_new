package com.example.kasun.busysms.taskCalendar;

import android.support.test.rule.ActivityTestRule;
import android.widget.Button;

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
        Button btnAddTask = (Button) addTaskActivity.findViewById(R.id.btnAddTask);
        int oldTaskSize = Task.getAllTasks(addTaskActivity.getApplicationContext()).size();
        btnAddTask.performClick();
        int newTaskSize = Task.getAllTasks(addTaskActivity.getApplicationContext()).size();
        assertNotEquals(oldTaskSize, newTaskSize);
    }

}