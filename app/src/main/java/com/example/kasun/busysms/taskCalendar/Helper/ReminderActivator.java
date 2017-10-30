package com.example.kasun.busysms.taskCalendar.Helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.kasun.busysms.taskCalendar.Model.Task;
import com.example.kasun.busysms.taskCalendar.ReminderActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Nishan on 10/27/2017.
 * @author Nishan
 * @version 1.0
 */

public abstract class ReminderActivator {

    public static void runActivator(Context context){
        List<Task> tasks = Task.getAllTasks(context);
        for (Task task : tasks) {
            Intent intent = new Intent(context, NotificationReciever.class);
            intent.putExtra("ui", new Intent(context, ReminderActivity.class));
            intent.putExtra("task", task);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, task.getTask_notification_time().getTime(), pendingIntent);
        }
    }

    public static void runActivator(Context context, Task task){
        Intent intent = new Intent(context, NotificationReciever.class);
        intent.putExtra("ui", new Intent(context, ReminderActivity.class));
        intent.putExtra("task", task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        System.out.println("Test notification time "+task.getTask_notification_time().toString());
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getTask_notification_time().getTime(), pendingIntent);
    }

    public static void postponeReminder(Context context, Task task, int minutes){
        Intent intent = new Intent(context, NotificationReciever.class);
        intent.putExtra("ui", new Intent(context, ReminderActivity.class));
        intent.putExtra("task", task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public static void changeReminder(Context context, Task task, int minutes){
        Intent intent = new Intent(context, NotificationReciever.class);
        intent.putExtra("ui", new Intent(context, ReminderActivity.class));
        intent.putExtra("task", task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


    public static void suspendReminder(Context context, Task task){
        Intent intent = new Intent(context, NotificationReciever.class);
        intent.putExtra("ui", new Intent(context, ReminderActivity.class));
        intent.putExtra("task", task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
