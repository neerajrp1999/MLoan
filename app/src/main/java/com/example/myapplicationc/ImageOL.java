package com.example.myapplicationc;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import com.google.android.material.snackbar.Snackbar;

import android.widget.NumberPicker;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageOL {
    Context context;
    String appName="My Application c";
    ImageOL(Context context){
        this.context=context;
    }

    String imagepath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + appName + File.separator + "Attachment" ;
    String imagepath1= Environment.DIRECTORY_PICTURES + File.separator + appName + File.separator + "Attachment" ;
    public void saveImage(Bitmap bitmap,String imageName){
        OutputStream fos;
        try{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Log.d("Tag1","Image Content");
                ContentResolver resolver=context.getContentResolver();
                ContentValues contentValues=new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,imageName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH,imagepath1);
                Uri imageUri=resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                fos= resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                Objects.requireNonNull(fos);
                Log.d("Tag1","Image Content done");
                Toast.makeText(context.getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Log.d("Tag image","Error " +e.toString());
        }
    }

    public void openImage(String filename){
        String pa1=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ File.separator + appName + File.separator + "Attachment" + File.separator +filename.trim();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(pa1), "image/*");
        context.startActivity(intent);
    }

    public String CreatePhotoFileName() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(" HHmmssyyyyMMdd");
        return dateFormat.format(currentTime).trim()+".jpg";
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(imagepath);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

}
