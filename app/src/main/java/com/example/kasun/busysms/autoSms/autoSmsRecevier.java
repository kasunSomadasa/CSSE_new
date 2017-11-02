package com.example.kasun.busysms.autoSms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kasun on 11/15/2016.
 */
public class autoSmsRecevier extends BroadcastReceiver {

    Calendar now = Calendar.getInstance();

    int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);

    Date date = parseDate(hour + ":" + minute+":"+second);


    public String getDayOfWeek(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayOfTheWeek = simpleDateFormat.format(date);
        return dayOfTheWeek;
    }

    public static final boolean isBetweenValidTime(Date startTime, Date endTime, Date validateTime)
    {
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

        final String inputFormat = "HH:mm:ss";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.UK);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }


    public void onReceive(Context context,Intent intent) {
        Database_Helper db = new Database_Helper(context);
        Cursor c = db.getData();

        if (c.getCount() == 0) {

        } else {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                String sendernumber = null;
                Object[] pdus = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdus.length; i++) {

                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sendernumber = sms.getOriginatingAddress();
                    String msg = sms.getDisplayMessageBody();

                    Toast.makeText(context, "From: " + sendernumber + " Message: " + msg, Toast.LENGTH_LONG).show();


                }
                while (c.moveToNext()) {
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

                        if (isBetweenValidTime(dateCompareOne, dateCompareTwo, date)&& sendernumber.length()<=10) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(sendernumber, null, getdb_msg, null, null);

                        }
                    }

                }

            }
        }
    }
}