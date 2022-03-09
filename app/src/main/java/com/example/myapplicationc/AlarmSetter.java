package com.example.myapplicationc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class AlarmSetter {
    Context context;
    AlarmSetter(Context context){
        this.context=context;
    }
    public boolean setAlarm(String text, String date, String time,int id,String name,int NotificationID) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram
        Intent intent = new Intent(context.getApplicationContext(), MyNotificationPublisher.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("u_id", id);
        intent.putExtra("u_name", name);
        intent.putExtra("NotificationID",NotificationID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Log.d("Tag","Alarm Set");
            return true;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean CancelAlarm(String text, String date, String time,int id,String name,int NotificationID) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram

        Intent intent = new Intent(context.getApplicationContext(), MyNotificationPublisher.class);
        intent.putExtra("event", text);                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("u_id", id);
        intent.putExtra("u_name", name);
        intent.putExtra("NotificationID",NotificationID);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.cancel(pendingIntent);
            Log.d("Tag","Alarm Cancel");
            return true;

        } catch (ParseException e) {
            Log.d("Tag","Alarm Cancel"+e.toString());
            return false;
        }
    }
    public int CreateNotificationID(int UserID, LocalDateTime dateTime){
        return dateTime.getYear()*100000000+dateTime.getMonthValue()*1000000+dateTime.getDayOfMonth()*10000+dateTime.getHour()*100+dateTime.getMinute();
    }
    public String CreateStringDate(LocalDateTime localDateTime){
        return localDateTime.getDayOfMonth()+"-"+localDateTime.getMonthValue()+"-"+localDateTime.getYear();
    }
    public String CreateStringDate(LocalDate localDate){
        return localDate.getDayOfMonth()+"-"+localDate.getMonthValue()+"-"+localDate.getYear();
    }
    public String CreateStringTime(LocalDateTime localDateTime){
        return localDateTime.getHour()+":"+localDateTime.getMinute();
    }
    public String CreateStringTime(LocalTime localTime){
        return localTime.getHour()+":"+localTime.getMinute();
    }
}
