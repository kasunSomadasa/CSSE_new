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
public class addTimeSlotTest {

    @Rule
    public ActivityTestRule<addTimeSlot> activityTestRule = new ActivityTestRule<addTimeSlot>(addTimeSlot.class);
    private addTimeSlot addTimeSlotActivity =null;

    @Before
    public void setUp() throws Exception {
        addTimeSlotActivity=activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){
        View view = addTimeSlotActivity.findViewById(R.id.addTimeSlot);
        assertNotNull(view);
    }


    @After
    public void tearDown() throws Exception {
        addTimeSlotActivity=null;
    }

}