package com.example.kasun.busysms;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasun.busysms.callBlocker.AddActivity;
import com.example.kasun.busysms.callBlocker.callBlockerDAO;

import java.util.ArrayList;

public class blockerHome extends AppCompatActivity {

    private static final int CONTACT_PICKER_RESULT = 1001;

    String phone;
    String finalNum;
    ArrayList<String> numbers;
    private ArrayList<callBlockerDAO> items = null;
    Database_Helper DC;
    ListView BlockList;
    boolean cursorFlag = false;
    Cursor c;
    ListAdapter numberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocker_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BlockList = (ListView) this.findViewById(R.id.list_item_iterator);
        loadData();
    }

    private void loadData() {
        try {
            DC = new Database_Helper(blockerHome.this);
            DC.open();
            Cursor c = DC.getData();
            int iNum = c.getColumnIndex(DC.CallBlockercolumnName()[0]);
            int iMsg = c.getColumnIndex(DC.CallBlockercolumnName()[1]);
            int iCall = c.getColumnIndex(DC.CallBlockercolumnName()[2]);
            numbers = new ArrayList<>();
            items = new ArrayList<callBlockerDAO>();
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                items.add(new callBlockerDAO(c.getString(iNum), c.getString(iMsg).equals("1"), c.getString(iCall).equals("1")));
                numbers.add(c.getString(iNum));
            }
            DC.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }

        numberList = new ListAdapter();
        BlockList.setAdapter(numberList);
    }

    //number selections types
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        SubMenu submenu1 = menu.addSubMenu("");
        submenu1.setIcon(R.drawable.ic_add_circle);

        submenu1.add(1, 3, 2, "Add from call logs");
        submenu1.add(1, 1, 3, "Add from contacts");
        submenu1.add(1, 2, 4, "Add new");
        submenu1.getItem().setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == 1) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, CONTACT_PICKER_RESULT);
        }

        if (item.getItemId() == 3) {
            String[] callLogFields = {
                    android.provider.CallLog.Calls._ID,
                    android.provider.CallLog.Calls.NUMBER,
                    android.provider.CallLog.Calls.CACHED_NAME
            };
            String viaOrder = android.provider.CallLog.Calls.DATE + " DESC";

            final Cursor callLog_cursor = this.getContentResolver().query(
                    android.provider.CallLog.Calls.CONTENT_URI, callLogFields,
                    null, null, viaOrder);

            AlertDialog.Builder myversionOfCallLog = new AlertDialog.Builder(this);
            android.content.DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int item) {
                    callLog_cursor.moveToPosition(item);
                    String numFromLog = callLog_cursor.getString(callLog_cursor
                            .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                    if (numFromLog.contains("+")) {
                        String[] parts = numFromLog.split("\\+");
                        finalNum = parts[1];
                    } else
                        finalNum = numFromLog;
                    if(finalNum.charAt(0)=='0') finalNum=finalNum.substring(1);
                    DC = new Database_Helper(blockerHome.this);
                    DC.open();
                    DC.createEntry(finalNum, new Boolean(true), new Boolean(true));
                    DC.close();
                    loadData();
                    callLog_cursor.close();
                }
            };
            myversionOfCallLog.setCursor(callLog_cursor, listener, CallLog.Calls.NUMBER);
            myversionOfCallLog.setTitle("Choose from Call Log");
            myversionOfCallLog.create().show();
        }

        if (item.getItemId() == 2) {
            Intent foo = new Intent(this, AddActivity.class);
            this.startActivityForResult(foo, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER_RESULT:
                    try {
                        c = managedQuery(data.getData(), null, null, null, null);
                        cursorFlag = true;
                        if (c.moveToFirst()) {
                            phone = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        if (phone.contains("+")) {
                            String[] parts = phone.split("\\+");
                            phone = parts[1];
                        }
                        if(phone.charAt(0)=='0') phone = phone.substring(1);
                        DC = new Database_Helper(blockerHome.this);
                        DC.open();
                        DC.createEntry(phone, new Boolean(true), new Boolean(true));
                        DC.close();
                        loadData();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;

                case 1:
                    try {
                        String value = data.getStringExtra("value");
                        if (value != null && value.length() > 0 && value.matches("[0-9]+")) {
                            if (value.charAt(0) == '0') value = value.substring(1);
                            DC = new Database_Helper(blockerHome.this);
                            DC.open();
                            DC.createEntry(value, new Boolean(true), new Boolean(true));
                            DC.close();
                            loadData();
                        } else
                            Toast.makeText(getBaseContext(), "Number cant containt extra charachter", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater = null;

        public ListAdapter() {
            this.inflater = blockerHome.this.getLayoutInflater();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.activity_callblock_blocked_number, null);

            callBlockerDAO item = items.get(position);
            TextView question = (TextView) convertView.findViewById(R.id.number);
            question.setText(item.getNumber());

            final CheckBox chkMsg = (CheckBox) convertView.findViewById(R.id.chkMsg);
            final CheckBox chkCall = (CheckBox) convertView.findViewById(R.id.chkCall);
            final ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.btnDelete);
            chkMsg.setChecked(item.getMsg());
            chkCall.setChecked(item.getCall());
            chkMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DC = new Database_Helper(blockerHome.this);
                    DC.open();
                    DC.EditCallBlockerMSG(numbers.get(position), isChecked);
                }

            });

            chkCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DC = new Database_Helper(blockerHome.this);
                    DC.open();
                    DC.EditCallBlockerCALL(numbers.get(position), isChecked);
                }

            });

            deleteBtn.setOnClickListener(new CompoundButton.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DC = new Database_Helper(blockerHome.this);
                    DC.open();
                    String num = numbers.get(position);
                    DC.DeleteData(num);
                    DC.close();
                    loadData();
                }
            });


            return convertView;
        }
    }

    public void onResume() {
        super.onResume();
        loadData();
    }

    public void onDestroy(){
        super.onDestroy();
        if(cursorFlag == true)
            c.close();
    }


}
