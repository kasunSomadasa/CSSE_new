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
public class SetAlarmTest {

        @Rule
        public ActivityTestRule<SetAlarm> testRule = new ActivityTestRule<SetAlarm>(SetAlarm.class);
        private SetAlarm setAlarmActivity  = null;


        @Before
        public void setUp() throws Exception {
            setAlarmActivity = testRule.getActivity();
        }

        @Test
        public void testAddTime(){
            View view = setAlarmActivity.findViewById(R.id.AddTimeText);
            assertNotNull(view);
        }

        @Test
        public void testShowRepeatTxt(){
            View view2 = setAlarmActivity.findViewById(R.id.show_repeats);
            assertNotNull(view2);
        }

        @Test
        public void testRepeatDays(){
            assertNotNull(setAlarmActivity.findViewById(R.id.repeat_days));
        }

        @After
        public void tearDown() throws Exception {
            setAlarmActivity = null;
        }


}