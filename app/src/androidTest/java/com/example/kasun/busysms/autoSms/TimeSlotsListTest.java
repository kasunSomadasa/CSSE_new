package com.example.kasun.busysms.autoSms;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.kasun.busysms.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class TimeSlotsListTest {

    @Rule
    public ActivityTestRule<TimeSlotsList> activityTestRule = new ActivityTestRule<TimeSlotsList>(TimeSlotsList.class);
    private TimeSlotsList timeSlotsListActivity =null;


    @Before
    public void setUp() throws Exception {
        timeSlotsListActivity = activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){
        View view = timeSlotsListActivity.findViewById(R.id.list);
        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception {
        timeSlotsListActivity=null;
    }

}