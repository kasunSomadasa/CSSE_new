package com.example.kasun.busysms.taskCalendar.Helper;

import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nishan on 9/27/2017.
 * @version 2.1
 * @author Nishan
 *
 */

public class DateEx extends Date {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    public DateEx(){
        super();
    }

    public static String getDateString(Date date){
        return dateFormat.format(date);
    }

    public static String getTimeString(Date date){
        return timeFormat.format(date);
    }

    public static String getDateTimeString(Date date){
        return dateTimeFormat.format(date);
    }

    public static Date getDateOfDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    public static Date getDateOfTime(String date) throws ParseException {
        return timeFormat.parse(date);
    }

    public static Date getDateOfDateTime(String date) throws ParseException {
        return dateTimeFormat.parse(date);
    }

    /**
     * @param date date object which wants to extract the day. If null, gets today
     * @return
     */
    public static int getYearOf(@Nullable Date date){
        Calendar calendar = Calendar.getInstance();
        if(date == null)
            return calendar.get(Calendar.YEAR);
        else{
            calendar.clear();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR);
        }
    }

    /**
     * @param date date object which wants to extract the day. If null, gets today
     * @return month of date object or today
     */
    public static int getMonthOf(@Nullable Date date){
        Calendar calendar = Calendar.getInstance();
        if(date == null)
            return calendar.get(Calendar.MONTH)+1;
        else{
            calendar.clear();
            calendar.setTime(date);
            return calendar.get(Calendar.MONTH)+1;
        }
    }

    /**
     * @param date date object which wants to extract the day. If null, gets today
     * @return day of month of date object or today
     */
    public static int getDayOf(@Nullable Date date){
        Calendar calendar = Calendar.getInstance();
        if(date == null)
            return calendar.get(Calendar.DAY_OF_MONTH);
        else{
            calendar.clear();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH);
        }
    }

    /**
     * @param date date to add minutes
     * @param noOfMinutes number of minutes to add
     * @return incremented date
     */
    public static Date addMinutesTo(Date date, int noOfMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, noOfMinutes);
        return calendar.getTime();
    }

    /**
     * @return returns 00:00 for current day
     */
    public static Date getTodayMorning(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * @return returns 11:59 of current day
     */
    public static Date getTodayMidNight(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * @param date String date Ex. 2017-9-3
     * @return formatted string Ex. 2017-09-03
     * @throws ParseException
     */
    public static String getFormattedDateString(String date) throws ParseException {
        return dateFormat.format(dateFormat.parse(date));
    }

    /**
     * @param time String time Ex. 18:3
     * @return formatted string Ex. 18:03
     * @throws ParseException
     */
    public static String getFormatedTimeString(String time) throws ParseException {
        return timeFormat.format(timeFormat.parse(time));
    }
}
