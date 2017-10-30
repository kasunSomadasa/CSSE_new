package com.example.kasun.busysms.callBlock;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by madupoorna on 10/22/17.
 */

public class Tab2Fragment extends Fragment {

    ArrayList<String> Calllist1 = new ArrayList<>();
    ListView callListView;
    ArrayAdapter<String> arrayAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_tab_2,container,false);

        callListView = (ListView)view.findViewById(R.id.frag2ListView);
        arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,Calllist1);
        callListView.setAdapter(arrayAdapter);
        getCallDetails();

        return view;

    }

    private void getCallDetails() {

        String strOrder = CallLog.Calls.DATE + " DESC";
        Uri callUri = Uri.parse("content://call_log/calls");
        ContentResolver ca = getActivity().getContentResolver();
        Cursor managedCursor = getActivity().getContentResolver().query(callUri, null, null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            String callDuration = managedCursor.getString(duration);
            Calllist1.add(phNum);
            Calllist1.add(callTypeCode);
            Calllist1.add(strcallDate);
            Calllist1.add(callDuration);

            int callcode = Integer.parseInt(callTypeCode);
            String callType;
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;

            }
        }
        managedCursor.close();

    }

}
