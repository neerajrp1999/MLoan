package com.example.myapplicationc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PayActivity extends AppCompatActivity {
    LinearLayoutCompat linear;
    TextView pay_see_id,pay_see_name,pay_see_mobileno,pay_see_amount,pay_text_attachment_link,PayAttachment;
    int _id;
    Double Amount;
    int[] payCheck;
    CheckBox all_s;
    EditText pay_amount_;
    Button pay_attachment,pay_attachment_cancel_button;
    Intent intent;
    File PayPhotoFile;
    Bitmap PayTakenImage=null;
    String Payfilename="none";
    List<String[]> pay_history_list;
    private static final int PAY_PICKFILE_RESULT_CODE = 11 ,PAY_Launch_Camera_REQUEST_CODE=13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        _id=getIntent().getExtras().getInt("id");

        intent=new Intent(getApplicationContext(),UserDash.class);
        intent.putExtra("id",_id);
        intent.putExtra("active",1);

        pay_see_id=(TextView)findViewById(R.id.pay_see_id);
        PayAttachment=(TextView)findViewById(R.id.pay_text_attachment_link);
        pay_text_attachment_link=(TextView)findViewById(R.id.pay_text_attachment_link);
        pay_see_name=(TextView)findViewById(R.id.pay_see_name);
        pay_see_mobileno=(TextView)findViewById(R.id.pay_see_mobileno);
        pay_see_amount=(TextView)findViewById(R.id.pay_see_amount);
        all_s=(CheckBox)findViewById(R.id.all_S_Pay);
        pay_amount_=(EditText) findViewById(R.id.pay_amount_);
        pay_attachment=(Button) findViewById(R.id.pay_attachment);
        pay_attachment_cancel_button=(Button) findViewById(R.id.pay_attachment_cancel_button);

        pay_see_id.setText("ID:"+_id+"");
        User_Getter_Setter user=new db(getApplicationContext()).feachActiveUserDataById(_id);
        pay_see_name.setText("User Name:"+user.getName());
        pay_see_mobileno.setText("Mobile no:"+user.getMobileno());

        Amount=new db(getApplicationContext()).feachUpdateAmountById(_id);
        pay_see_amount.setText("Balance:"+Amount);
        linear=(LinearLayoutCompat)findViewById(R.id.pay_ll2);
        pay_history_list=new db(getApplicationContext()).not_pay_history(_id);
        payCheck=new int[pay_history_list.size()];
        for (int i=0;i<pay_history_list.size();i++){
            LinearLayoutCompat.LayoutParams lparams = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            String ph[]=pay_history_list.get(i);
            payCheck[i]=1;
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(i);
            checkBox.setText(ph[2]);
            checkBox.setChecked(true);
            checkBox.setLayoutParams(lparams);
            final int j=i;
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String msg = "You have " + (isChecked ? "checked" : "unchecked") + " this Check it Checkbox.";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    if(isChecked){
                        payCheck[j]=1;
                    }else{
                        payCheck[j]=0;
                    }
                }
            });
            linear.addView(checkBox);
        }
        pay_attachment_cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayCancelActivity();
            }
        });
        all_s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    linear.setVisibility(View.GONE);
                    linear.setEnabled(false);
                }else{
                    linear.setVisibility(View.VISIBLE);
                    linear.setEnabled(true);
                }
            }
        });
        pay_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Payfilename="none";
                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                builder.setTitle("Choose");
                builder.setItems(new CharSequence[]
                                {"Camera", "File"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        PayonLaunchCamera();
                                        break;
                                    case 1:
                                        PayfileChooser();
                                        break;
                                }
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        Button cancel=(Button) findViewById(R.id.pay_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

        Button pay_button=(Button) findViewById(R.id.pay);
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pay_amount_.getText().toString().trim().isEmpty()){
                    Snackbar.make(view, "Amount Should not be empty!!!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }
                Double pay_a=Double.parseDouble(pay_amount_.getText().toString().trim());

                if(pay_a>Amount){
                    Snackbar.make(view, "Pay  Amount Should not be Greater then balance amount!!!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return;
                }
                Double newAmount=Amount-pay_a;
                Date currentTime = Calendar.getInstance().getTime();
                LocalDateTime Date_now=LocalDateTime.of(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate(),currentTime.getHours(),currentTime.getMinutes(),currentTime.getSeconds());
                new db(getApplicationContext()).updatePayAmountById(_id,newAmount);

                new db(getApplicationContext()).insertHistory(_id, pay_a,Payfilename, Date_now,"pay");
                if(all_s.isChecked()){
                    new db(getApplicationContext()).updateIsActive(_id);
                    new db(getApplicationContext()).deleteDataFromPayTable(_id);
                    intent.putExtra("active",0);
                }
                else{
                    for (int k=0;k<payCheck.length;k++) {
                        String ss[]=pay_history_list.get(k);
                        new db(getApplicationContext()).update_not_pay_history(_id,payCheck[k],ss[2]);
                    }
                }
                if(PayTakenImage==null){ }
                else{
                    new ImageOL(getApplicationContext()).saveImage(PayTakenImage,Payfilename);
                }
                startActivity(intent);
                finish();
            }
        });
    }

    public void PayfileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"), PAY_PICKFILE_RESULT_CODE);
    }

    public void PayonLaunchCamera() {
        if(Payfilename=="none"){
            Payfilename = new ImageOL(this).CreatePhotoFileName();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PayPhotoFile = new ImageOL(getApplicationContext()).getPhotoFileUri(Payfilename);
        Uri fileProvider = FileProvider.getUriForFile(PayActivity.this, "com.example.myapplicationc", PayPhotoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent,PAY_Launch_Camera_REQUEST_CODE );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PAY_PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri.toString().startsWith("content://")) {
                        try {
                            String[] projection = {MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATA};
                            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                Uri imageUri = data.getData();
                                Log.d("Tag iamge","clay");
                                PayTakenImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                                /* document_id,mime_type,_display_name,last_modified,flags,_size */
                                Payfilename = cursor.getString(cursor.getColumnIndex("_display_name"));
                                PayAttachment.setText(Payfilename);
                                pay_attachment_cancel_button.setVisibility(View.VISIBLE);
                                Log.d("Tag iamge","clay done");
                            }
                            cursor.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                } else{
                    PayCancelActivity();
                }
                break;
            case PAY_Launch_Camera_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    PayTakenImage = BitmapFactory.decodeFile(PayPhotoFile.getAbsolutePath());
                    PayAttachment.setText(Payfilename);
                    pay_attachment_cancel_button.setVisibility(View.VISIBLE);
                }else{
                    PayCancelActivity();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void PayCancelActivity(){
        PayAttachment.setText("");
        Payfilename="none";
        PayTakenImage=null;
        pay_attachment_cancel_button.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onBackPressed() {
        startActivity(intent);
        finish();
    }
}