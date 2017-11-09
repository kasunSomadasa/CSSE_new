package com.example.kasun.busysms.taskCalendar.Helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.addMinutesTo;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getDateOfDate;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getDateOfDateTime;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getDateOfTime;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getDateString;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getDateTimeString;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getDayOf;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getFormatedTimeString;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getFormattedDateString;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getMonthOf;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getTimeString;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getTodayMidNight;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getTodayMorning;
import static com.example.kasun.busysms.taskCalendar.Helper.DateEx.getYearOf;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nishan on 9/29/2017.
 * @version 1.0
 * @author Nishan
 */
public class DateExTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDateString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 0, 2);
        assertEquals("2017-01-02", getDateString(calendar.getTime()));
    }

    @Test
    public void testGetTimeString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 2);
        assertEquals("10:02", getTimeString(calendar.getTime()));
    }

    @Test
    public void testGetDateTimeString() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 3, 10, 2, 0);
        assertEquals("2017-03-03 10:02:00", getDateTimeString(calendar.getTime()));
    }

    @Test
    public void testGetDateOfDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(2017, 2, 3);
        assertEquals(calendar.getTime().getTime(), getDateOfDate("2017-03-03").getTime());
    }

    @Test
    public void testGetDateOfTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, 0, 1, 10, 2, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTime().getTime(), getDateOfTime("10:02").getTime());
    }

    @Test
    public void testGetDateOfDateTime() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 3, 10, 2, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar.getTime().getTime(), getDateOfDateTime("2017-03-03 10:02:00").getTime());
    }

    @Test
    public void testGetYearOf() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 3, 10, 2);
        assertEquals(2017, getYearOf(calendar.getTime()));
    }

    @Test
    public void testGetMonthOf() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 3, 10, 2);
        assertEquals(3, getMonthOf(calendar.getTime()));
    }

    @Test
    public void testGetDayOf() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 3, 10, 2);
        assertEquals(3, getDayOf(calendar.getTime()));
    }

    @Test
    public void testAddMinutesTo() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 2, 3, 10, 2, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2017, 2, 3, 10, 7, 0);
        calendar2.set(Calendar.MILLISECOND, 0);
        assertEquals(calendar2.getTimeInMillis(), addMinutesTo(calendar.getTime(), 5).getTime());
    }

    @Test
    public void testGetTodayMorning() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        calendar1.setTime(getTodayMorning());

        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testGetTodayMidNight() throws Exception {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        calendar1.setTime(getTodayMidNight());

        assertEquals(calendar.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testGetFormattedDateString() throws Exception {
        assertEquals("2017-02-04", getFormattedDateString("2017-2-4"));
    }

    @Test
    public void testGetFormatedTimeString() throws Exception {
        assertEquals("08:02", getFormatedTimeString("8:2"));
    }

}