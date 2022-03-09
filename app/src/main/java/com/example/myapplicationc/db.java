package com.example.myapplicationc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.IntentCompat;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class db extends SQLiteOpenHelper {
    private static final String DB_NAME = "db";
    private static final int DB_VERSION = 1;

    private static final String Table_name = "User";
    private static final String UserUpdatedBalanceTable = "UserUpdatedBalanceTable";
    private static final String UserHistory = "UserHistory";
    private static final String UserPayAndNotification= "UserPayAndNotification";

    private static final String temp_Table_name = "temp_User";
    private static final String temp_UserUpdatedBalanceTable = "temp_UserUpdatedBalanceTable";
    private static final String temp_UserHistory = "temp_UserHistory";
    private static final String temp_UserPayAndNotification= "temp_UserPayAndNotification";

    private static final String U_id = "U_id";
    private static final String N_u_id = "N_u_id";
    private static final String U_name = "U_name";
    private static final String U_mobile_no = "U_mobile_no";
    private static final String DayOrMonth = "DayOrMonth";
    private static final String StartAmount = "StartAmount";
    private static final String UpdateAmount = "UpdateAmount";
    private static final String ExchangeAmount = "ExchangeAmount";
    private static final String U_Rate = "U_Rate";
    private static final String U_Attachment_link = "U_Attacment_link";
    private static final String IsActiveUser = "IsActiveUser";
    private static final String Return_time = "Return_time";
    private static final String StartDate = "StartDate";
    private static final String DueDate = "DueDate";
    private static final String PayDate = "PayDate";
    private static final String ispaid = "ispaid";
    private static final String ExchangeDate = "ExchangeDate";
    private static final String Day_Before_DueDate = "Day_Before_DueDate";
    private static final String Text_Data = "Text_Data";
    private static Context context;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String Create_table = "Create table "+Table_name+"("+
            U_id + " Integer , "+
            U_name + " Varchar(20) ,"+
            U_mobile_no + " Varchar(10) ,"+
            DayOrMonth + " Integer ,"+
            StartAmount + " Decimal(10,2), "+
            U_Rate + " Decimal(5,2), "+
            U_Attachment_link + " Text ," +
            StartDate + " datetime , " +
            Return_time + " Integer ,"+
            IsActiveUser + " Integer );" ;

    private static final String Create_table_update_balance = "Create table "+UserUpdatedBalanceTable+"("+
            U_id+" Integer , "+
            DayOrMonth+ " Integer ,"+
            UpdateAmount + " Decimal(10,2), "+
            DueDate + " datetime ," +
            Day_Before_DueDate + " datetime ," +
            U_Rate + " Decimal(5,2) );" ;

    private static final String Create_Table_User_History = "Create table "+UserHistory+"("+
            U_id+" Integer , "+
            ExchangeAmount + " Decimal(10,2), "+
            ExchangeDate + " datetime ," +
            U_Attachment_link + " Text ," +
            Text_Data + " Text );" ;

    private static final String Create_Table_User_pay_and_notification = "Create table "+UserPayAndNotification+"("+
            U_id+" Integer , "+
            N_u_id+" Integer , "+
            PayDate + " datetime ," +
            ispaid+" Integer );" ;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalTime due_time=LocalTime.of(8,0);

    public db(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL(Create_table);
            sqLiteDatabase.execSQL(Create_table_update_balance);
            sqLiteDatabase.execSQL(Create_Table_User_History);
            sqLiteDatabase.execSQL(Create_Table_User_pay_and_notification);
            Log.d("Tag create table:","Done");
        }
        catch (Exception exception){
            Log.d("Tag create table:",""+exception.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){ }

    public boolean insertUserTable(int userId, String nameS, String mobileno, double Amount, Double rate, int dailyOrMonthly,
                                   String Attachment_link,int return_time,int isActiveUser,String startDate){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, userId);
            values.put(U_name, nameS);
            values.put(U_mobile_no, mobileno);
            values.put(DayOrMonth, dailyOrMonthly);
            values.put(StartAmount, Amount);
            values.put(U_Rate, rate);
            values.put(U_Attachment_link, Attachment_link);
            values.put(IsActiveUser, isActiveUser);
            values.put(Return_time, return_time);
            values.put(StartDate, startDate);
            db.insert(Table_name,null,values);
            Log.d("Tag","Done:insertUserTable ");
            db.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error:insertUserTable "+e.toString());
            return false;
        }
    }

    public boolean insertUserPayAndNotificationTable(int Id,int IsPaid,String payDate,int n_u_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, Id);
            values.put(N_u_id,n_u_id);
            values.put(ispaid, IsPaid);
            values.put(PayDate, payDate);
            db.insert(UserPayAndNotification,null,values);
            db.close();
            Log.d("Tag","Done:insertUserPayAndNotificationTable");
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error:insertUserPayAndNotificationTable"+e.toString());
            return false;
        }
    }

    public boolean insertUserUpdatedBalanceTable(int Id,int dailyOrMonthly,Double Amount,String dueDate,String day_Before_DueDate,Double rate){
     try{
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(U_id, Id);
         values.put(DayOrMonth, dailyOrMonthly);
         values.put(UpdateAmount, Amount);
         values.put(DueDate, dueDate);
         values.put(Day_Before_DueDate, day_Before_DueDate);
         values.put(U_Rate, rate);
         db.insert(UserUpdatedBalanceTable,null,values);
         db.close();
         Log.d("Tag","Done:insertUserUpdatedBalanceTable");
         return true;
     }
     catch (Exception e){
         Log.d("Tag","Error:insertUserUpdatedBalanceTable"+e.toString());
         return false;
     }
    }

    public static void printError(){
        Toast.makeText(context.getApplicationContext(),"Something wrong try again",Toast.LENGTH_SHORT).show();
    }

    public void addA(int userId, String nameS, String mobilenoS, double Amount, Double rate,
                     int dailyOrMonthly,String Attachment_link,Double firstMonthAmount,int return_time) {
        Date currentTime = Calendar.getInstance().getTime();
        LocalDate Date_now=LocalDate.of(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate());
        LocalTime Time_now=LocalTime.now();
        if(
            new db(context.getApplicationContext()).insertUserTable(userId, nameS, mobilenoS, Amount, rate,dailyOrMonthly,
                    Attachment_link, return_time,1, new db(context.getApplicationContext()).CreateSimpleDateFormat(Date_now,Time_now))
        ){
            Log.d("Tag","insertUserTable Done");
        }
        else{
            printError();
            return;
        }

        //0= day 1=month
        if(dailyOrMonthly==1){
            LocalDate nexMonth_DueDay=Date_now.plusMonths(1);
            LocalDate nexMonth_Day_Before_DueDay=nexMonth_DueDay.minusDays(1);
            if(
                new db(context.getApplicationContext()).insertUserUpdatedBalanceTable(userId, dailyOrMonthly, Amount,
                    new db(context.getApplicationContext()).CreateSimpleDateFormat(nexMonth_DueDay,due_time),
                    new db(context.getApplicationContext()).CreateSimpleDateFormat(nexMonth_Day_Before_DueDay,due_time),rate)
            ){
                Log.d("Tag","insertUserUpdatedBalanceTable done");
            }else{
                printError();
                return;
            }
            String string_nexMonth_Day_Before_DueDay=new AlarmSetter(context.getApplicationContext()).CreateStringDate(nexMonth_Day_Before_DueDay);
            String string_nexMonth_DueDay=new AlarmSetter(context.getApplicationContext()).CreateStringDate(nexMonth_DueDay);
            String string_DueTime=new AlarmSetter(context.getApplicationContext()).CreateStringTime(due_time);
            int notificationIdDay=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(userId,LocalDateTime.of(nexMonth_DueDay,due_time));
            int notificationIdDayBefore=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(userId,LocalDateTime.of(nexMonth_Day_Before_DueDay,due_time));
            if(
            new AlarmSetter(context.getApplicationContext()).setAlarm("Tomorrow is Pay time",string_nexMonth_Day_Before_DueDay,
                    string_DueTime,userId,nameS, notificationIdDayBefore)
            &&
            new AlarmSetter(context.getApplicationContext()).setAlarm("Today is Pay time",string_nexMonth_DueDay,
                    string_DueTime,userId,nameS,notificationIdDay)
            ){
                Log.d("Tag","setAlarm Month done");
            }
            else{
                printError();
                return;
            }
            if(
               new db(context.getApplicationContext()).insertUserPayAndNotificationTable(userId, 0,
                  new db(context.getApplicationContext()).CreateSimpleDateFormat(nexMonth_DueDay,due_time), notificationIdDay)
                            &&
               new db(context.getApplicationContext()).insertUserPayAndNotificationTable(userId, 2,
                  new db(context.getApplicationContext()).CreateSimpleDateFormat(nexMonth_Day_Before_DueDay,due_time), notificationIdDayBefore)
            ){
                Log.d("Tag","insertUserPayAndNotificationTable done");
            }
            else{
                printError();
                return;
            }
        }
        else if(dailyOrMonthly==0) {
            LocalDate nextDay_DueDay=Date_now.plusDays(return_time);
            LocalDate next_Day_Before_DueDay=nextDay_DueDay.minusDays(1);
            Double _Amount_after_return_time=Amount;
            for (int i=0;i<return_time;i++){
                Double _Amount_toAdd = (_Amount_after_return_time/100) * rate;
                _Amount_after_return_time = _Amount_after_return_time + _Amount_toAdd;
            }
            if(
            new db(context.getApplicationContext()).insertUserUpdatedBalanceTable(userId, dailyOrMonthly, _Amount_after_return_time,
                    new db(context.getApplicationContext()).CreateSimpleDateFormat(nextDay_DueDay,due_time),
                    new db(context.getApplicationContext()).CreateSimpleDateFormat(next_Day_Before_DueDay,due_time),rate)
            ){
                Log.d("Tag","setAlarm day done");
            }
            else{
                printError();
                return;
            }

            String string_next_Day_Before_DueDay=new AlarmSetter(context.getApplicationContext()).CreateStringDate(next_Day_Before_DueDay);
            String string_nextDay_DueDay=new AlarmSetter(context.getApplicationContext()).CreateStringDate(nextDay_DueDay);
            String string_DueTime=new AlarmSetter(context.getApplicationContext()).CreateStringTime(due_time);
            int notificationIdDayBefore_day=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(userId,LocalDateTime.of(next_Day_Before_DueDay,due_time));
            int notificationIdDay_day=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(userId,LocalDateTime.of(nextDay_DueDay,due_time));
            if(
            new AlarmSetter(context.getApplicationContext()).setAlarm("Tomorrow is Pay time",
                    string_next_Day_Before_DueDay,string_DueTime,userId,nameS,notificationIdDayBefore_day)
            &&
            new AlarmSetter(context.getApplicationContext()).setAlarm("Today is Pay time",
                    string_nextDay_DueDay,string_DueTime,userId,nameS, notificationIdDay_day)
            ){
                Log.d("Tag","setAlarm day done");
            }
            else{
                printError();
                return;
            }
            if(
               new db(context.getApplicationContext()).insertUserPayAndNotificationTable(userId, 0,
                 new db(context.getApplicationContext()).CreateSimpleDateFormat(nextDay_DueDay,due_time), notificationIdDay_day)
                            &&
               new db(context.getApplicationContext()).insertUserPayAndNotificationTable(userId, 2,
                 new db(context.getApplicationContext()).CreateSimpleDateFormat(next_Day_Before_DueDay,due_time), notificationIdDayBefore_day)
            ){
                Log.d("Tag","insertUserPayAndNotificationTable done");
            }
            else{
                printError();
                return;
            }

        }
        if(
            new db(context).insertHistory(userId,Amount,Attachment_link,LocalDateTime.of(Date_now,Time_now),"Created")
        ){
            Log.d("Tag","insertHistory done");
        }
        else{
            printError();
            return;
        }
        if(firstMonthAmount!=0){
            if(
            new db(context).updateAmountById(userId,firstMonthAmount)
            ){
                Log.d("Tag","updateAmountById done");
            }
            else{
                printError();
                return;
            }
            if(
            new db(context).insertHistory(userId,firstMonthAmount,Attachment_link,LocalDateTime.of(Date_now,Time_now),"Pay")
            ){
                Log.d("Tag","insertHistory after update done");
            }
            else{
                printError();
                return;
            }
        }
    }

    public boolean updateAmountById(int Id, Double Amount) {
        try{
            String q1="Select "+UpdateAmount+" from "+UserUpdatedBalanceTable+" where "+U_id+" = "+Id;
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor =  database.rawQuery( q1, null );
            cursor.moveToNext();
            Double bam=cursor.getDouble(cursor.getColumnIndex(UpdateAmount));
            database.close();
            Double pam=bam-Amount;
            String q2="update "+UserUpdatedBalanceTable+" set "+UpdateAmount+" = "+pam+" where "+U_id+" = "+Id;
            SQLiteDatabase database1 = this.getWritableDatabase();
            database1.execSQL( q2 );
            database1.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error updateAmountById:"+e.toString());
            return false;
        }
    }

    public boolean updateIsActive(int id){
        try{
            String q2="update "+Table_name+" set "+IsActiveUser+" = 0 where "+U_id+" = "+id;
            SQLiteDatabase database1 = this.getWritableDatabase();
            database1.execSQL( q2 );
            database1.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error updateIsActive:"+e.toString());
            return false;
        }
    }

    public void checkDailyUpdate(){
        //monthly
        String m="select * from "+ UserUpdatedBalanceTable+" where "+ DueDate +" <= CAST(CURRENT_TIMESTAMP AS DATE) AND "+DayOrMonth+" =1";
        List<User_Getter_Setter> toUpdateList_m=feachData_byQuery(m);

        for(int i=0; i < toUpdateList_m.size();i++){
            User_Getter_Setter data=toUpdateList_m.get(i);
            new db(context.getApplicationContext()).DailyUpdate_monthly(data.getId());
        }
        //DailyUpdate_daily
        String d="select * from "+ UserUpdatedBalanceTable+" where "+ DueDate +" <= CAST(CURRENT_TIMESTAMP AS DATE) AND "+DayOrMonth+" =0";
        List<User_Getter_Setter> toUpdateList_d=feachData_byQuery(d);

        for(int i=0; i < toUpdateList_d.size();i++){
            User_Getter_Setter data=toUpdateList_d.get(i);
            new db(context.getApplicationContext()).DailyUpdate_Daily(data.getId());
        }
        Log.d("Tag","Daily update is done!!");
    }

    // update daily view (monthly)
    public void DailyUpdate_monthly(int id){
        while (true){
            Date currentTime = Calendar.getInstance().getTime();
            LocalDate Date_now=LocalDate.of(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate());
            String q1="select * from "+ UserUpdatedBalanceTable+" where "+ DueDate +" <= NOW() AND "+DayOrMonth+" =1 AND "+U_id+" = "+id;
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor =  database.rawQuery( q1, null );
            cursor.moveToNext();
            LocalDateTime duedate=LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(DueDate)),formatter);
            int diff = currentTime.compareTo(new Date(cursor.getString(cursor.getColumnIndex(DueDate))));
            if (diff <= 0) {
                break;
            } else if (diff > 0) {
                LocalDateTime updateDuedate=duedate.plusMonths(1);
                LocalDateTime update_DayBeforeDueDate=updateDuedate.minusDays(1);
                Double a=cursor.getDouble(cursor.getColumnIndex(UpdateAmount));
                Double r=cursor.getDouble(cursor.getColumnIndex(U_Rate));
                Double _Amount_toAdd = (a/100) * r;
                Double _UpdateAmount = a + _Amount_toAdd;
                if(
                   new db(context.getApplicationContext()).insertHistory(id,_Amount_toAdd ,null,duedate,"Interest Added")
                ){
                    Log.d("Tag","insertHistory done");
                }
                else{
                    printError();
                    return;
                }
                if(
                   new db(context.getApplicationContext()).updatePayAmountById(id,_UpdateAmount)
                ){
                    Log.d("Tag","updatePayAmountById done");
                }
                else{
                    printError();
                    return;
                }
                if(
                   new db(context.getApplicationContext()).updateDates(id,updateDuedate,update_DayBeforeDueDate)
                ){
                    Log.d("Tag","updateDates done");
                }
                else{
                    printError();
                    return;
                }
                int notificationId=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(id,updateDuedate);
                int notificationId_before=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(id,update_DayBeforeDueDate);
                if(
                   new db(context.getApplicationContext()).insertUserPayAndNotificationTable(id, 0,
                       new db(context.getApplicationContext()).CreateSimpleDateFormat(updateDuedate), notificationId)
                           &&
                   new db(context.getApplicationContext()).insertUserPayAndNotificationTable(id, 2,
                       new db(context.getApplicationContext()).CreateSimpleDateFormat(update_DayBeforeDueDate), notificationId_before)
                ){
                    Log.d("Tag","insertUserPayAndNotificationTable done");
                }
                else{
                    printError();
                    return;
                }
                String update_DayBeforeDueDate_text=new AlarmSetter(context.getApplicationContext()).CreateStringDate(update_DayBeforeDueDate);
                String updateDuedate_text=new AlarmSetter(context.getApplicationContext()).CreateStringDate(updateDuedate);
                String string_DueTime=new AlarmSetter(context.getApplicationContext()).CreateStringTime(due_time);
                String name=cursor.getString(cursor.getColumnIndex(U_name));
                if(
                   new AlarmSetter(context.getApplicationContext()).setAlarm("Tomorrow is Pay time",update_DayBeforeDueDate_text,string_DueTime,id,name,
                       new AlarmSetter(context.getApplicationContext()).CreateNotificationID(id,update_DayBeforeDueDate))
                           &&
                   new AlarmSetter(context.getApplicationContext()).setAlarm("Today is Pay time",updateDuedate_text,string_DueTime,id,name,
                       new AlarmSetter(context.getApplicationContext()).CreateNotificationID(id,updateDuedate))
                ){
                    Log.d("Tag","setAlarm done");
                }
                else{
                    printError();
                    return;
                }
            }
        }

    }

    // update daily view (daily)
    public void DailyUpdate_Daily(int id){
        while (true){
            Date currentTime = Calendar.getInstance().getTime();
            LocalDate Date_now=LocalDate.of(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate());
            String q1="select * from "+ UserUpdatedBalanceTable+" where "+ DueDate +" <= NOW() AND "+DayOrMonth+" =0 AND "+U_id+" = "+id;
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor =  database.rawQuery( q1, null );
            cursor.moveToNext();
            LocalDateTime duedate=LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(DueDate)),formatter);
            LocalDateTime DayBeforeDueDate=LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(Day_Before_DueDate)),formatter);
            int diff=currentTime.compareTo(new Date(cursor.getString(cursor.getColumnIndex(DueDate))));
            if (diff <= 0) {
                break;
            } else if (diff > 0) {
                LocalDateTime updateDuedate=duedate.plusDays(1);
                Double a=cursor.getDouble(cursor.getColumnIndex(UpdateAmount));
                Double r=cursor.getDouble(cursor.getColumnIndex(U_Rate));
                Double _Amount_toAdd = (a/100) * r;
                Double _UpdateAmount = a + _Amount_toAdd;
                if(
                    new db(context.getApplicationContext()).insertHistory(id,_Amount_toAdd ,null,duedate,"Interest Added")
                ){
                    Log.d("Tag","insertHistory done");
                }
                else{
                    printError();
                    return;
                }
                if(
                    new db(context.getApplicationContext()).updatePayAmountById(id,_UpdateAmount)
                ){
                    Log.d("Tag","updatePayAmountById done");
                }
                else{
                    printError();
                    return;
                }
                if(
                    new db(context.getApplicationContext()).updateDates(id,DayBeforeDueDate,updateDuedate)
                ){
                    Log.d("Tag","updateDates done");
                }
                else{
                    printError();
                    return;
                }
                int notificationId=new AlarmSetter(context.getApplicationContext()).CreateNotificationID(id,updateDuedate);
                if(
                   new db(context.getApplicationContext()).insertUserPayAndNotificationTable(id, 2,
                       new db(context.getApplicationContext()).CreateSimpleDateFormat(updateDuedate), notificationId)
                ){
                    Log.d("Tag","insertUserPayAndNotificationTable done");
                }
                else{
                    printError();
                    return;
                }
                String updateDuedate_text=new AlarmSetter(context.getApplicationContext()).CreateStringDate(updateDuedate);
                String string_DueTime=new AlarmSetter(context.getApplicationContext()).CreateStringTime(due_time);
                String name=cursor.getString(cursor.getColumnIndex(U_name));
                if(
                   new AlarmSetter(context.getApplicationContext()).setAlarm("Today is Pay time",updateDuedate_text,string_DueTime,id,name,
                       new AlarmSetter(context.getApplicationContext()).CreateNotificationID(id,updateDuedate))
                ){
                    Log.d("Tag","setAlarm done");
                }
                else{
                    printError();
                    return;
                }
            }
        }

    }

    public boolean updateDates(int Id, LocalDateTime date,LocalDateTime _date) {
        try {
            String q1="update "+UserUpdatedBalanceTable+" set "+DueDate+" = "+
                    new db(context.getApplicationContext()).CreateSimpleDateFormat(date)
                    +" where "+U_id+" = "+Id;
            String q2="update "+UserUpdatedBalanceTable+" set "+Day_Before_DueDate+" = "+
                    new db(context.getApplicationContext()).CreateSimpleDateFormat(_date)
                    +" where "+U_id+" = "+Id;
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL( q1 );
            database.execSQL( q2 );
            database.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Errojr updateDates"+e);
            return false;
        }
    }

    public boolean updatePayAmountById(int Id, Double Amount) {
        try {
            String q="update "+UserUpdatedBalanceTable+" set "+UpdateAmount+" = "+Amount+" where "+U_id+" = "+Id;
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL( q );
            database.close();
            return true;
        }
        catch (Exception e) {
            Log.d("Tag","Error updatePayAmountById:"+e);
            return false;
        }
    }

    public boolean deleteDataFromPayTable(int id){
        try {
            String a="DELETE FROM "+UserPayAndNotification+" WHERE "+ U_id+" = "+id+" ; ";
            SQLiteDatabase database1 = this.getWritableDatabase();
            database1.execSQL( a );
            database1.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error deleteDataFromPayTable:"+e.toString());
            return false;
        }
    }

    public Double feachUpdateAmountById(int Id) {
        String q1="Select "+UpdateAmount+" from "+UserUpdatedBalanceTable+" where "+U_id+" = "+Id;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor =  database.rawQuery( q1, null );
        cursor.moveToNext();
        Double bam=cursor.getDouble(cursor.getColumnIndex(UpdateAmount));
        database.close();
       return bam;
    }

    public boolean update_not_pay_history(int id,int ispay,String date1){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String s1="UPDATE " +UserPayAndNotification+ " SET "+ispaid +" = "+ ispay+"  where "+U_id+" = "+id+" and "+ PayDate +" = '"+date1+"' ;";
            db.execSQL(s1);
            db.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error update_not_pay_history:"+e.toString());
            return false;
        }
    }

    public List<String[]> not_pay_history(int id){
        List<String[]> a=new ArrayList();
        String q1="Select * from "+UserPayAndNotification+" Where "+U_id+"="+id+" and "+ispaid+"=0";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor =  database.rawQuery( q1, null );
        while (cursor.moveToNext()){
            String s[]=new String[4];
            s[0]= String.valueOf(cursor.getInt(cursor.getColumnIndex(U_id)));
            s[1]= String.valueOf(cursor.getInt(cursor.getColumnIndex(ispaid)));
            s[2]= String.valueOf(cursor.getString(cursor.getColumnIndex(PayDate)));
            s[3]= String.valueOf(cursor.getString(cursor.getColumnIndex(N_u_id)));
            a.add(s);
        }
        database.close();
        return a;
    }

    public boolean insertHistory(int id,Double Amount,String Attachment_link,LocalDateTime now,String text){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, id);
            values.put(ExchangeAmount, Amount);
            values.put(U_Attachment_link, Attachment_link);
            values.put(ExchangeDate, new db(context.getApplicationContext()).CreateSimpleDateFormat(now));
            values.put(Text_Data, text);
            db.insert(UserHistory,null,values);
            db.close();
            return true;
        }catch (Exception e){
            Log.d("Tag","Error:insertHistory"+e.toString());
            return false;
        }
    }

    public boolean insertHistory(int id,Double Amount,String Attachment_link,String now,String text){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, id);
            values.put(ExchangeAmount, Amount);
            values.put(U_Attachment_link, Attachment_link);
            values.put(ExchangeDate, now);
            values.put(Text_Data, text);
            db.insert(UserHistory,null,values);
            db.close();
            return true;
        }catch (Exception e){
            Log.d("Tag","Error:insertHistory"+e.toString());
            return false;
        }
    }

    public List<String[]> fetchHistory_Id(int id){
        List<String[]> a=new ArrayList();
        String q1="Select * from "+UserHistory+" where "+U_id+" = "+id;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor =  database.rawQuery( q1, null );
        while (cursor.moveToNext()){
            String s[]=new String[5];
            s[0]= String.valueOf(cursor.getInt(cursor.getColumnIndex(U_id)));
            s[1]= String.valueOf(cursor.getDouble(cursor.getColumnIndex(ExchangeAmount)));
            s[2]= String.valueOf(cursor.getString(cursor.getColumnIndex(U_Attachment_link)));
            s[3]= String.valueOf(cursor.getString(cursor.getColumnIndex(ExchangeDate)));
            s[4]= String.valueOf(cursor.getString(cursor.getColumnIndex(Text_Data)));
            a.add(s);
        }
        database.close();
        return a;
    }

    public List<User_Getter_Setter> feachActiveUser(String SearchQuery) {
        String where = null;
        if (SearchQuery == null || SearchQuery == "") {
            where = " Where m." + IsActiveUser + " = 1 ";
        } else {
            where = " Where ( " + IsActiveUser + " = 1 ) AND ( n."+U_id+" LIKE '%"+SearchQuery+"%' OR "
                    +U_name+" LIKE '%"+SearchQuery+"%' OR "+U_mobile_no+" LIKE '%"+SearchQuery+"%' ) ";
        }
        String query = "SELECT n." + U_id + " , m." + U_name + ", m." + U_mobile_no + ", u." + UpdateAmount + ", n." + PayDate +", m." + IsActiveUser + ", m." + U_Rate +
                " from " + UserPayAndNotification + " n " +
                " INNER JOIN " + Table_name + " m " +
                " on m." + U_id + " = n." + U_id +
                " INNER JOIN " + UserUpdatedBalanceTable + " u" +
                " on m." + U_id + " = u." + U_id +
                where +
                " GROUP BY n." + U_id +
                " ORDER BY n." + PayDate + " ASC;";
        List<User_Getter_Setter> a= feachData_byQuery(query);
        Log.d("Tag my","datadb size:"+a.size());
        return a;
    }

    public List<User_Getter_Setter> feachAllUser(String SearchQuery){
        List<User_Getter_Setter> list_user_gettersetter=new ArrayList();
        String where=null;
        if(SearchQuery==null){
            where=";";
        }else {
            where=" Where "+ U_id+" LIKE '%"+SearchQuery+"%' or "
                    + U_name+" LIKE '%"+SearchQuery+"%' or "+U_mobile_no+" LIKE '%"+SearchQuery+"%' ;";
        }
        String q="Select * from "+Table_name +""+where;
        return feachData_byQuery(q);
    }

    public List<User_Getter_Setter> feachData_byQuery(String query){
        List<User_Getter_Setter> list_user_gettersetter=new ArrayList();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor =  db.rawQuery( query, null );
            Log.d("Tag","cursor:");
            while (cursor.moveToNext()){
                Log.d("Tag","cursor loop:");
                list_user_gettersetter.add(new db(context.getApplicationContext()).ugs_set(cursor));
            }
            return list_user_gettersetter;
        }
        catch (Exception exception){
            Log.d("Tag","cursor Error:"+ exception.toString());
            return list_user_gettersetter;
        }
    }

    public User_Getter_Setter feachUserDataById(int id){
        String q="Select * from "+Table_name+" Where  "+ U_id+" = "+id+" ;";
        User_Getter_Setter user_getter_setter=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( q, null );
        while (cursor.moveToNext()) {
            user_getter_setter=new db(context.getApplicationContext()).ugs_set(cursor);
        }
        return user_getter_setter;
    }

    public User_Getter_Setter feachActiveUserDataById(int id){
        String q="Select a.* , b."+UpdateAmount+",b."+DueDate+", b."+Day_Before_DueDate+" from "+Table_name+" a INNER JOIN "+UserUpdatedBalanceTable+" b on a."+U_id+"=b."+U_id+" Where a."+U_id+" = "+id+";";
        User_Getter_Setter user_getter_setter=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( q, null );
        while (cursor.moveToNext()){
            user_getter_setter=new db(context.getApplicationContext()).ugs_set(cursor);
        }
        return user_getter_setter;
    }

    public String CreateSimpleDateFormat(LocalDate localDate,LocalTime localTime){
        return dateFormat.format(new Date(localDate.getYear(),localDate.getMonthValue(),localDate.getDayOfMonth(),localTime.getHour(),localTime.getMinute(),localTime.getSecond()));
    }

    public String CreateSimpleDateFormat(LocalDateTime localDateTime){
        return dateFormat.format(new Date(localDateTime.getYear(),localDateTime.getMonthValue(),localDateTime.getDayOfMonth(),localDateTime.getHour(),localDateTime.getMinute(),localDateTime.getSecond()));
    }

    public List<User_Getter_Setter> feachUserTable(){
        List<User_Getter_Setter> allUser=new ArrayList();
        String q="Select * from "+Table_name+" ; ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( q, null );
        while (cursor.moveToNext()){
            User_Getter_Setter user_getter_setter=new db(context.getApplicationContext()).ugs_set(cursor);
            allUser.add(user_getter_setter);
        }
        return allUser;
    }

    public List<User_Getter_Setter> feachUserUpdatedBalanceTable(){
        List<User_Getter_Setter> allUser=new ArrayList();
        String q="Select * from "+UserUpdatedBalanceTable+" ; ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( q, null );
        while (cursor.moveToNext()){
            User_Getter_Setter user_getter_setter=new db(context.getApplicationContext()).ugs_set(cursor);
            allUser.add(user_getter_setter);
        }
        return allUser;
    }

    public List<User_Getter_Setter> feachUserHistoryTable(){
        List<User_Getter_Setter> allUser=new ArrayList();
        String q="Select * from "+UserHistory+" ; ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( q, null );
        while (cursor.moveToNext()){
            User_Getter_Setter user_getter_setter=new db(context.getApplicationContext()).ugs_set(cursor);
            allUser.add(user_getter_setter);
        }
        return allUser;
    }

    public List<User_Getter_Setter> feachUserPayAndNotification(){
        List<User_Getter_Setter> allUser=new ArrayList();
        String q="Select * from "+UserPayAndNotification+" ; ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( q, null );
        while (cursor.moveToNext()){
            User_Getter_Setter user_getter_setter=new db(context.getApplicationContext()).ugs_set(cursor);
            allUser.add(user_getter_setter);
        }
        return allUser;
    }

    public User_Getter_Setter ugs_set(Cursor cursor){

        User_Getter_Setter user_getter_setter=new User_Getter_Setter();
        try{
            user_getter_setter.setName(cursor.getString(cursor.getColumnIndex(U_name)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setPayDate(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(PayDate)),formatter));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setMobileno(cursor.getString(cursor.getColumnIndex(U_mobile_no)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setId(cursor.getInt(cursor.getColumnIndex(U_id)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setDailyOrMonthly(cursor.getInt(cursor.getColumnIndex(DayOrMonth)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setAmount(cursor.getDouble(cursor.getColumnIndex(StartAmount)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setRate(cursor.getDouble(cursor.getColumnIndex(U_Rate)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setAttachment_link(cursor.getString(cursor.getColumnIndex(U_Attachment_link)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setIsActiveUser(cursor.getInt(cursor.getColumnIndex(IsActiveUser)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setReturn_time(cursor.getInt(cursor.getColumnIndex(Return_time)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setUpdateAmount(cursor.getDouble(cursor.getColumnIndex(UpdateAmount)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setStartDate(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(StartDate)),formatter));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setDueDate(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(DueDate)),formatter));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setDay_Before_DueDate(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(Day_Before_DueDate)),formatter));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setExchangeDate(LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(ExchangeDate)),formatter));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setExchangeAmount(cursor.getDouble(cursor.getColumnIndex(ExchangeAmount)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setIsPaid(cursor.getInt(cursor.getColumnIndex(ispaid)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setN_u_id(cursor.getInt(cursor.getColumnIndex(N_u_id)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        try{
            user_getter_setter.setTextData(cursor.getString(cursor.getColumnIndex(Text_Data)));
        }catch(Exception e){
            Log.d("Tag",e.getMessage()+"");
        }
        return user_getter_setter;
    }

    public void deleteDublicateTable(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL( "drop table "+temp_Table_name+" ;" );
        database.execSQL( "drop table "+temp_UserHistory+" ;" );
        database.execSQL( "drop table "+temp_UserPayAndNotification+" ;" );
        database.execSQL( "drop table "+temp_UserUpdatedBalanceTable+" ;" );
        Toast.makeText(context.getApplicationContext(),"Done delete",Toast.LENGTH_LONG).show();
        Toast.makeText(context.getApplicationContext(),"Restore Done!!",Toast.LENGTH_SHORT).show();
        database.close();
    }

    public void CreateDublicateTable(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL( "CREATE TABLE "+temp_Table_name+" as select * FROM "+Table_name+" ;" );
        //database.execSQL( "CREATE TABLE "+temp_Table_name+" as select distinct * FROM "+Table_name+" ;" );
        database.execSQL( "CREATE TABLE "+temp_UserHistory+" as select * FROM "+UserHistory+" ;" );
        database.execSQL( "CREATE TABLE "+temp_UserPayAndNotification+" as select * FROM "+UserPayAndNotification+" ;" );
        database.execSQL( "CREATE TABLE "+temp_UserUpdatedBalanceTable+" as select * FROM "+UserUpdatedBalanceTable+" ;" );
        database.execSQL( "DELETE FROM "+temp_Table_name+" ;" );
        database.execSQL( "DELETE FROM "+temp_UserHistory+" ;" );
        database.execSQL( "DELETE FROM "+temp_UserPayAndNotification+" ;" );
        database.execSQL( "DELETE FROM "+temp_UserUpdatedBalanceTable+" ;" );
        database.close();
    }

    public void DublicateEXCEPTCoperation(File file){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor =  database.rawQuery( "SELECT * FROM "+temp_Table_name+
                " EXCEPT " +
                " SELECT * FROM " + Table_name+" ;", null );
        Log.d("restoredb t1","jsonobject:"+cursor.toString());
        while(cursor.moveToNext()){
            new db(context.getApplicationContext()).insertUserTable(
                    cursor.getInt(cursor.getColumnIndex(U_id)),
                    cursor.getString(cursor.getColumnIndex(U_name)),
                    cursor.getString(cursor.getColumnIndex(U_mobile_no)),
                    cursor.getDouble(cursor.getColumnIndex(StartAmount)),
                    cursor.getDouble(cursor.getColumnIndex(U_Rate)),
                    cursor.getInt(cursor.getColumnIndex(DayOrMonth)),
                    cursor.getString(cursor.getColumnIndex(U_Attachment_link)),
                    cursor.getInt(cursor.getColumnIndex(Return_time)),
                    cursor.getInt(cursor.getColumnIndex(IsActiveUser)),
                    cursor.getString(cursor.getColumnIndex(StartDate))
            );
        }
        database.close();

        SQLiteDatabase database1 = this.getReadableDatabase();
        Cursor cursor1 =  database1.rawQuery( "SELECT * FROM "+temp_UserHistory+
                " EXCEPT " +
                "SELECT * FROM " +UserHistory+" ;", null );
        Log.d("restoredb t2","jsonobject:"+cursor1.toString());
        while(cursor1.moveToNext()){
            new db(context.getApplicationContext()).insertHistory(
                    cursor1.getInt(cursor1.getColumnIndex(U_id)),
                    cursor1.getDouble(cursor1.getColumnIndex(ExchangeAmount)),
                    cursor1.getString(cursor1.getColumnIndex(U_Attachment_link)),
                    cursor1.getString(cursor1.getColumnIndex(ExchangeDate)),
                    cursor1.getString(cursor1.getColumnIndex(Text_Data))
            );
        }
        database1.close();

        SQLiteDatabase database2 = this.getReadableDatabase();
        Cursor cursor2 =  database2.rawQuery( "SELECT * FROM "+temp_UserUpdatedBalanceTable+
                " EXCEPT " +
                "SELECT * FROM " +UserUpdatedBalanceTable+" ;", null );
        Log.d("restoredb t3","jsonobject:"+cursor2.toString());
        while(cursor2.moveToNext()){
            new db(context.getApplicationContext()).insertUserUpdatedBalanceTable(
                    cursor2.getInt(cursor2.getColumnIndex(U_id)),
                    cursor2.getInt(cursor2.getColumnIndex(DayOrMonth)),
                    cursor2.getDouble(cursor2.getColumnIndex(UpdateAmount)),
                    cursor2.getString(cursor2.getColumnIndex(DueDate)),
                    cursor2.getString(cursor2.getColumnIndex(Day_Before_DueDate)),
                    cursor2.getDouble(cursor2.getColumnIndex(U_Rate))
            );
        }
        database2.close();

        SQLiteDatabase database3 = this.getReadableDatabase();
        Cursor cursor3 =  database3.rawQuery( "SELECT * FROM "+temp_UserPayAndNotification+
                " EXCEPT " +
                "SELECT * FROM " +UserPayAndNotification+" ;", null );
        Log.d("restoredb t4","jsonobject:"+cursor3.toString());
        while(cursor3.moveToNext()){
            new db(context.getApplicationContext()).insertUserPayAndNotificationTable(
                    cursor3.getInt(cursor3.getColumnIndex(U_id)),
                    cursor3.getInt(cursor3.getColumnIndex(ispaid)),
                    cursor3.getString(cursor3.getColumnIndex(PayDate)),
                    cursor3.getInt(cursor3.getColumnIndex(N_u_id))
            );
        }
        database3.close();
        if(file.delete()){
            Log.d("restoredb","file delete");
        }else{
            Log.d("restoredb","file not delete");
        }
        new db(context.getApplicationContext()).deleteDublicateTable();
        //Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //context.startActivity(intent);
        Toast.makeText(context.getApplicationContext(),"Restore done!!",Toast.LENGTH_SHORT).show();
        //System.exit(0);
    }

    public boolean insertTempUserUpdatedBalanceTable(int Id,int dailyOrMonthly,Double Amount,String dueDate,String day_Before_DueDate,Double rate){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, Id);
            values.put(DayOrMonth, dailyOrMonthly);
            values.put(UpdateAmount, Amount);
            values.put(DueDate, dueDate);
            values.put(Day_Before_DueDate, day_Before_DueDate);
            values.put(U_Rate, rate);
            db.insert(temp_UserUpdatedBalanceTable,null,values);
            db.close();
            Log.d("Tag","Done:insertUserUpdatedBalanceTable");
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error:insertUserUpdatedBalanceTable"+e.toString());
            return false;
        }
    }

    public boolean insertTempUserPayAndNotificationTable(int Id,int IsPaid,String payDate,int n_u_id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, Id);
            values.put(N_u_id,n_u_id);
            values.put(ispaid, IsPaid);
            values.put(PayDate, payDate);
            db.insert(temp_UserPayAndNotification,null,values);
            db.close();
            Log.d("Tag","Done:insertUserPayAndNotificationTable");
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error:insertUserPayAndNotificationTable"+e.toString());
            return false;
        }
    }

    public boolean insertTempUserTable(int userId, String nameS, String mobileno, double Amount, Double rate, int dailyOrMonthly,
                                       String Attachment_link,int return_time,int isActiveUser,String startDate){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, userId);
            values.put(U_name, nameS);
            values.put(U_mobile_no, mobileno);
            values.put(DayOrMonth, dailyOrMonthly);
            values.put(StartAmount, Amount);
            values.put(U_Rate, rate);
            values.put(U_Attachment_link, Attachment_link);
            values.put(IsActiveUser, isActiveUser);
            values.put(Return_time, return_time);
            values.put(StartDate, startDate);
            db.insert(temp_Table_name,null,values);
            Log.d("Tag","Done:insertUserTable ");
            db.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag","Error:insertUserTable "+e.toString());
            return false;
        }
    }

    public boolean insertTempHistory(int id, Double Amount, String Attachment_link, String ExchangeD, String text){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(U_id, id);
            values.put(ExchangeAmount, Amount);
            values.put(U_Attachment_link, Attachment_link);
            values.put(ExchangeDate, ExchangeD);
            values.put(Text_Data, text);
            db.insert(temp_UserHistory,null,values);
            db.close();
            return true;
        }catch (Exception e){
            Log.d("Tag","Error:insertHistory"+e.toString());
            return false;
        }
    }

}