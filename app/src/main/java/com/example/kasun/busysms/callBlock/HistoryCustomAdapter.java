package com.example.kasun.busysms.callBlock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.List;

/**
 * Created by madupoorna on 10/31/17.
 */

public class historyCustomAdapter extends BaseAdapter {

    private List<callBlockerLogModel> historyList;
    private LayoutInflater mInflater;
    Database_Helper helper;
    Context context;
    com.example.kasun.busysms.callBlock.tab2Fragment tab2Fragment;

    public historyCustomAdapter(Context context, List<callBlockerLogModel> results) {
        historyList = results;
        mInflater = LayoutInflater.from(context);
        helper = new Database_Helper(context);
        this.context = context;
    }

    public void setFragment(com.example.kasun.busysms.callBlock.tab2Fragment tab2Fragment) {
        this.tab2Fragment = tab2Fragment;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.call_log_listview_model, null);
            mHolder = new historyCustomAdapter.ViewHolder();

            mHolder.number = (TextView) convertView.findViewById(R.id.numbertv);
            mHolder.dateTime = (TextView) convertView.findViewById(R.id.datetimetv);

            convertView.setTag(mHolder);
        } else {
            mHolder = (historyCustomAdapter.ViewHolder) convertView.getTag();
        }

        mHolder.number.setText(historyList.get(position).getNumber());
        mHolder.dateTime.setText(historyList.get(position).getDateTime());

        return convertView;
    }

    private class ViewHolder {
        private TextView number;
        private TextView dateTime;
    }
}
