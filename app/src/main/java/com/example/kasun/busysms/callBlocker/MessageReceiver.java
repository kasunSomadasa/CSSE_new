package com.example.kasun.busysms.callBlocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.kasun.busysms.Database_Helper;

/**
 * Created by madupoorna on 9/28/17.
 */

public class MessageReceiver  extends BroadcastReceiver {

    Boolean Is_blocked_number=false;

    @Override
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
                    Cursor c = DC.getData();
                    int iNum = c.getColumnIndex(DC.CallBlockercolumnName()[0]);
                    int iMSG = c.getColumnIndex(DC.CallBlockercolumnName()[1]);

                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        if(c.getString(iMSG).equals("1") && senderNum.equalsIgnoreCase("+" + c.getString(iNum))){
                            Is_blocked_number = true;
                            break;
                        } else
                            Is_blocked_number = false;
                    }
                    DC.close();

                    if(Is_blocked_number!=false){
                        abortBroadcast();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

}
