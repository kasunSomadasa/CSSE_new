package com.example.kasun.busysms.autoSms;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.kasun.busysms.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class SmsHomeTest {

    @Rule
    public ActivityTestRule<SmsHome>  activityTestRule = new ActivityTestRule<SmsHome>(SmsHome.class);
    private SmsHome smsHomeActivity =null;

    Instrumentation.ActivityMonitor newMonitor = getInstrumentation().addMonitor(AddTimeSlot.class.getName(),null,false);
    Instrumentation.ActivityMonitor logMonitor = getInstrumentation().addMonitor(TimeSlotsList.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        smsHomeActivity=activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){
        View view = smsHomeActivity.findViewById(R.id.silentButton);
        assertNotNull(view);
    }

    @Test
    public void addBtnTest(){
        assertNotNull(smsHomeActivity.findViewById(R.id.newButton));
        onView(withId(R.id.newButton)).perform(click());
        Activity addNewActivity =getInstrumentation().waitForMonitorWithTimeout(newMonitor,5000);
        assertNotNull(addNewActivity);
        addNewActivity.finish();
    }

    @Test
    public void logBtnTest(){
        assertNotNull(smsHomeActivity.findViewById(R.id.logButton));
        onView(withId(R.id.logButton)).perform(click());
        Activity addNewActivity =getInstrumentation().waitForMonitorWithTimeout(logMonitor,5000);
        assertNotNull(addNewActivity);
        addNewActivity.finish();
    }


    @After
    public void tearDown() throws Exception {
        smsHomeActivity=null;
    }

}