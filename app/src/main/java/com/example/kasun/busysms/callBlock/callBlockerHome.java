package com.example.kasun.busysms.callBlock;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasun.busysms.Database_Helper;
import com.example.kasun.busysms.R;

import java.util.ArrayList;
import java.util.List;

public class callBlockerHome extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private FloatingActionButton fab_plus, fab_contact, fab_dial, fab_log;
    Animation fabOpen, fabClose, fabRotateClockwise, fabRotateAnticlockwise;
    boolean isOpen = false;
    private final int PICK_CONTACTS = 1;
    Database_Helper dbObj;

    Tab1Fragment tab1Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_blocker_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab_plus = (FloatingActionButton) findViewById(R.id.fab_plus);
        fab_dial = (FloatingActionButton) findViewById(R.id.fab_dialer);
        fab_contact = (FloatingActionButton) findViewById(R.id.fab_contactlist);
        fab_log = (FloatingActionButton) findViewById(R.id.fab_calllog);

        fabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fabRotateClockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        fabRotateAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        dbObj = new Database_Helper(this);

        //show plus button animations
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOpen) {
                    fab_dial.startAnimation(fabClose);
                    fab_log.startAnimation(fabClose);
                    fab_contact.startAnimation(fabClose);
                    fab_plus.startAnimation(fabRotateAnticlockwise);

                    fab_dial.setClickable(false);
                    fab_contact.setClickable(false);
                    fab_log.setClickable(false);

                    isOpen = false;

                } else {
                    fab_dial.startAnimation(fabOpen);
                    fab_log.startAnimation(fabOpen);
                    fab_contact.startAnimation(fabOpen);
                    fab_plus.startAnimation(fabRotateClockwise);

                    fab_dial.setClickable(true);
                    fab_log.setClickable(true);
                    fab_contact.setClickable(true);

                    isOpen = true;
                }
            }
        });

        //add number manually
        fab_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = "";
                String number = "";
                openAddNumberPopUp(name, number);

            }
        });

        //add number from call log
        fab_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mViewPager.setCurrentItem(2);

            }
        });

        //add number from contact list
        fab_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, PICK_CONTACTS);
            }
        });
    }

    public void openAddNumberPopUp(final String conName, String conNumber) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(callBlockerHome.this);
        final View mView = getLayoutInflater().inflate(R.layout.popup_window_add_number, null);

        final EditText name = (EditText) mView.findViewById(R.id.nameTxtBox);
        final EditText number = (EditText) mView.findViewById(R.id.numberTxtBox);

        final Switch callSwitch = (Switch) mView.findViewById(R.id.callSwitch);
        final Switch msgSwitch = (Switch) mView.findViewById(R.id.messageSwitch);

        Button addBtn = (Button) mView.findViewById(R.id.add_num_btn);

        name.setText(conName);
        number.setText(conNumber);

        mBuilder.setView(mView);
        final AlertDialog alertDialog = mBuilder.create();
        alertDialog.setCanceledOnTouchOutside(true);

        addBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                boolean callSwitchState = callSwitch.isChecked();
                boolean msgSwitchState = msgSwitch.isChecked();

                if ((!number.getText().toString().isEmpty()) && (callSwitchState == true || msgSwitchState ==true)) {
                    boolean isSuccess = addtoBlockTable(name.getText().toString(), number.getText().toString(), msgSwitchState, callSwitchState);
                    if (isSuccess == true) {

                        alertDialog.dismiss();
                        //dbHelper = new Database_Helper(this.getActivity());

                        List<callBlockerModel> results = GetlistData();
                        ListView lv = (ListView)tab1Fragment.getView().findViewById(R.id.blockedListView);

                        customAdapter adapter=new customAdapter(callBlockerHome.this, results);
                        adapter.setFragment(tab1Fragment);
                        lv.setAdapter(adapter);

                        Toast.makeText(callBlockerHome.this, "Successfully Blocked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(callBlockerHome.this, "Blocking unsuccessfull", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(callBlockerHome.this, "Please fill Empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }

    private List<callBlockerModel> GetlistData(){
        List<callBlockerModel> itemList = new ArrayList<>();

        callBlockerModel listItem;
        Database_Helper dbHelper = new Database_Helper(callBlockerHome.this);

        Cursor mCursor = dbHelper.getDataCallBlocker();

        if(mCursor.getCount() != 0 ) {
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
        }else{
            Toast.makeText(getApplicationContext(),"No data in table",Toast.LENGTH_LONG);
        }
        return itemList;
    }

    private boolean addtoBlockTable(String name, String number, boolean msgState, boolean callState) {

        dbObj.open();

        boolean state = dbObj.insertDataToCallBlocker(name, number, msgState, callState);
        if (state == true) {
            return true;
        }

        dbObj.close();
        return false;
    }

    private void getFromTable() {
        Cursor cursor = dbObj.getDataCallBlocker();

        String array[] = new String[cursor.getCount()];
        int i = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            array[i] = cursor.getString(1);
            i++;
            cursor.moveToNext();
        }

        Toast.makeText(callBlockerHome.this, array.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_CONTACTS:
                    contactPicked(data);
                    break;
            }
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor;
        try {
            String phoneNo;
            String name;

            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            openAddNumberPopUp(name, phoneNo);

        } catch (Exception e) {
            Toast.makeText(this, "Exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_blocker_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_call_blocker_home, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    //Tab1Fragment tab1=new Tab1Fragment();
                    tab1Fragment=new Tab1Fragment();
                    tab1Fragment.setContext(getApplicationContext());
                    return tab1Fragment;
                case 1:
                    Tab2Fragment tab2=new Tab2Fragment();
                    return tab2;
                case 2:
                    Tab3Fragment tab3=new Tab3Fragment();
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Blocked List";
                case 1:
                    return "Blocked History";
                case 2:
                    return "More";
            }
            return null;
        }
    }

}
