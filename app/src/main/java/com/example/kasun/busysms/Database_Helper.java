package com.example.kasun.busysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kasun
 * Class which handle all database operation
 */
public class Database_Helper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="BUSY.db";//database name
    public static final String DATABASE_TABLE="Busy_info";//database table which belongs to autoSMS function
    public static final String COL1="_id";
    public static final String COL2="TIME_FROM";
    public static final String COL3="TIME_TO";
    public static final String COL4="TYPE";
    public static final String COL5="DAY";
    public static final String COL6="MSG";
    public static final String COL7="CALL_T";
    public static final String COL8="SMS_T";
    public static final String COL9="ACTIVATION";
    public static final String[] allColumn=new String[] {COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9};

    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, 3);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database table which belongs to autoSMS function
        db.execSQL("CREATE TABLE Busy_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,TIME_FROM TEXT,TIME_TO TEXT,TYPE TEXT," +
                "DAY TEXT,MSG TEXT,CALL_T TEXT,SMS_T TEXT,ACTIVATION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete table when app is uninstall
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);
    }

    public boolean insertData(String from,String to,String type,String day,String msg,String call,String sms,String active){
        //data insert query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL2,from);
        cv.put(COL3,to);
        cv.put(COL4,type);
        cv.put(COL5,day);
        cv.put(COL6,msg);
        cv.put(COL7,call);
        cv.put(COL8,sms);
        cv.put(COL9,active);
        long result=db.insert(DATABASE_TABLE,null,cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }

    }
    public void open(){
        //open database connection
        SQLiteDatabase db=this.getWritableDatabase();
    }


    public Cursor getData(){
        //get data query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+DATABASE_TABLE,null);
        return  c;
    }

    public Integer deleteData(String id){
        //delete data query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        return  db.delete(DATABASE_TABLE,"_id =?",new String[]{id});
    }

    public boolean updateData(String id,String from,String to,String type,String day,String msg,String call,String sms,String active){
        //data update query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL1,id);
        cv.put(COL2,from);
        cv.put(COL3,to);
        cv.put(COL4,type);
        cv.put(COL5,day);
        cv.put(COL6,msg);
        cv.put(COL7,call);
        cv.put(COL8,sms);
        cv.put(COL9,active);
        db.update(DATABASE_TABLE,cv,"_id=?",new  String[]{id});
        return true;
    }

    public Cursor getListOfData(){
        //get data query belongs to autoSMS function for display in listView
        SQLiteDatabase db=this.getReadableDatabase();

        String where=null;
        Cursor c=db.query(true,DATABASE_TABLE,allColumn,null,null,null,null,null,null);

        if (c != null) {
            c.moveToFirst();
            return  c;
        }else{
            return  null;
        }

    }
    public Cursor searchData(String key){
        //data search query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+DATABASE_TABLE+" where "+COL4+" like '%"+key+"%'",null);
        return  c;
    }
}
