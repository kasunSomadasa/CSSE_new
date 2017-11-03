package com.example.kasun.busysms.callBlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;

/**
 * Created by madupoorna on 10/19/17.
 */

public class messageReceiver extends BroadcastReceiver {

    Boolean block_number=false;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String Num = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = 0+Num.substring(Num.length()-9);
                    Database_Helper DC = new Database_Helper(context);
                    DC.open();
                    Cursor c = DC.getDataCallBlocker();
                    int iNum = c.getColumnIndex(DC.columnName()[1]);
                    int iMSG = c.getColumnIndex(DC.columnName()[2]);

                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                        Toast.makeText(context, c.getString(iMSG), Toast.LENGTH_SHORT).show();

                        if(c.getString(iMSG).equals("1") && senderNum.equalsIgnoreCase( c.getString(iNum))){
                            block_number = true;
                            Toast.makeText(context, "block number", Toast.LENGTH_SHORT).show();
                            break;
                        } else
                            block_number = false;
                    }
                    DC.close();
                    if(block_number==true){
                        abortBroadcast();
                        setResultData(null);
                        Toast.makeText(context, "message blocked from "+senderNum, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
