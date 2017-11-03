package com.example.kasun.busysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Model.Task;

/**
 * Created by Kasun on 11/16/2016.
 */
public class Database_Helper extends SQLiteOpenHelper {
    //region Columns and information - Call Responder 📞;
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
    //endregion

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

    public Database_Helper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }

    //region Database creation - Common 📢
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Busy_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,TIME_FROM TEXT,TIME_TO TEXT,TYPE TEXT," +
                "DAY TEXT,MSG TEXT,CALL_T TEXT,SMS_T TEXT,ACTIVATION TEXT)");
        db.execSQL(TASK_SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        db.execSQL(TASK_SQL_DROP_ENTRIES);
        onCreate(db);
    }
    //endregion

    //region Operations - Call Responder 📞
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
