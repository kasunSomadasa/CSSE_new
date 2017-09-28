package com.example.kasun.busysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Kasun on 11/16/2016.
 */
public class Database_Helper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="BUSY.db";
    public static final String DATABASE_TABLE="Busy_info";
    public static final String COL1="_id";
    public static final String COL2="TIME_FROM";
    public static final String COL3="TIME_TO";
    public static final String COL4="TYPE";
    public static final String COL5="DAY";
    public static final String COL6="MSG";
    public static final String COL7="CALL_T";
    public static final String COL8="SMS_T";
    public static final String COL9="ACTIVATION";
    public static final String[] allcol=new String[] {COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9};

    //call blocker
    public static final String COL_ID = "_id";
    public static final String COL_NUM = "NUMBER";
    public static final String COL_MSG = "MESSAGE";
    public static final String COL_CALL = "CALL";
    private static final String CALL_BLOCKER_DATABASE_TABLE = "callBlockTB";

    private Database_Helper Helper;
    private SQLiteDatabase mainDB;
    public ArrayList<Bundle> result;

    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Busy_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,TIME_FROM TEXT,TIME_TO TEXT,TYPE TEXT," +
                "DAY TEXT,MSG TEXT,CALL_T TEXT,SMS_T TEXT,ACTIVATION TEXT)");
        db.execSQL("CREATE TABLE "+CALL_BLOCKER_DATABASE_TABLE+" ("+ COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+ COL_NUM +
                " TEXT,"+ COL_MSG +" BOOLEAN,"+ COL_CALL +" BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+CALL_BLOCKER_DATABASE_TABLE);
        onCreate(db);
    }

    public boolean insertData(String w1,String w2,String w3,String w4,String w5,String w6,String w7,String w8){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL2,w1);
        cv.put(COL3,w2);
        cv.put(COL4,w3);
        cv.put(COL5,w4);
        cv.put(COL6,w5);
        cv.put(COL7,w6);
        cv.put(COL8,w7);
        cv.put(COL9,w8);
        long result=db.insert(DATABASE_TABLE,null,cv);
        if(result == -1){
            return false;
        }else{
            return true;
        }

    }
    public void open(){
        SQLiteDatabase db=this.getWritableDatabase();
    }

    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+DATABASE_TABLE,null);
        return  c;
    }

    public Cursor getlistofData(){
        SQLiteDatabase db=this.getReadableDatabase();

        String where=null;
        Cursor c=db.query(true,DATABASE_TABLE,allcol,null,null,null,null,null,null);

        if (c != null) {
            c.moveToFirst();
            return  c;
        }else{
            return  null;
        }
    }

    public Integer DeleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();

        return  db.delete(DATABASE_TABLE,"_id =?",new String[]{id});
    }

    public boolean updateData(String t1,String t2,String t3,String t4,String t5,String t6,String t7,String t8,String t9){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COL1,t1);
        cv.put(COL2,t2);
        cv.put(COL3,t3);
        cv.put(COL4,t4);
        cv.put(COL5,t5);
        cv.put(COL6,t6);
        cv.put(COL7,t7);
        cv.put(COL8,t8);
        cv.put(COL9,t9);
        db.update(DATABASE_TABLE,cv,"_id=?",new  String[]{t1});
        return true;
    }

    public Cursor searchData(String key){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+DATABASE_TABLE+" where "+COL4+" like '%"+key+"%'",null);
        return  c;
    }

    /*
     *
     * CallBlocker methods
     *
     */

    public void close(){
        Helper.close();
    }

    public void createEntry(String number, boolean msg, boolean call){
        System.out.println(call);
        ContentValues cv = new ContentValues();
        cv.put(COL_NUM, number);
        cv.put(COL_MSG, msg);
        cv.put(COL_CALL, call);
        mainDB.insert(CALL_BLOCKER_DATABASE_TABLE, null, cv);
    }

    public String getCallBlockerDbTableName(){
        return CALL_BLOCKER_DATABASE_TABLE;
    }

    public String[] CallBlockercolumnName(){
        return new String[]{COL_NUM, COL_MSG, COL_CALL};
    }

    public Cursor getCallBlockerData() {
        String[] columns = new String[]{COL_NUM, COL_MSG, COL_CALL};
        Cursor c = mainDB.query(CALL_BLOCKER_DATABASE_TABLE, columns, null, null, null, null, null);
        return c;
    }

    public void deleteCallBlockerAll(){
        mainDB.delete(CALL_BLOCKER_DATABASE_TABLE, null, null);
    }

    public void DeleteCallBlockerData(String num) {
        //System.out.println(num);
        mainDB.delete(CALL_BLOCKER_DATABASE_TABLE, COL_NUM + "=" + num, null);

    }

    public void EditCallBlockerMSG(String num, Boolean msg) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(COL_NUM, msg);
        mainDB.update(CALL_BLOCKER_DATABASE_TABLE, cvUpdate, COL_NUM + "=" + num, null);
    }

    public void EditCallBlockerCALL(String num, Boolean call) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(COL_NUM, call);
        mainDB.update(CALL_BLOCKER_DATABASE_TABLE, cvUpdate, COL_NUM + "=" + num, null);
    }

}
