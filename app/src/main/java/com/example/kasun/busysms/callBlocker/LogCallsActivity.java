package com.example.kasun.busysms.callBlocker;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.example.kasun.busysms.R;

import java.util.Date;

/**
 * Created by madupoorna on 9/29/17.
 */

public class LogCallsActivity extends ActionBarActivity {

    ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callblock_call_log);

        ListView list_view = (ListView) findViewById(R.id.listView);
        getCallDetails();
    }

    private void getCallDetails() {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, null);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
        sb.append( "Call Details :");

        while ( managedCursor.moveToNext() ) {

            String phoneNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDur = managedCursor.getString( duration );
            String dir = null;
            int code = Integer.parseInt( callType );

            switch( code ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append( "\nPhone Number :--- "+phoneNumber +" \nType :--- "+dir+" \nDate :--- "+callDayTime+" \nDuration(in sec) :--- "+callDur );
            sb.append("\n----------------------------------");
        }
        managedCursor.close();

    }

}
