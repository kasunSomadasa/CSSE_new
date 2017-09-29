package com.example.kasun.busysms.taskCalendar.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kasun.busysms.taskCalendar.Helper.DateEx;
import com.example.kasun.busysms.taskCalendar.Model.Task;


/**
 * Created by Nishan on 9/27/2017.
 * @version 3.0
 * @author Nishan
 */

public class TaskDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Tasks.db";
    private static final String TABLE_NAME = "Tasks";
    private static final String COL_1 = "task_id";
    private static final String COL_2 = "task_name";
    private static final String COL_3 = "task_description";
    private static final String COL_4 = "task_date";
    private static final String COL_5 = "task_participants";
    private static final String COL_6 = "task_start";
    private static final String COL_7 = "task_end";
    private static final String COL_8 = "task_location";
    private static final String COL_9 = "is_all_day_task";
    private static final String COL_10 = "task_notification_time";
    private static final String COL_11 = "notification_sound";

    private static final String SQL_CREATE_ENTRIES = "create table "+TABLE_NAME+" (" +
            COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_2+" TEXT, " +
            COL_3+" TEXT, " +
            COL_4+" DATETIME, " +
            COL_5+" TEXT, " +
            COL_6+" DATETIME, " +
            COL_7+" DATETIME, " +
            COL_8+" TEXT, " +
            COL_9+" BOOL, " +
            COL_10+" DATETIME, " +
            COL_11+" TEXT)";
    private static final String SQL_DROP_ENTRIES = "drop table if exists "+TABLE_NAME;


    public TaskDB(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_ENTRIES);
    }

    public boolean insert(Task task){
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

        long insertedResult = db.insert(TABLE_NAME, null, contentValues);
        if(insertedResult == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean update(Task newTask, int oldTaskId){
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
        String selection = COL_1 + " = ?";
        String[] selectionArgs = {String.valueOf(oldTaskId)};
        long updateResult = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
        if(updateResult == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean delete(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COL_1 + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};
        long deleteResult = db.delete(TABLE_NAME, selection, selectionArgs);
        if(deleteResult == -1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor selectAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COL_1,
                COL_2,
                COL_3,
                COL_4,
                COL_5,
                COL_6,
                COL_7,
                COL_8,
                COL_9,
                COL_10,
                COL_11
        };
        return db.query(TABLE_NAME, projection, null, null, null, null, null);
    }


    //Date format - yyyy-MM-dd HH:mm:ss
    public Cursor selectByDate(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COL_1,
                COL_2,
                COL_3,
                COL_4,
                COL_5,
                COL_6,
                COL_7,
                COL_8,
                COL_9,
                COL_10,
                COL_11
        };
        String selection = "task_date like ?";
        String[] selectionArgs = {date+"%"};
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor selectBetweenDate(String date1, String date2){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COL_1,
                COL_2,
                COL_3,
                COL_4,
                COL_5,
                COL_6,
                COL_7,
                COL_8,
                COL_9,
                COL_10,
                COL_11
        };
        String selection = "task_date between ? and ?";
        String[] selectionArgs = {date1, date2};
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }

    public Cursor selectByTaskId(int taskId){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {
                COL_1,
                COL_2,
                COL_3,
                COL_4,
                COL_5,
                COL_6,
                COL_7,
                COL_8,
                COL_9,
                COL_10,
                COL_11
        };
        String selection = "task_id = ?";
        String[] selectionArgs = {String.valueOf(taskId)};
        return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    }
}
