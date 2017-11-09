package com.example.kasun.busysms.taskCalendar;

import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import com.example.kasun.busysms.R;
import com.example.kasun.busysms.taskCalendar.Model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Nishan on 11/3/2017.
 * @author Nishan
 * @version 1.0
 */
public class ViewTaskActivityTest {
    @Rule
    public ActivityTestRule<ViewTaskActivity> activityTestRule = new ActivityTestRule<ViewTaskActivity>(ViewTaskActivity.class);
    private ViewTaskActivity viewTaskActivity;

    @Before
    public void setUp() throws Exception {
        viewTaskActivity = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        viewTaskActivity = null;
    }

    @Test
    public void checkViewItems() throws Exception{
        RecyclerView recyclerView = (RecyclerView) viewTaskActivity.findViewById(R.id.recyclerView_Tasks);
        List<Task> taskList = Task.getAllTasks(viewTaskActivity);
        assertEquals(taskList.size(), recyclerView.getAdapter().getItemCount());
    }

}