package com.example.kasun.busysms.callBlock;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madupoorna on 10/22/17.
 */

public class customAdapter extends BaseAdapter {

    private List<callBlockerModel> list;
    private LayoutInflater mInflater;
    Database_Helper helper;
    Context context;
    boolean isMsgChk=true;
    boolean isCallChk =true;
    com.example.kasun.busysms.callBlock.tab1Fragment tab1Fragment;

    public customAdapter(Context context, List<callBlockerModel> results) {

        list = results;
        mInflater = LayoutInflater.from(context);
        helper = new Database_Helper(context);
        this.context = context;
        checkIconShow();
    }

    public void setFragment(com.example.kasun.busysms.callBlock.tab1Fragment tab1Fragment) {
        this.tab1Fragment = tab1Fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder mHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.listviewmodel, null);
            mHolder = new ViewHolder();

            mHolder.name = (TextView) convertView.findViewById(R.id.NameTextView);
            mHolder.number = (TextView) convertView.findViewById(R.id.numberTextView);
            mHolder.msg = (CheckBox) convertView.findViewById(R.id.msgChkBox);
            mHolder.call = (CheckBox) convertView.findViewById(R.id.callChkBox);

            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        mHolder.name.setText(list.get(position).getName());
        mHolder.number.setText(list.get(position).getNumber());
        mHolder.msg.setChecked(list.get(position).isCheckMsg());
        mHolder.call.setChecked(list.get(position).isCheckCall());

        ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.deleteBtn);
        final CheckBox msgChkBox = (CheckBox) convertView.findViewById(R.id.msgChkBox);
        CheckBox callChkBox = (CheckBox) convertView.findViewById(R.id.callChkBox);

        deleteBtn.setOnClickListener(new CompoundButton.OnClickListener() {

            @Override
            public void onClick(View v) {

                String number = list.get(position).getNumber();
                helper.DeleteDataCallBlocker(number);

                list = GetlistData();
                //lv.setAdapter(this);
                notifyDataSetChanged();
                if(list.size() == 0){
                    ((callBlockerHome)context).disapperIcon();
                }
                Toast.makeText(v.getContext(), number + " deleted", Toast.LENGTH_LONG).show();
            }
        });

        msgChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String number = list.get(position).getNumber();
                boolean isMsgChk = isChecked;

                helper.EditMSG(number, isMsgChk);

                checkIconShow();

                System.out.println("***********" + number + isMsgChk);

            }
        });

        callChkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String number = list.get(position).getNumber();
                boolean isCallChk = isChecked;

                helper.EditCALL(number, isCallChk);

                checkIconShow();

                System.out.println("--------------" + number + isCallChk);

            }
        });

        return convertView;
    }

    public void checkIconShow(){

        if((isCallChk && isMsgChk) || isMsgChk || isCallChk ){
            ((callBlockerHome)context).showIcon();
        }else{
            ((callBlockerHome)context).disapperIcon();
        }
    }

    private List<callBlockerModel> GetlistData() {
        List<callBlockerModel> itemList = new ArrayList<>();

        callBlockerModel listItem;
        Database_Helper dbHelper = new Database_Helper(context);

        Cursor mCursor = dbHelper.getDataCallBlocker();

        if (mCursor.getCount() != 0) {
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                listItem = new callBlockerModel();
                String name = mCursor.getString(mCursor.getColumnIndex("_name"));
                String number = mCursor.getString(mCursor.getColumnIndex("_number"));
                boolean msg = mCursor.getInt(mCursor.getColumnIndex("_msg")) > 0;
                boolean call = mCursor.getInt(mCursor.getColumnIndex("_call")) > 0;

                listItem.setName(name);
                listItem.setNumber(number);
                listItem.setCheckMsg(msg);
                listItem.setCheckCall(call);

                itemList.add(listItem);
            }
        } else {
            Toast.makeText(context, "No data in table", Toast.LENGTH_LONG);
        }
        return itemList;
    }

    private class ViewHolder {
        private TextView name;
        private TextView number;
        private CheckBox msg;
        private CheckBox call;
    }

}
