package com.example.kasun.busysms.callBlock;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by madupoorna on 11/2/17.
 */

public class audioRecorder {

    public Context context;
    final MediaRecorder recorder = new MediaRecorder();
    public String path,number;

    public audioRecorder(Context context,String number) {
        this.context=context;
        this.number=number;
    }

    public static String ConfigureGetIniPath(Context context){
        if(context != null) {
            File file = context.getFilesDir();
            return file.getAbsolutePath();
        }else{
            return "/data/data/com.example.kasun.busysms/files";
        }
    }

    private String sanitizePath(String number) {

        path = ConfigureGetIniPath(context);
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd_hh_mm_ss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String dateTime = dateFormatter.format(today);

        path=path+"/"+number+"_"+dateTime;

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.contains(".")) {
            path += ".3gp";
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
    }

    /**
     * Starts a new recording.
     */
    public void startRecording() throws IOException {

        path=sanitizePath(number);

        File directory = new File(path).getParentFile();

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Path to file could not be created.");
        }

        recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        recorder.setOutputFile(path);

        recorder.prepare();
        recorder.start();
    }

    /**
     * Stops a recording that has been previously started.
     */
    public void stop() throws IOException {
        recorder.stop();
        recorder.release();
    }

}
