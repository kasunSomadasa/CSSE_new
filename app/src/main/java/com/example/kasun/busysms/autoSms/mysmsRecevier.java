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
public class mysmsRecevier extends BroadcastReceiver {

    Calendar now = Calendar.getInstance();

    int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
    int minute = now.get(Calendar.MINUTE);
    int second = now.get(Calendar.SECOND);

    Date date = parseDate(hour + ":" + minute+":"+second);


    public String getDayofWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
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

    private Date parseDate(String date) {

        final String inputFormat = "HH:mm:ss";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.UK);
        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }


    public void onReceive(Context context,Intent intent) {
        Database_Helper mydb = new Database_Helper(context);
        Cursor c = mydb.getData();

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
                    String from_t = c.getString(1);
                    String to_t = c.getString(2);
                    String day_t = c.getString(4);
                    String msg_t = c.getString(5);
                    String call_t = c.getString(6);
                    String act_t = c.getString(8);


                    if (call_t.equals("true") && act_t.equals("Active") && day_t.equals(getDayofWeek())) {

                        Date dateCompareOne = parseDate(from_t);
                        Date dateCompareTwo = parseDate(to_t);
                        Toast.makeText(context, "Message From: " + from_t, Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Message To: " + to_t, Toast.LENGTH_LONG).show();

                        if (isBetweenValidTime(dateCompareOne, dateCompareTwo, date)&& sendernumber.length()==10) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(sendernumber, null, msg_t, null, null);

                        }
                    }

                }

            }
        }
    }
}