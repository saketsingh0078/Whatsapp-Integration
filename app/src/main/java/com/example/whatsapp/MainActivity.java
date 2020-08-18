package com.example.whatsapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_ID_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText phoneNumber = findViewById(R.id.phone);
        Button videoCall = findViewById(R.id.videocall);
        Button audioCall = findViewById(R.id.audiocall);
        checkandRequestPermission();
        videoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                //here you have to pass whatsApp contact  number  as  number..
                String number = phoneNumber.getText().toString();
                String name= getContactName( number, MainActivity.this);
                int videoCall=getContactIdForWhatsAppVideoCall(name,MainActivity.this);
                if (videoCall!=0)
                {
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" +videoCall),
                            "vnd.android.cursor.item/vnd.com.whatsapp.video.call");
                    intent.setPackage("com.whatsapp");
                    startActivity(intent);
                }

            }
        });

        audioCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String mimeString = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                String number = phoneNumber.getText().toString();
                String name= getContactName(number , MainActivity.this);
                int whatsappcall=getContactIdForWhatsAppCall(name,MainActivity.this);
                if (whatsappcall!=0) {
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" +whatsappcall),
                            "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                    intent.setPackage("com.whatsapp");

                    startActivityForResult(intent, 121);
                }
            }
        });

    }

    public String getContactName(final String phoneNumber, Context context)
    {
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }


    public  int getContactIdForWhatsAppVideoCall(String name,Context context)
    {
        Cursor  cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=? and "+ContactsContract.Data.MIMETYPE+ "=?",
                new String[] {name,"vnd.android.cursor.item/vnd.com.whatsapp.video.call"},
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.getCount()>0)
        {
            cursor.moveToFirst();
            int phoneContactID=  cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            return phoneContactID;
        }
        else
        {
            System.out.println("Error        ");
            return 0;
        }
    }

    public  int getContactIdForWhatsAppCall(String name,Context context)
    {
        Cursor cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.Data._ID},
                ContactsContract.Data.DISPLAY_NAME + "=? and "+ContactsContract.Data.MIMETYPE+ "=?",
                new String[] {name,"vnd.android.cursor.item/vnd.com.whatsapp.voip.call"},
                ContactsContract.Contacts.DISPLAY_NAME);

        if (cursor.getCount()>0)
        {
            cursor.moveToNext();
            int phoneContactID=  cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
            System.out.println("9999999999999999          name  "+name+"      id    "+phoneContactID);
            return phoneContactID;
        }
        else
        {
            System.out.println("Error          ");
            return 0;
        }
    }

    public boolean checkandRequestPermission()
    {
        int read_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int write_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        int call_phone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        List<String> ls =new ArrayList<>();
        if(read_contacts!= PackageManager.PERMISSION_GRANTED)
        {
            ls.add(Manifest.permission.READ_CONTACTS);
        }
        if(write_contacts!= PackageManager.PERMISSION_GRANTED)
        {
            ls.add(Manifest.permission.WRITE_CONTACTS);
        }
        if(call_phone!= PackageManager.PERMISSION_GRANTED)
        {
            ls.add(Manifest.permission.CALL_PHONE);
        }
        if(!ls.isEmpty())
        {
            ActivityCompat.requestPermissions(this,ls.toArray(new String[ls.size()]),REQ_ID_PERMISSION);
            return false;
        }
        return true;
    }
}
