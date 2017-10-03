package com.example.kasun.busysms.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by SM_MYPC on 9/29/2017.
 */

public class alarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Toast.makeText(context,"Alarm is running.... ",Toast.LENGTH_LONG).show();
        Log.e("we are in the receiver","Hooo");

        //start new intent when  alarm is triggering
        Intent alarmTrggerIntent = new Intent();
        alarmTrggerIntent.setClassName("com.example.kasun.busysms.alarm","com.example.kasun.busysms.alarm.alarmFire");
        alarmTrggerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmTrggerIntent);


        //fetch extra strings from the intent
        //tells the app whether the user pressed the "on" or "off" button
        String get_your_String = intent.getExtras().getString("extra");
        Log.e("what is the key? ",get_your_String);


        //fetch extra int from intent
        //tellse the app which value the user picked from the spinner
        int get_ringtone_choice = intent.getExtras().getInt("ringtoneChoice");
        Log.e("ringtonReceivechoice : ", String.valueOf(get_ringtone_choice));


        //create an intent to the ringtone service
        Intent service_intent = new Intent(context,ringtonPlayingService.class);

        //pass the extra string from receiver to the rington Playing Service
        service_intent.putExtra("extra",get_your_String);

        //pass the extra int from receiver to the ringtone playing service
        service_intent.putExtra("ringtoneChoice",get_ringtone_choice);


        //start the rington service
        context.startService(service_intent);



    }
}
