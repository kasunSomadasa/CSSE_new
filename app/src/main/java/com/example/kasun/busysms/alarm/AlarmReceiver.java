package com.example.kasun.busysms.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SM_MYPC on 9/29/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context,"Alarm is running.... ",Toast.LENGTH_LONG).show();
        Log.e("we are in the receiver", "Hooo");
                //create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonPlayingService.class);

                //create new intent to the alarm fire
        Intent alarmTriggerIntent = new Intent();

                //fetch extra strings from the intent
                //tells the app whether the user pressed the "on" or "off" button
        String get_alarm_String = intent.getExtras().getString("extra");
        Log.e("what is the key? ", get_alarm_String);

        //fetch extra int from intent
        //tells the app which value the user picked from the spinner
        int get_ringtone_choice = intent.getExtras().getInt("ringtoneChoice");
        Log.e("ringtonReceivechoice : ", String.valueOf(get_ringtone_choice));

        //fetch extra intent from the intent
        //switch to on or off
        String get_silent_string = intent.getExtras().getString("silentExtra");
        Log.e("silent mode receiver : ",get_silent_string);


        //pass the extra string from receiver to the rington Playing Service
        service_intent.putExtra("extra", get_alarm_String);

        //pass the extra int from receiver to the ringtone playing service
        service_intent.putExtra("ringtoneChoice", get_ringtone_choice);

        //pass the extra String from receiver to the ringtone playing service
        service_intent.putExtra("silentExtra", get_silent_string);


        //start the ringtone service
        context.startService(service_intent);


        //pass the extra String from receiver to the newly create intent
        alarmTriggerIntent.putExtra("silentExtra",get_silent_string);
        Log.e("silentMode re to fire ",get_silent_string);

        //start new intent when  alarm is triggering
        if(get_alarm_String.equals("on")) {
            alarmTriggerIntent.setClass(context, AlarmFire.class);
            alarmTriggerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmTriggerIntent);
        }
    }
}