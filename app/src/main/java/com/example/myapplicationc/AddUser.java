package com.example.myapplicationc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class AddUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    EditText name,amount_,firstMonthAmount,return_time;
    Button attachment_button,attachment_cancel_button;
    TextView Attachment;
    NumberPicker rateInt,rateDes;
    Double firstMonthAmount_int;
    int UserId,DailyOrMonthly_value=1;
    String filename="none",DailyOrMonthly="Monthly",mobileno=null;
    private static final int PICKFILE_RESULT_CODE = 1 ,Launch_Camera_REQUEST_CODE=3;
    File photoFile;
    Bitmap takenImage=null;
    EditText Rate;
    List<String[]> ContactList;
    SearchableSpinner spinnercontactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        if(getActionBar()!=null){
            getActionBar().hide();
        }
        Attachment=(TextView) findViewById(R.id.text_attachment_link);
        attachment_button=(Button)findViewById(R.id.button_attachment);
        attachment_cancel_button=(Button)findViewById(R.id.attachment_cancel_button1);
        name=(EditText)findViewById(R.id.name);
        Rate=(EditText)findViewById(R.id.rate);
        amount_=(EditText)findViewById(R.id.amount_);
        spinnercontactList=(SearchableSpinner)findViewById(R.id.contactList);
        firstMonthAmount=(EditText)findViewById(R.id.firstMonthAmount);
        return_time=(EditText)findViewById(R.id.return_time);
        spinnercontactList.setTitle("Choose Contact");
        spinnercontactList.setOnItemSelectedListener(this);
        ContactList=getContacts();
        List<String> a1=new ArrayList();
        a1.add("Choose Contact");
        for(int i=0;i<ContactList.size();i++){
            String[] aa=ContactList.get(i);
            a1.add(aa[0]+":"+aa[1]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, a1);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercontactList.setAdapter(dataAdapter);

        UserId = new Random().nextInt(98000) + 1000;
        RadioButton day=(RadioButton)findViewById(R.id.radio_d);
        RadioButton month=(RadioButton)findViewById(R.id.radio_m);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailyOrMonthly_value=0;
                DailyOrMonthly="Daily";
            }
        });
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailyOrMonthly_value=1;
                DailyOrMonthly="Monthly";
            }
        });

        attachment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filename="none";
                AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
                builder.setTitle("Choose");
                builder.setItems(new CharSequence[]
                                {"Camera", "File"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        onLaunchCamera();
                                        break;
                                    case 1:
                                        fileChooser();
                                        break;
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        attachment_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelActivity();
            }
        });
    }

    public void fileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"), PICKFILE_RESULT_CODE);
    }

    public void onLaunchCamera() {
        if(filename=="none"){
            filename = new ImageOL(this).CreatePhotoFileName();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = new ImageOL(getApplicationContext()).getPhotoFileUri(filename);
        Uri fileProvider = FileProvider.getUriForFile(AddUser.this, "com.example.myapplicationc", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        ContentResolver cr = getContentResolver();
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,Launch_Camera_REQUEST_CODE );
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri.toString().startsWith("content://")) {
                        try {
                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATA};
                            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                //Uri imageUri = data.getData();
                                takenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);//imageUri);
                                /* document_id,mime_type,_display_name,last_modified,flags,_size */
                                filename = cursor.getString(cursor.getColumnIndex("_display_name"));
                                Attachment.setText(filename);
                                attachment_cancel_button.setVisibility(View.VISIBLE);
                            }
                            cursor.close();
                        }catch (Exception e){
                            Log.d("Tag","image error"+e.toString());
                            e.printStackTrace();
                        }
                    }
                } else{
                    cancelActivity();
                }
                break;
            case Launch_Camera_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    Attachment.setText(filename);
                    attachment_cancel_button.setVisibility(View.VISIBLE);
                }else{
                    cancelActivity();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void cancelActivity(){
        Attachment.setText("");
        filename="none";
        takenImage=null;
        attachment_cancel_button.setVisibility(View.INVISIBLE);
    }

    public void save(View view){
        String nameS=name.getText().toString().trim();
        String amount_S=amount_.getText().toString().trim();
        String return_time_s=return_time.getText().toString().trim();
        String rate_s=Rate.getText().toString().trim();
        if(nameS.length()<1){
            Snackbar.make(view, "Name Should not be empty!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        if(amount_S.length()<1){
            Snackbar.make(view, "Amount Should not be empty!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        if(mobileno==null){
            Snackbar.make(view, "Select Valid Mobile No.!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        if(rate_s.length()<1){
            Snackbar.make(view, "Enter a valid Rate!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        Double rate=Double.parseDouble(rate_s.toString());
        if(rate<0 || rate>20){
            Snackbar.make(view, "Enter a valid Rate!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        if(return_time_s.length()<=0){
            Snackbar.make(view, "Return Time Should not be empty!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        if(firstMonthAmount.length()<1){
            Snackbar.make(view, "1st Months Paying Amount Should not be empty!!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }
        firstMonthAmount_int=Double.parseDouble(firstMonthAmount.getText().toString());
        if(Double.parseDouble(amount_S) < firstMonthAmount_int){
            Snackbar.make(view, "Amount Should not less than 1st Months Paying Amount !!!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return;
        }

        Double interest= Double.parseDouble(amount_.getText().toString())/100*rate;
        Double TotalAM=interest+Integer.parseInt(amount_.getText().toString());
        int return_time=Integer.parseInt(return_time_s);

        String Message="Generated User ID:"+ UserId +"\n"+
                "Name:"+name.getText().toString()+"\n"+
                "Mobile no:"+mobileno+"\n" +
                "Amount:"+amount_.getText().toString()+" ₹\n" +
                "Rate:"+ rate +"% "+DailyOrMonthly+"\n" +
                DailyOrMonthly+" Interest:"+String.format("%.2f ₹%n", interest )+
                "Return Time:"+ return_time +""+DailyOrMonthly+"\n" +
                "Amount Taken:"+firstMonthAmount_int+"₹\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(AddUser.this);
        builder.setMessage(Message).setTitle("Confirmation !");
        builder.setNegativeButton("Edit",null);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(takenImage==null){ }
                else{
                    new ImageOL(getApplicationContext()).saveImage(takenImage,filename);
                }
                //mobileno=Attachment.getText().toString().trim();
                new db(getApplicationContext()).addA(UserId,nameS,mobileno,Double.parseDouble(amount_S),rate,DailyOrMonthly_value,filename,firstMonthAmount_int,return_time);
                Toast.makeText(getApplicationContext(),"Add Done",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddUser.this,MainActivity.class));
                finish();
            }
        });
        builder.create().show();
    }

    public void cancel(View v){
        startActivity(new Intent(AddUser.this,MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddUser.this,MainActivity.class));
        finish();
    }

    private List<String[]> getContacts() {
        List<String[]> Contactlist=new ArrayList();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String[] contact=new String[2];
                        contact[0]= name;
                        contact[1]= phoneNo;
                        Contactlist.add(contact);
                    }
                    pCur.close();
                }
            }
        }
    return Contactlist;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            mobileno=null;
        }else{
            String item = parent.getItemAtPosition(position).toString().trim();
            String[] selected=ContactList.get(position-1);
            mobileno=selected[1];
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}