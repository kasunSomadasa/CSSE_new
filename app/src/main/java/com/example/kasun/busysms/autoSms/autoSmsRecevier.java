package com.example.kasun.busysms.autoSms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kasun Somadasa
 * This class run in background for identify incomming SMS
 */
public class autoSmsRecevier extends BroadcastReceiver {

    Calendar now = Calendar.getInstance();

    // Get Currunt hour in 24 hour format
    int hour = now.get(Calendar.HOUR_OF_DAY);
    // Get Currunt minute
    int minute = now.get(Calendar.MINUTE);
    // Get Currunt second
    int second = now.get(Calendar.SECOND);

    //make current time in Date format
    Date date = parseDate(hour + ":" + minute+":"+second);


    public String getDayOfWeek(){
        /*
         * get day of week in name format (i.e. Monday)
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayOfTheWeek = simpleDateFormat.format(date);
        return dayOfTheWeek;
    }

    public static final boolean isBetweenValidTime(Date startTime, Date endTime, Date validateTime)
    {
        /*
         * check given time(now) is between start and end times
         * if thats in between then return true.
         */
        boolean validTimeFlag = false;

        if(endTime.compareTo(startTime) <= 0)
        {
            if(validateTime.compareTo(endTime) < 0 || validateTime.compareTo(startTime) >= 0)
            {
                validTimeFlag = true;
            }
        }
        else if(validateTime.compareTo(endTime) < 0 && validateTime.compareTo(startTime) >= 0)
        {
            validTimeFlag = true;
        }

        return validTimeFlag;
    }

    public Date parseDate(String date) {
        /*
         * given String format date convert to Date format
         * because isBetweenValidTime wants date in Date format
         */
        final String inputFormat = "HH:mm:ss";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.UK);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }


    public void onReceive(Context context,Intent intent) {
        /*
         * this method run in background and detect incomming sms event
         * and check that incomming sms time is between our time slots then return our sms to that number
         */
        Database_Helper db = new Database_Helper(context);
        Cursor c = db.getData();

        if (c.getCount() == 0) {

        } else {
             /*
              * Get incomming sms as a bundle and split into 'pdus'(sms which exceed normal sms length)
              * and print that sms and get sender number
              *
              */
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                String senderNumber = null;
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {

                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    senderNumber = sms.getOriginatingAddress();
                    String msg = sms.getDisplayMessageBody();

                    Toast.makeText(context, "From: " + senderNumber + " Message: " + msg, Toast.LENGTH_LONG).show();


                }

                while (c.moveToNext()) {
                    //Get time solt data from db and check is between valid time and its activation then send that stored msg
                    String getdb_from = c.getString(1);
                    String getdb_to = c.getString(2);
                    String getdb_day = c.getString(4);
                    String getdb_msg = c.getString(5);
                    String getdb_call = c.getString(6);
                    String getdb_activation = c.getString(8);


                    if (getdb_call.equals("true") && getdb_activation.equals("Active") && getdb_day.equals(getDayOfWeek())) {

                        Date dateCompareOne = parseDate(getdb_from);
                        Date dateCompareTwo = parseDate(getdb_to);
                        Toast.makeText(context, "Message From: " + getdb_from, Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Message To: " + getdb_to, Toast.LENGTH_LONG).show();
                        Log.i("INFO_SMS","Detected sms recevier event");

                        if (isBetweenValidTime(dateCompareOne, dateCompareTwo, date)) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(senderNumber, null, getdb_msg, null, null);
                            Log.i("INFO_SMS","Auto SMS sent");
                        }
                    }

                }

            }
        }
    }
}