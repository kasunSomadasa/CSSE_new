package com.example.kasun.busysms.autoSms;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class autoSmsRecevierTest {

    autoSmsRecevier smsRecevier=new autoSmsRecevier();
    @Test
    public void getDayOfWeek() throws Exception {

        String expectedOutput ="Saturday";
        String output;
        output=smsRecevier.getDayOfWeek();

        assertEquals(expectedOutput,output);
    }

    @Test
    public void isBetweenValidTime() throws Exception {

        Date inputTimeOne =smsRecevier.parseDate("10:12:00");
        Date inputTimeTow =smsRecevier.parseDate("12:16:00");
        Date inputTimeNow =smsRecevier.parseDate("11:12:00");

        boolean expected =true;
        boolean output = smsRecevier.isBetweenValidTime(inputTimeOne,inputTimeTow,inputTimeNow);

        assertEquals(expected,output);

    }

}