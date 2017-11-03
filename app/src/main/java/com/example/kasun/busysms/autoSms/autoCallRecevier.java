package com.example.kasun.busysms.autoSms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;


/**
 * Created by Kasun Somadasa
 * This class run in background for identify incomming Call
 */
public class autoCallRecevier extends BroadcastReceiver {

    Calendar now = Calendar.getInstance();
    // Get hour in 24 hour format
    int hour = now.get(Calendar.HOUR_OF_DAY);
    // Get Currunt minute
    int minute = now.get(Calendar.MINUTE);
    // Get Currunt second
    int second = now.get(Calendar.SECOND);

    //make current time in Date format
    Date date = parseDate(hour + ":" + minute+":"+second);


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

    public String getDayOfWeek(){
         /*
         * get day of week in name format (i.e. Monday)
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayOfTheWeek = simpleDateFormat.format(date);
        return dayOfTheWeek;
    }

    public void onReceive(Context context,Intent intent) {
        /*
         * this method run in background and detect incomming Call event
         * and check that incomming Call time is between our time slots then return our sms to that number
         */
        Database_Helper db = new Database_Helper(context);
        Cursor c = db.getData();

        if (c.getCount() == 0) {

        } else {
            //detect call ring event and identify its a call then get its number
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

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
                        Toast.makeText(context, "Call From: " + getdb_from, Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Call To: " + getdb_to, Toast.LENGTH_LONG).show();
                        Log.i("INFO_CALL","Detected call recevier event");

                        if (isBetweenValidTime(dateCompareOne, dateCompareTwo, date)) {

                            Toast.makeText(context, "Call From: " + incomingNumber, Toast.LENGTH_LONG).show();
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(incomingNumber,null,getdb_msg,null,null);
                            Log.i("INFO_CALL","Auto SMS sent");

                        }

                    }
                }
            //detect call hangup event
            } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                Toast.makeText(context, "Detected call hangup event", Toast.LENGTH_LONG).show();
                Log.i("INFO_CALL","Detected call hangup event");
            }
        }


    }

}
