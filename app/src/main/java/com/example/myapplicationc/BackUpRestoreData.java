package com.example.myapplicationc;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BackUpRestoreData {
    Context context;
    String appName="My Application c";
    String imagepath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + appName ;
    String imagepath1= (Environment.DIRECTORY_DOCUMENTS) + "/" + appName ;
    private static final String Table_name = "User";
    private static final String UserUpdatedBalanceTable = "UserUpdatedBalanceTable";
    private static final String UserHistory = "UserHistory";
    private static final String UserPayAndNotification= "UserPayAndNotification";

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

    BackUpRestoreData(Context context){
        this.context=context;
    }

    private JSONObject InsertDataInUserHistoryTable(
            int id, Double Exchange_Amount,
            String Exchange_Date,String link,String text){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(U_id, id);
            jsonObject.put(ExchangeAmount, Exchange_Amount);
            jsonObject.put(ExchangeDate, Exchange_Date);
            jsonObject.put(U_Attachment_link,link);
            jsonObject.put(Text_Data,text);
        }
        catch (Exception e){
            Log.d("JSON","Error createData"+e.toString());
        }
        return jsonObject;
    }

    private JSONObject InsertDataInUserUpdatedBalanceTable(
            int id,int Day_Month, Double update_Amount,
            String Due_Date,String DayBeforeDueDate,Double Rate){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(U_id, id);
            jsonObject.put(DayOrMonth, Day_Month);
            jsonObject.put(UpdateAmount, update_Amount);
            jsonObject.put(DueDate,Due_Date);
            jsonObject.put(Day_Before_DueDate,DayBeforeDueDate);
            jsonObject.put(U_Rate,Rate);
        }
        catch (Exception e){
            Log.d("JSON","Error createData"+e.toString());
        }
        return jsonObject;
    }

    private JSONObject InsertDataInUserTableJSON(
            int id,String name,String mobile_no,int Day_Month, Double Start_Amount,
            Double  Rate, String Attachment_link,String Start_Date,int ReturnTime,int isActiveUser){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(U_id, id);
            jsonObject.put(U_name, name);
            jsonObject.put(U_mobile_no, mobile_no);
            jsonObject.put(DayOrMonth, Day_Month);
            jsonObject.put(StartAmount,Start_Amount);
            jsonObject.put(U_Rate,Rate);
            jsonObject.put(U_Attachment_link,Attachment_link);
            jsonObject.put(StartDate,Start_Date);
            jsonObject.put(Return_time,ReturnTime);
            jsonObject.put(IsActiveUser,isActiveUser);
        }
        catch (Exception e){
            Log.d("JSON","Error createData"+e.toString());
        }
        return jsonObject;
    }

    private JSONObject InsertDataInUserPayAndNotificationTable(int id, int n_u_id, String Pay_Date, int isPaid) {
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(U_id, id);
            jsonObject.put(N_u_id, n_u_id);
            jsonObject.put(PayDate, Pay_Date);
            jsonObject.put(ispaid,isPaid);
        }
        catch (Exception e){
            Log.d("JSON","Error createData"+e.toString());
        }
        return jsonObject;
    }

    public boolean makeBackUp(){
        try{
            // Main
            JSONObject jsonObjectBackUpFile = new JSONObject();
            // UserTable
            JSONArray jsonArrayUserTable = new JSONArray();
            List<User_Getter_Setter> listuser=new db(context.getApplicationContext()).feachUserTable();
            for(int i=0;i<listuser.size();i++){
                User_Getter_Setter ugs=listuser.get(i);
                JSONObject json =InsertDataInUserTableJSON(
                        ugs.getId(),ugs.getName(),ugs.getMobileno(),ugs.getDailyOrMonthly(),ugs.getAmount(),ugs.getRate(),
                        ugs.getAttachment_link(),new db(context.getApplicationContext()).CreateSimpleDateFormat(ugs.getStartDate()),ugs.getReturn_time(),ugs.getIsActiveUser()
                );
                jsonArrayUserTable.put(json);
            }
            jsonObjectBackUpFile.put(Table_name,jsonArrayUserTable);

            // UserUpdatedBalanceTable
            JSONArray jsonArrayUserUpdatedBalanceTable = new JSONArray();
            List<User_Getter_Setter> listuserUUBT=new db(context.getApplicationContext()).feachUserUpdatedBalanceTable();
            for(int i=0;i<listuserUUBT.size();i++){
                User_Getter_Setter ugs=listuserUUBT.get(i);
                JSONObject json =InsertDataInUserUpdatedBalanceTable(
                        ugs.getId(),ugs.getDailyOrMonthly(),ugs.getUpdateAmount(),
                        new db(context.getApplicationContext()).CreateSimpleDateFormat(ugs.getDueDate()),
                        new db(context.getApplicationContext()).CreateSimpleDateFormat(ugs.getDay_Before_DueDate()),ugs.getRate()
                );
                jsonArrayUserUpdatedBalanceTable.put(json);
            }
            jsonObjectBackUpFile.put(UserUpdatedBalanceTable,jsonArrayUserUpdatedBalanceTable);

            // UserUpdatedBalanceTable
            JSONArray jsonArrayUserHistoryTable = new JSONArray();
            List<User_Getter_Setter> listuserUHT=new db(context.getApplicationContext()).feachUserHistoryTable();
            for(int i=0;i<listuserUHT.size();i++){
                User_Getter_Setter ugs=listuserUHT.get(i);
                JSONObject json =InsertDataInUserHistoryTable(
                        ugs.getId(),ugs.getExchangeAmount(),
                        new db(context.getApplicationContext()).CreateSimpleDateFormat(ugs.getExchangeDate()),
                        ugs.getHistory_Table_Attachment_link(),ugs.getTextData()
                );
                jsonArrayUserHistoryTable.put(json);
            }
            jsonObjectBackUpFile.put(UserHistory,jsonArrayUserHistoryTable);

            // UserPayAndNotification
            JSONArray jsonArrayUserPayAndNotificationTable = new JSONArray();
            List<User_Getter_Setter> listuserUPNT=new db(context.getApplicationContext()).feachUserPayAndNotification();
            for(int i=0;i<listuserUPNT.size();i++){
                User_Getter_Setter ugs=listuserUPNT.get(i);
                JSONObject json =InsertDataInUserPayAndNotificationTable(
                        ugs.getId(),ugs.getN_u_id(),
                        new db(context.getApplicationContext()).CreateSimpleDateFormat(ugs.getPayDate()),
                        ugs.getIsPaid()
                );
                jsonArrayUserPayAndNotificationTable.put(json);
            }
            jsonObjectBackUpFile.put(UserPayAndNotification,jsonArrayUserPayAndNotificationTable);

            String userString = jsonObjectBackUpFile.toString();
            File file1 = new File(imagepath);
            if(file1.exists()){
                Log.d("JSON","already exist ");
            }else {
                if (file1.mkdirs()) {
                    Log.d("JSON", "mkdir done ");
                } else {
                    Log.d("JSON", "mkdir not done ");
                }
            }
            Date currentTime = Calendar.getInstance().getTime();
            File file = new File(imagepath, "BackUp "+currentTime.toString().replaceAll(":", ".")+".json");

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(userString);
            bufferedWriter.close();
            return true;
        }
        catch (Exception e){
            Log.d("Tag value1","backup: "+e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public void readData(File file){
        try{
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String responce = stringBuilder.toString();
            readExecution(responce,file);
        }
        catch (Exception e){
            Log.d("restoredb","Error:"+e.toString());
        }
    }

    private void readExecution(String Fname,File file){
        try{
            try{
                new db(context.getApplicationContext()).deleteDublicateTable();
            }
            catch (Exception e){

            }
            String data=Fname;
            if(data!=null){
                new db(context.getApplicationContext()).CreateDublicateTable();
                JSONObject obj = new JSONObject(data);
                // UserTable
                JSONArray myListsAll = obj.getJSONArray(Table_name);
                for (int i = 0; i < myListsAll.length(); i++) {
                    JSONObject jsonobject = (JSONObject) myListsAll.getJSONObject(i);
                    Log.d("restoredb",""+jsonobject.toString());
                    new db(context.getApplicationContext()).insertTempUserTable(
                            jsonobject.getInt(U_id), jsonobject.getString(U_name), jsonobject.getString(U_mobile_no),
                            jsonobject.getDouble(StartAmount), jsonobject.getDouble(U_Rate), jsonobject.getInt(DayOrMonth),
                            jsonobject.has(U_Attachment_link)?jsonobject.getString(U_Attachment_link):"", jsonobject.getInt(Return_time),
                            jsonobject.getInt(IsActiveUser), jsonobject.getString(StartDate)
                    );
                }

                // UserUpdatedBalanceTable
                JSONArray UserUpdatedBalanceTableJsonArray = obj.getJSONArray(UserUpdatedBalanceTable);
                for (int i = 0; i < UserUpdatedBalanceTableJsonArray.length(); i++) {
                    JSONObject jsonobject = (JSONObject) UserUpdatedBalanceTableJsonArray.getJSONObject(i);
                    Log.d("restoredb",""+jsonobject.toString());
                    new db(context.getApplicationContext()).insertTempUserUpdatedBalanceTable(
                            jsonobject.getInt(U_id), jsonobject.getInt(DayOrMonth),jsonobject.getDouble(UpdateAmount),
                            jsonobject.getString(DueDate),jsonobject.getString(Day_Before_DueDate),jsonobject.getDouble(U_Rate)
                    );

                }

                // UserHistory
                JSONArray UserHistoryJsonArray = obj.getJSONArray(UserHistory);
                for(int i=0;i<UserHistoryJsonArray.length();i++){
                    JSONObject jsonobject= (JSONObject) UserHistoryJsonArray.getJSONObject(i);
                    Log.d("restoredb",""+jsonobject.toString());
                    new db(context.getApplicationContext()).insertTempHistory(
                            jsonobject.getInt(U_id),
                            jsonobject.getDouble(ExchangeAmount),
                            jsonobject.has(U_Attachment_link)?jsonobject.getString(U_Attachment_link):"",
                            jsonobject.getString(ExchangeDate),
                            jsonobject.getString(Text_Data)
                    );
                }

                // UserPayAndNotification
                JSONArray UserPayAndNotificationJsonArray = obj.getJSONArray(UserPayAndNotification);
                for(int i=0;i<UserPayAndNotificationJsonArray.length();i++){
                    JSONObject jsonobject= (JSONObject) UserPayAndNotificationJsonArray.getJSONObject(i);
                    Log.d("restoredb",""+jsonobject.toString());
                    new db(context.getApplicationContext()).insertTempUserPayAndNotificationTable(
                            jsonobject.getInt(U_id),
                            jsonobject.getInt(N_u_id),
                            jsonobject.getString(PayDate),
                            jsonobject.getInt(ispaid)
                    );
                }
                new db(context.getApplicationContext()).DublicateEXCEPTCoperation(file);

            }else{
                Toast.makeText(context.getApplicationContext(),"file is empty!!",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(context.getApplicationContext(),""+e.toString(),Toast.LENGTH_SHORT).show();
            Log.d("restoredb",": "+e.toString());
            e.printStackTrace();
        }
    }

}