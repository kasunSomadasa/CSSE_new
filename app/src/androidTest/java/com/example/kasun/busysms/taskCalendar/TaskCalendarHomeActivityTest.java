package com.example.kasun.busysms.taskCalendar;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.kasun.busysms.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Nishan on 9/28/2017.
 * @author Nishan
 * @version 1.0
 */
public class TaskCalendarHomeActivityTest {
    @Rule
    public ActivityTestRule<TaskCalendarHomeActivity> activityTestRule = new ActivityTestRule<TaskCalendarHomeActivity>(TaskCalendarHomeActivity.class);
    private TaskCalendarHomeActivity taskCalendarHomeActivity;

    @Before
    public void setUp() throws Exception {
        taskCalendarHomeActivity = activityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        taskCalendarHomeActivity = null;
    }


    @Test
    public void checkMainCalendar(){
        View calendarView = taskCalendarHomeActivity.findViewById(R.id.calendarView_main);
        assertNotNull(calendarView);
    }

    @Test
    @UiThreadTest
    public void testTodayCalendarSelection(){
        Button btnToday = (Button) taskCalendarHomeActivity.findViewById(R.id.btnToday);
        CalendarView calendarView = (CalendarView) taskCalendarHomeActivity.findViewById(R.id.calendarView_main);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 4, 0, 0, 0);

        //Set calendarview date to 2017-Feb-4
        calendarView.setDate(calendar.getTimeInMillis());
        btnToday.performClick();


        Calendar timeNow = Calendar.getInstance();
        timeNow.clear(Calendar.HOUR);
        timeNow.clear(Calendar.MINUTE);
        timeNow.clear(Calendar.SECOND);
        timeNow.clear(Calendar.MILLISECOND);

        Calendar buttonClickTime = Calendar.getInstance();
        buttonClickTime.clear();
        buttonClickTime.setTimeInMillis(calendarView.getDate());
        buttonClickTime.clear(Calendar.HOUR);
        buttonClickTime.clear(Calendar.MINUTE);
        buttonClickTime.clear(Calendar.SECOND);
        buttonClickTime.clear(Calendar.MILLISECOND);

        assertEquals(timeNow.getTimeInMillis(), buttonClickTime.getTimeInMillis());
    }

}