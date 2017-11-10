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
public class AlarmHomeTest {

    @Rule
    public ActivityTestRule<AlarmHome> testRule = new ActivityTestRule<AlarmHome>(AlarmHome.class);
    private AlarmHome alarmHomeActivity  = null;

    @Before
    public void setUp() throws Exception {
        alarmHomeActivity = testRule.getActivity();

    }

    @Test
    public void testLaunch(){
        View view = alarmHomeActivity.findViewById(R.id.textday);
        assertNotNull(view);

    }

    @After
    public void tearDown() throws Exception {
        alarmHomeActivity = null;
    }

}