package com.example.kasun.busysms.autoSms;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.kasun.busysms.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class updateTimeSlotTest {

    @Rule
    public ActivityTestRule<updateTimeSlot> activityTestRule = new ActivityTestRule<updateTimeSlot>(updateTimeSlot.class);
    private updateTimeSlot updateTimeSlotActivity =null;

    @Before
    public void setUp() throws Exception {
        updateTimeSlotActivity=activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){
        View view = updateTimeSlotActivity.findViewById(R.id.for_call);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        updateTimeSlotActivity =null;
    }

}