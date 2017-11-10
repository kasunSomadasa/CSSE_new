package com.example.kasun.busysms.callBlock;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kasun.busysms.DatabaseHelper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

/**
 * Created by madupoorna on 10/22/17.
 */

public class Tab3Fragment extends Fragment {

    DatabaseHelper helper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_tab_3, container, false);

        Button recCallBtn=(Button) view.findViewById(R.id.recNoBtn);
        Button blockWrodBtn=(Button) view.findViewById(R.id.blockWrdBtn);

        final TextView recNotv=(TextView) view.findViewById(R.id.recNumberTV);
        final TextView addWrdtv=(TextView) view.findViewById(R.id.blockWrdTV);

        final TextView timeFrom=(TextView) view.findViewById(R.id.fromTV);
        final TextView timeTo=(TextView) view.findViewById(R.id.toTV);

        Button fromBtn=(Button) view.findViewById(R.id.fromBtn);
        Button toBtn=(Button) view.findViewById(R.id.toBtn);
        Button addBtn=(Button) view.findViewById(R.id.addTimesBtn);
        Button timeListBtn=(Button) view.findViewById(R.id.blockedTimeListBtn);

        helper = new DatabaseHelper(view.getContext());

        recCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no=recNotv.getText().toString();
                if(no !=null && !no.isEmpty()) {
                    boolean isSuccess = helper.insertDataToCallRecorder(no);
                    if(isSuccess == true){
                        Toast.makeText(getActivity(), no + " added to call record list successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), no + " insertion failed", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please add number", Toast.LENGTH_LONG).show();
                }
            }
        });

        blockWrodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no=addWrdtv.getText().toString();
                if(no !=null && !no.isEmpty()) {
                    boolean isSuccess = helper.insertDataToCallBlockerWords(no);
                    if(isSuccess == true){
                        Toast.makeText(getActivity(), no + " added to msg block list successfully", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(), no + " insertion failed", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please add word", Toast.LENGTH_LONG).show();
                }
            }
        });

        fromBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeFrom.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        toBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeTo.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String from=timeFrom.getText().toString();
                String to=timeTo.getText().toString();
                if((!from.isEmpty() || from != null) && (to.isEmpty() || to != null)){
                    boolean isSucccess=helper.insertCallBlockTimes(from,to);
                    if(isSucccess == true){
                        Toast.makeText(v.getContext(), "times added successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(v.getContext(), "time insertion failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(v.getContext(), "Please fill both from and to fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        timeListBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder callLogAlertDialog = new AlertDialog.Builder(v.getContext());

                Cursor c = helper.getCallBlockTimes();

                ArrayList<String> mArrayList = new ArrayList<>();
                c.moveToFirst();
                while(!c.isAfterLast()) {
                    String from=c.getString(c.getColumnIndex("_from"));
                    String to = c.getString(c.getColumnIndex("_to"));
                    mArrayList.add(from+","+to);
                    c.moveToNext();
                }

                callLogAlertDialog.setTitle("blocked times");

                CharSequence[] list = new CharSequence[mArrayList.size()];
                list = mArrayList.toArray(list);
                final CharSequence[] finalAccounts_list = list;

                callLogAlertDialog.setItems(list, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, final int item) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        String time=finalAccounts_list[item].toString();
                                        String from=null;
                                        String to=null;
                                        StringTokenizer st = new StringTokenizer(time,",");
                                        while (st.hasMoreTokens()) {
                                            from=st.nextToken();
                                            to=st.nextToken();
                                        }

                                        helper = new DatabaseHelper(v.getContext());
                                        boolean delSuccess=helper.DeleteTime(from,to);
                                        if(delSuccess == true) {
                                            Toast.makeText(v.getContext(), finalAccounts_list[item]+ " deleted", Toast.LENGTH_LONG).show();
                                        }else{
                                            Toast.makeText(v.getContext(), "deletin failed", Toast.LENGTH_LONG).show();
                                        }
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure you want to delete time?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();


                    }



                });

                helper.getCallBlockTimes();
                callLogAlertDialog.create().show();
            }


        });

        return view;
    }
}
