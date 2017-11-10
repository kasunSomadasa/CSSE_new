package com.example.kasun.busysms.callBlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.example.kasun.busysms.DatabaseHelper;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallReceiver extends BroadcastReceiver {

    public static boolean inCall;

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneCallStateListener customPhoneListener = new PhoneCallStateListener(context);
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    public class PhoneCallStateListener extends PhoneStateListener {

        private Context context;
        boolean block_number = false;
        boolean isBetween=false;

        public PhoneCallStateListener(Context context) {
            this.context = context;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            DatabaseHelper DC = new DatabaseHelper(context);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:

                    Toast.makeText(context, "Ringing", Toast.LENGTH_LONG).show();

                    inCall = false;


                    DC.open();

                    Cursor timeCursor = DC.getCallBlockTimes();
                    for (timeCursor.moveToFirst(); !timeCursor.isAfterLast(); timeCursor.moveToNext()) {
                        String from=timeCursor.getString(timeCursor.getColumnIndex("_from"));
                        String to=timeCursor.getString(timeCursor.getColumnIndex("_to"));

                        if (isBetween(from,to)) {//block
                            block_number = true;
                            isBetween = true;
                            break;
                        }
                        else{//check is in block list
                            block_number = false;
                        }
                    }
                    Cursor c = DC.getDataCallBlocker();
                    int indexNumber = c.getColumnIndex(DC.columnName()[1]);
                    int indexCall = c.getColumnIndex(DC.columnName()[3]);

                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                        if(isBetween == true){
                            break;
                        }

                        if (c.getString(indexCall).equals("1") && PhoneNumberUtils.compare(incomingNumber, c.getString(indexNumber))) {
                            block_number = true;
                            break;
                        } else
                            block_number = false;
                    }

                    DC.close();

                    AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

                    audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);

                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                    try {

                        Class cls = Class.forName(telephonyManager.getClass().getName());
                        Method method = cls.getDeclaredMethod("getITelephony");
                        method.setAccessible(true);
                        ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);

                        if (block_number == true) {

                            Toast.makeText(context, "block number", Toast.LENGTH_LONG).show();
                            audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                            telephonyService = (ITelephony) method.invoke(telephonyManager);
                            telephonyService.endCall();
                            audioManager.setStreamMute(AudioManager.STREAM_RING, false);

                            //add number to blocked call log history
                            DateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                            dateFormatter.setLenient(false);
                            Date today = new Date();
                            String dateTime = dateFormatter.format(today);

                            DC.open();
                            DC.insertDataToCallBlockerHistory(incomingNumber, dateTime);
                            DC.close();

                        }
                    } catch (Exception e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                    }

                    audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
                    audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                  /*  AudioRecorder recObj = new AudioRecorder(context,incomingNumber);
                    try {
                        recObj.stop();
                        Toast.makeText(context, "recording finished", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */
                    inCall = false;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    inCall = true;
                   /* DC.open();
                    Cursor numCursor = DC.getRecordNumbers(incomingNumber);
                    for (numCursor.moveToFirst(); !numCursor.isAfterLast(); numCursor.moveToNext()) {
                        if (PhoneNumberUtils.compare(incomingNumber, numCursor.getString(numCursor.getColumnIndex("_recNumber")))) {

                            Toast.makeText(context, "no is in list start recording", Toast.LENGTH_LONG).show();
                            recObj = new AudioRecorder(context,incomingNumber);
                            try {
                                recObj.startRecording();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        } else {
                            Toast.makeText(context, "no not in list not recording", Toast.LENGTH_LONG).show();
                        }
                    }
                    DC.close();

                    */

                    Toast.makeText(context, "recording started", Toast.LENGTH_LONG).show();
                    //Toast.makeText(context,"in a call",Toast.LENGTH_LONG).show();
                    break;
            }

            super.onCallStateChanged(state, incomingNumber);
        }

        public boolean isBetween(String from, String to) {

            try {
                DateFormat dateformatter = new SimpleDateFormat("HH:mm");

                Date startime = dateformatter.parse(from);
                Date endtime = dateformatter.parse(to);

                String cur_time = dateformatter.format(new Date());
                Date current_time=dateformatter.parse(cur_time);

                if (current_time.after(startime) && current_time.before(endtime)) {
                    return true;
                } else {
                    return false;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }

    }
}