package com.example.kasun.busysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kasun on 11/16/2016.
 */
public class Database_Helper extends SQLiteOpenHelper {

    //database
    private static final String DATABASE_NAME="BUSY.db";

    //tables
    private static final String DATABASE_TABLE="Busy_info";
    private static final String CALLBLOCKER_TABLE = "CALLBLOCKER";
    private static final String CALLBLOCKER_HISTORY_TABLE = "CALLBLOCKERHISTORY";
    private static final String CALLBLOCKER_MSG_BLOCK_WORDS_TABLE = "CALLBLOCKERWORDS";
    private static final String CALLRECORDER_TABLE = "CALLRECORDER";
    private static final String CALLBLOCK_TIMES = "CALLBLOCKTIMES";

    //auto sms table columns
    public static final String COL1="_id";
    public static final String COL2="TIME_FROM";
    public static final String COL3="TIME_TO";
    public static final String COL4="TYPE";
    public static final String COL5="DAY";
    public static final String COL6="MSG";
    public static final String COL7="CALL_T";
    public static final String COL8="SMS_T";
    public static final String COL9="ACTIVATION";

    //callblocker table columns
    private static final String COL_NAME = "_name";
    private static final String COL_NUMBER = "_number";
    private static final String COL_MSG = "_msg";
    private static final String COL_CALL = "_call";

    //call blocker history table
    private static final String COL_BLOCKED_NUMBER = "_blockedNumber";
    private static final String COL_BLOCKED_DATE = "_blockedDate";

    //callblocker words to block message table
    private static final String COL_MSG_BLOCK_WORDS = "_word";

    //call recorder table
    private static final String COL_REC_NUMBER = "_recNumber";

    //block time table
    private static final String COL_FROM = "_from";
    private static final String COL_TO = "_to";

    private SQLiteDatabase db;

    public static final String[] allcol=new String[] {COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9};

    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Busy_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,TIME_FROM TEXT,TIME_TO TEXT,TYPE TEXT," +
                "DAY TEXT,MSG TEXT,CALL_T TEXT,SMS_T TEXT,ACTIVATION TEXT)");

        db.execSQL("CREATE TABLE " + CALLBLOCKER_TABLE + " (" +
                COL_NAME + " TEXT, " +
                COL_NUMBER + " TEXT PRIMARY KEY, " +
                COL_MSG + " BOOLEAN, " +
                COL_CALL + " BOOLEAN );"
        );

        db.execSQL("CREATE TABLE " + CALLBLOCKER_HISTORY_TABLE + " (" +
                COL_BLOCKED_NUMBER + " TEXT, " +
                COL_BLOCKED_DATE + " TEXT );"
        );

        db.execSQL("CREATE TABLE " + CALLBLOCKER_MSG_BLOCK_WORDS_TABLE + " (" +
                COL_MSG_BLOCK_WORDS + " TEXT PRIMARY KEY);"
        );

        db.execSQL("CREATE TABLE " + CALLRECORDER_TABLE + " (" +
                COL_REC_NUMBER + " TEXT PRIMARY KEY);"
        );

        db.execSQL("CREATE TABLE " + CALLBLOCK_TIMES + " (" +
                COL_FROM + " TEXT, "+
                COL_TO + " TEXT );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCKER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCKER_HISTORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCKER_MSG_BLOCK_WORDS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLRECORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCK_TIMES);
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
        return db.rawQuery("select * from "+DATABASE_TABLE,null);
    }

    public Cursor getlistofData(){
        SQLiteDatabase db=this.getReadableDatabase();

        String where=null;
        Cursor c=db.query(true,DATABASE_TABLE,allcol,null,null,null,null,null,null);

        if (c != null) {
            c.moveToFirst();
            return  c;
        } else {
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
        return db.rawQuery("select * from "+DATABASE_TABLE+" where "+COL4+" like '%"+key+"%'",null);
    }

    /**
     *
     ***************************
     * call blocker methods
     ***************************
     *
     */

    //call blocker table methods
    public boolean insertDataToCallBlocker(String w1, String w2, boolean w3, boolean w4) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_NAME, w1);
        cv.put(COL_NUMBER, w2);
        cv.put(COL_MSG, w3);
        cv.put(COL_CALL, w4);

        long result = db.insert(CALLBLOCKER_TABLE, null, cv);

        return result != -1;
    }

    public Cursor getDataCallBlocker() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from " + CALLBLOCKER_TABLE, null);
    }

    public boolean isBlocked(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from " + CALLBLOCKER_TABLE + " where "+ COL_NUMBER +" = '"+number+"'", null);

        if (c != null) {
            c.moveToFirst();
            return true;
        } else {
            return false;
        }
    }

    public void DeleteDataCallBlocker(String num) {
        db = this.getWritableDatabase();
        db.delete(CALLBLOCKER_TABLE, COL_NUMBER + "='" + num+"'", null);
    }

    public void EditMSG(String num, boolean msg) {
        db = this.getWritableDatabase();
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(COL_MSG, msg);
        db.update(CALLBLOCKER_TABLE, cvUpdate, COL_NUMBER + "='" + num+"'", null);
    }

    public void EditCALL(String num, boolean call) {
        db = this.getWritableDatabase();
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(COL_CALL, call);
        db.update(CALLBLOCKER_TABLE, cvUpdate, COL_NUMBER + "='" + num+"'", null);
    }

    public String[] columnName() {
        return new String[]{COL_NAME, COL_NUMBER, COL_MSG, COL_CALL};
    }

    //call blocker history table methods
    public boolean insertDataToCallBlockerHistory(String w1, String w2) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_BLOCKED_NUMBER, w1);
        cv.put(COL_BLOCKED_DATE, w2);

        long result = db.insert(CALLBLOCKER_HISTORY_TABLE, null, cv);

        return result != -1;
    }

    public Cursor getDataCallBlockerHistory() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+CALLBLOCKER_HISTORY_TABLE,null);
        return  c;
    }

    //call blocker words table methods
    public boolean insertDataToCallBlockerWords(String w1) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_MSG_BLOCK_WORDS, w1);
        long result = db.insert(CALLBLOCKER_MSG_BLOCK_WORDS_TABLE, null, cv);

        return result != -1;
    }

    public Cursor getDataCallBlockerWords() {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+CALLBLOCKER_MSG_BLOCK_WORDS_TABLE,null);
        return  c;
    }

    //insert to call recorder
    public boolean insertDataToCallRecorder(String w1) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_REC_NUMBER, w1);
        long result = db.insert(CALLRECORDER_TABLE, null, cv);

        return result != -1;
    }

    public Cursor getRecordNumbers(String w1){
        db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from "+CALLBLOCKER_MSG_BLOCK_WORDS_TABLE,null);
        return  c;
    }

    //call blocker times
    public boolean insertCallBlockTimes(String w1,String w2) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_FROM, w1);
        cv.put(COL_TO, w2);
        long result = db.insert(CALLBLOCK_TIMES, null, cv);

        return result != -1;
    }

    public Cursor getCallBlockTimes(){
        db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from "+CALLBLOCK_TIMES,null);
        return  c;
    }

    public boolean DeleteTime(String from,String to) {
        db = this.getWritableDatabase();
        long result = db.delete(CALLBLOCK_TIMES, COL_FROM + " ='" + from +"' and "+COL_TO + " ='" + to + "'", null);

        return result != -1;
    }

}
