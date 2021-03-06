package com.example.kasun.busysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Model.Task;

/**
 * Class which handle all database operation
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    //database name
    public static final String DATABASE_NAME="BUSY.db";
    //database table which belongs to autoSMS function
    public static final String DATABASE_TABLE="Busy_info";

    //database table which belongs to call blocker function
    private static final String CALLBLOCKER_TABLE = "CALLBLOCKER";
    private static final String CALLBLOCKER_HISTORY_TABLE = "CALLBLOCKERHISTORY";
    private static final String CALLBLOCKER_MSG_BLOCK_WORDS_TABLE = "CALLBLOCKERWORDS";
    private static final String CALLRECORDER_TABLE = "CALLRECORDER";
    private static final String CALLBLOCK_TIMES = "CALLBLOCKTIMES";

    //auto sms columns
    public static final String COL1="_id";
    public static final String COL2="TIME_FROM";
    public static final String COL3="TIME_TO";
    public static final String COL4="TYPE";
    public static final String COL5="DAY";
    public static final String COL6="MSG";
    public static final String COL7="CALL_T";
    public static final String COL8="SMS_T";
    public static final String COL9="ACTIVATION";

    //    alarm table
    public static final String DATABASE_TABLE_ALARM="alarm_info";
    public static final String ALARM_ID_COL="_id";
    public static final String ALARM_TIME_COL ="ALARM_TIME";
    public static final String ALARM_REPEAT_COL="ALARM_REPEAT";
    public static final String ALARM_SOUND_COL="ALARM_SOUND";
    public static final String ALARM_SOUND_CHK="SOUND_CHK";
    public static final String ALARM_VOLUME_COL="ALARM_VOL";
    public static final String ALARM_SILENT_COL="ALARM_SILENT";
    public static final String[] allcolumns=new String[] {ALARM_ID_COL,ALARM_TIME_COL,ALARM_REPEAT_COL,ALARM_SOUND_COL,
            ALARM_SOUND_CHK,ALARM_VOLUME_COL,ALARM_SILENT_COL};

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

    //region Columns and information - Task Calendar 📆
    private static final String TASK_TABLE_NAME = "Tasks";
    private static final String TASK_COL_1 = "task_id";
    private static final String TASK_COL_2 = "task_name";
    private static final String TASK_COL_3 = "task_description";
    private static final String TASK_COL_4 = "task_date";
    private static final String TASK_COL_5 = "task_participants";
    private static final String TASK_COL_6 = "task_start";
    private static final String TASK_COL_7 = "task_end";
    private static final String TASK_COL_8 = "task_location";
    private static final String TASK_COL_9 = "is_all_day_task";
    private static final String TASK_COL_10 = "task_notification_time";
    private static final String TASK_COL_11 = "notification_sound";

    private static final String TASK_SQL_CREATE_ENTRIES = "create table "+ TASK_TABLE_NAME +" (" +
            TASK_COL_1 +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASK_COL_2 +" TEXT, " +
            TASK_COL_3 +" TEXT, " +
            TASK_COL_4 +" DATETIME, " +
            TASK_COL_5 +" TEXT, " +
            TASK_COL_6 +" DATETIME, " +
            TASK_COL_7 +" DATETIME, " +
            TASK_COL_8 +" TEXT, " +
            TASK_COL_9 +" BOOL, " +
            TASK_COL_10 +" DATETIME, " +
            TASK_COL_11 +" TEXT)";
    private static final String TASK_SQL_DROP_ENTRIES = "drop table if exists "+ TASK_TABLE_NAME;
    //endregion


    public static final String[] allColumn=new String[] {COL1,COL2,COL3,COL4,COL5,COL6,COL7,COL8,COL9};


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);

    }

    //region Database creation - Common 📢
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database table which belongs to autoSMS function
        db.execSQL("CREATE TABLE Busy_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,TIME_FROM TEXT,TIME_TO TEXT,TYPE TEXT," +
                "DAY TEXT,MSG TEXT,CALL_T TEXT,SMS_T TEXT,ACTIVATION TEXT)");


//        create alarm_info table
        db.execSQL("CREATE TABLE alarm_info (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "                                ALARM_TIME TEXT," +
                "                                ALARM_REPEAT TEXT," +
                "                                ALARM_SOUND TEXT," +
                "                                SOUND_CHK TEXT," +
                "                                ALARM_VOL TEXT," +
                "                                ALARM_SILENT TEXT)");


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

        db.execSQL(TASK_SQL_CREATE_ENTRIES);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      
        //delete all tables when app is uninstall
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        //        check db is create is or not
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_ALARM);
        db.execSQL(TASK_SQL_DROP_ENTRIES);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCKER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCKER_HISTORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCKER_MSG_BLOCK_WORDS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLRECORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALLBLOCK_TIMES);
        onCreate(db);
    }
    //endregion


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

    //insert alarm data
    public boolean insertAlarmData(String time,String repeat,String sound,String sound_check,String volume,String silent){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(ALARM_TIME_COL,time);
        cv.put(ALARM_REPEAT_COL,repeat);
        cv.put(ALARM_SOUND_COL,sound);
        cv.put(ALARM_SOUND_CHK,sound_check);
        cv.put(ALARM_VOLUME_COL,volume);
        cv.put(ALARM_SILENT_COL,silent);
        long result=db.insert(DATABASE_TABLE_ALARM,null,cv);
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


    //get alarm data
    public Cursor getAlarmDataList(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+DATABASE_TABLE_ALARM,null);
        return  cursor;
    }

    public Cursor getAlarmData(){
        SQLiteDatabase db=this.getReadableDatabase();

        String where=null;
        Cursor cursor = db.query(true,DATABASE_TABLE_ALARM,allcolumns,null,null,null,null,null,null);

        if (cursor != null) {
            cursor.moveToFirst();
            return  cursor;
        }else{
            return  null;
        }

    }


    public Cursor getData(){
        //get data query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("select * from "+DATABASE_TABLE,null);
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
        } else {
            return  null;
        }
    }
    public Cursor searchData(String key){
        //data search query belongs to autoSMS function
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select * from "+DATABASE_TABLE+" where "+COL4+" like '%"+key+"%'",null);
        return  c;
    }
    //endregion


    //region Operations - Task Calendar 📆
    public boolean task_insert(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_name", task.getTask_name());
        contentValues.put("task_location", task.getTask_location());
        contentValues.put("task_date", DateEx.getDateString(task.getTask_date()));
        contentValues.put("task_description", task.getTask_description());
        contentValues.put("task_participants", task.getTask_participants());
        contentValues.put("task_start", DateEx.getTimeString(task.getTask_start()));
        contentValues.put("task_end", DateEx.getTimeString(task.getTask_end()));
        contentValues.put("task_location", task.getTask_location());
        contentValues.put("is_all_day_task", task.is_all_day_task());
        contentValues.put("task_notification_time", DateEx.getDateTimeString(task.getTask_notification_time()));
        contentValues.put("notification_sound", task.getTask_notification_sound());

        long insertedResult = db.insert(TASK_TABLE_NAME, null, contentValues);
        if(insertedResult == -1){
            return false;
        }else{
            return true;
        }
    }


    public boolean task_update(Task newTask, int oldTaskId){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task_name", newTask.getTask_name());
        contentValues.put("task_location", newTask.getTask_location());
        contentValues.put("task_date", DateEx.getDateString(newTask.getTask_date()));
        contentValues.put("task_description", newTask.getTask_description());
        contentValues.put("task_participants", newTask.getTask_participants());
        contentValues.put("task_start", DateEx.getTimeString(newTask.getTask_start()));
        contentValues.put("task_end", DateEx.getTimeString(newTask.getTask_end()));
        contentValues.put("task_location", newTask.getTask_location());
        contentValues.put("is_all_day_task", newTask.is_all_day_task());
        contentValues.put("task_notification_time", DateEx.getDateTimeString(newTask.getTask_notification_time()));
        contentValues.put("notification_sound", newTask.getTask_notification_sound());
        String selection = TASK_COL_1 + " = ?";
        String[] selectionArgs = {String.valueOf(oldTaskId)};
        long updateResult = db.update(TASK_TABLE_NAME, contentValues, selection, selectionArgs);
        if(updateResult == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean task_delete(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = TASK_COL_1 + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};
        long deleteResult = db.delete(TASK_TABLE_NAME, selection, selectionArgs);
        if(deleteResult == -1){
            return false;
        }else{
            return true;
        }
    }


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
        Cursor c=db.rawQuery("select * from "+CALLRECORDER_TABLE,null);
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


    public Cursor task_selectAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                TASK_COL_1,
                TASK_COL_2,
                TASK_COL_3,
                TASK_COL_4,
                TASK_COL_5,
                TASK_COL_6,
                TASK_COL_7,
                TASK_COL_8,
                TASK_COL_9,
                TASK_COL_10,
                TASK_COL_11
        };
        return db.query(TASK_TABLE_NAME, projection, null, null, null, null, null);
    }


    //Date format - yyyy-MM-dd HH:mm:ss
    public Cursor task_selectByDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                TASK_COL_1,
                TASK_COL_2,
                TASK_COL_3,
                TASK_COL_4,
                TASK_COL_5,
                TASK_COL_6,
                TASK_COL_7,
                TASK_COL_8,
                TASK_COL_9,
                TASK_COL_10,
                TASK_COL_11
        };
        String selection = "task_date like ?";
        String[] selectionArgs = {date+"%"};
        return db.query(TASK_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor task_selectBetweenDate(String date1, String date2){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                TASK_COL_1,
                TASK_COL_2,
                TASK_COL_3,
                TASK_COL_4,
                TASK_COL_5,
                TASK_COL_6,
                TASK_COL_7,
                TASK_COL_8,
                TASK_COL_9,
                TASK_COL_10,
                TASK_COL_11
        };
        String selection = "task_date between ? and ?";
        String[] selectionArgs = {date1, date2};
        return db.query(TASK_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor task_selectByTaskId(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                TASK_COL_1,
                TASK_COL_2,
                TASK_COL_3,
                TASK_COL_4,
                TASK_COL_5,
                TASK_COL_6,
                TASK_COL_7,
                TASK_COL_8,
                TASK_COL_9,
                TASK_COL_10,
                TASK_COL_11
        };
        String selection = "task_id = ?";
        String[] selectionArgs = {String.valueOf(taskId)};
        return db.query(TASK_TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }
    //endregion

}
