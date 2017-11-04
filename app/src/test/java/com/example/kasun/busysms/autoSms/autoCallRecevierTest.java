package com.example.kasun.busysms.autoSms;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/1/2017.
 */
public class autoCallRecevierTest {

    autoCallRecevier callRecevier =new autoCallRecevier();
    @Test
    public void isBetweenValidTime() throws Exception {

        Date inputTimeOne =callRecevier.parseDate("10:12:00");
        Date inputTimeTow =callRecevier.parseDate("12:16:00");
        Date inputTimeNow =callRecevier.parseDate("11:12:00");

        boolean expected =true;
        boolean output = callRecevier.isBetweenValidTime(inputTimeOne,inputTimeTow,inputTimeNow);

        assertEquals(expected,output);

    }

    @Test
    public void getDayOfWeek() throws Exception {

        String expectedOutput ="Saturday";
        String output;
        output=callRecevier.getDayOfWeek();

        assertEquals(expectedOutput,output);
    }

}