package com.example.kasun.busysms.callBlock;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madupoorna on 10/22/17.
 */

public class tab2Fragment extends Fragment {

    Database_Helper dbHelper;
    List<callBlockerLogModel> results;
    historyCustomAdapter adapter;
    Context superContext;

    public tab2Fragment() {
    }

    public void setContext(Context context) {
        this.superContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tab_2, container, false);

        dbHelper = new Database_Helper(this.getActivity());
        results = GetlistData();

        ListView lv = (ListView) view.findViewById(R.id.frag2ListView);

        //set listview scrollable
        setListViewHeightBasedOnChildren(lv);

        adapter = new historyCustomAdapter(this.getContext(), results);

        lv.setAdapter(adapter);

        return view;

    }

    //method to make list view scrollable
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //method to get data from table
    private List<callBlockerLogModel> GetlistData() {
        List<callBlockerLogModel> itemList = new ArrayList<>();

        callBlockerLogModel listItem;
        Database_Helper dbHelper = new Database_Helper(getContext());

        Cursor mCursor = dbHelper.getDataCallBlockerHistory();

        if (mCursor.getCount() != 0) {
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                listItem = new callBlockerLogModel();
                String number = mCursor.getString(mCursor.getColumnIndex("_blockedNumber"));
                String dateTime = mCursor.getString(mCursor.getColumnIndex("_blockedDate"));

                listItem.setNumber(number);
                listItem.setDateTime(dateTime);

                itemList.add(listItem);
            }
        } else {
            Toast.makeText(getContext(), "No data in table", Toast.LENGTH_LONG);
        }
        return itemList;
    }

}
