package com.example.kasun.busysms.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.kasun.busysms.R;

/**
 * Created by SM_MYPC on 9/29/2017.
 */

public class ringtonPlayingService extends Service {

    MediaPlayer alarm_song;
    boolean isRunning;
    int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);

        //fetch the extra strings from the on/off values
        String state = intent.getExtras().getString("extra");

        String silent_string = intent.getExtras().getString("silentExtra");
        Log.e("silent mode in service",silent_string);

        //fetch the extra integer
        //tt
        int ringtone_sound_choice = intent.getExtras().getInt("ringtoneChoice");

        Log.e("Rington state extra is ",state);
        Log.e("Rington choice is ", String.valueOf(ringtone_sound_choice));

        assert state != null;
//        assert vibrateState != null;
        switch (state) {
            case "on":
                startId = 1;
                break;
            case "off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        //if there is no music playing, and the user pressed "on"
        //music should start playing
        if (!this.isRunning && startId == 1){
            Log.e("there is no music ","and you want start");

            //create an instance of the media player
            //alarm_song = MediaPlayer.create(this, R.raw.wake_up_tone);
            // alarm_song.start();

            this.isRunning = true;
            this.startId = 0;

            notifyAlarm();

            //set ringtone options
            switch (ringtone_sound_choice) {
                case 1:{
                    //create an instance of the media player
                    alarm_song = MediaPlayer.create(this, R.raw.wake_up);
                    break;}
                case 2:{
                    //create an instance of the media player
                    alarm_song = MediaPlayer.create(this, R.raw.alarm);
                    break;}
                case 3:{
                    //create an instance of the media player
                    alarm_song = MediaPlayer.create(this, R.raw.wake_up_tone);
                    break;}
                case 4:{
                    //create an instance of the media player
                    alarm_song = MediaPlayer.create(this, R.raw.sweet_alarm);
                    break;}
                case 5:{
                    //create an instance of the media player
                    alarm_song = MediaPlayer.create(this, R.raw.morning_alarm);
                    break;}
                default:{
                    //any error happen create ringtone
                    alarm_song = MediaPlayer.create(this, R.raw.wake_up_tone);

                    break;}
            }
            if(alarm_song!=null){
                switch (am.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:
                        if (silent_string.equals("on")){
//                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            alarm_song.start();
                        }else{
//                            alarm_song.start();
                            alarm_song.setVolume(0,0);
                        }
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:
                        if (silent_string.equals("on")){
                            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            alarm_song.start();
                        }else{
//                            alarm_song.start();
                            alarm_song.setVolume(0,0);
                        }
                        break;
                    case AudioManager.RINGER_MODE_NORMAL:
                        alarm_song.start();
                        alarm_song.setLooping(true);
                        break;
                }
//                alarm_song.start();
//                alarm_song.setLooping(true);
            }

//            play alarm sound 1 minute
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alarm_song.stop();
                }
            }, 60 * 1000);


        }
        //if there is music playing, and the user pressed "off"
        //music should stop playing
        else if(this.isRunning && startId == 0){

            Log.e("there is music","and you want end");

            alarm_song.stop();
            alarm_song.reset();
            alarm_song.setLooping(false);

            this.isRunning = false;
            this.startId = 1;

        }

        //these are if the user presses random buttons
        //if there no music playing, and the user pressed "off"
        //do nothing
        else if(!this.isRunning && startId == 0){
            Log.e("there is no music","and you want end");

            this.isRunning = false;
            this.startId = 0;
        }
        //if there is music playing and the user pressed "on"
        //do nothing
        else if(this.isRunning && startId == 1){

            Log.e("there is music","and you want start");

            this.isRunning = true;
            this.startId = 1;

        }
        //any other thing catch
        else{
            Log.e("else","somehow you reach this");
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Tell the user we stopped.
        Toast.makeText(this,"On destroy called", Toast.LENGTH_SHORT).show();

        super.onDestroy();
        this.isRunning = false;
    }

    //notification on when alarm is triggring.
    public void notifyAlarm(){

        //set up an intent that goes to the setAlarm activity
        Intent notifyIntent = new Intent(this,setAlarm.class);
//        Intent notifyIn = new Intent(this,alarmFire.class);

        //set up a pending intent
        PendingIntent pending_setAlarm_activity;
        pending_setAlarm_activity = PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //make the notification parameters
        NotificationCompat.Builder notifyAlarmBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_alarm_on_black_24dp)
                .setContentTitle("Alarm on")
                .setAutoCancel(true);
        notifyAlarmBuilder.setContentIntent(pending_setAlarm_activity);
        Notification alarm_notification = notifyAlarmBuilder.build();
//        alarm_notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,alarm_notification);

    }


}
