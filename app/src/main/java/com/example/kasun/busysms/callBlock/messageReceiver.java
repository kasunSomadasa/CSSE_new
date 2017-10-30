package com.example.kasun.busysms.callBlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;

/**
 * Created by madupoorna on 10/19/17.
 */

public class messageReceiver extends BroadcastReceiver{

    Boolean block_number=false;

    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String senderNum = currentMessage.getDisplayOriginatingAddress();

                    Database_Helper DC = new Database_Helper(context);

                    DC.open();

                    Cursor c = DC.getDataCallBlocker();

                    int numberIndex = c.getColumnIndex(DC.columnName()[1]);
                    int msgIndex = c.getColumnIndex(DC.columnName()[2]);

                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        if(c.getString(msgIndex).equals("1") && senderNum.equalsIgnoreCase("+" + c.getString(numberIndex))){
                            block_number = true;
                            break;
                        } else
                            block_number = false;
                    }

                    DC.close();

                    if(block_number!=false){
                        abortBroadcast();

                        //write method to add number to msg block log
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context,"Exception occured",Toast.LENGTH_LONG);
        }
    }

}
