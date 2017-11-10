package com.example.kasun.busysms.alarm;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.example.kasun.busysms.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by SM_MYPC on 11/3/2017.
 */
public class alarmFireTest {
    @Rule
    public ActivityTestRule<alarmFire> testRule = new ActivityTestRule<alarmFire>(alarmFire.class);
    private alarmFire alarmFireActivity  = null;

    @Before
    public void setUp() throws Exception {
        alarmFireActivity = testRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view = alarmFireActivity.findViewById(R.id.imageButton_AlarmOff);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        alarmFireActivity = null;
    }

}