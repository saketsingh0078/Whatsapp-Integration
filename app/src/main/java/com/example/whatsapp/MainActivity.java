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
                String number = phoneNumber.getText().toString();
                int videoCall=getContactIdForWhatsAppVideoCall(number,MainActivity.this);
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                String number = phoneNumber.getText().toString();
                int whatsappcall=getContactIdForWhatsAppCall(number,MainActivity.this);
                if (whatsappcall!=0) {
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" +whatsappcall),
                            "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                    intent.setPackage("com.whatsapp");

                    startActivityForResult(intent, 121);
                }
            }
        });
    }

    public  int getContactIdForWhatsAppVideoCall(String contactNumber,Context context)
    {
        Cursor cursor = context.getContentResolver ().query (
                        ContactsContract.Data.CONTENT_URI,
                        new String [] { ContactsContract.Data._ID },
                        ContactsContract.RawContacts.ACCOUNT_TYPE + " = 'com.whatsapp' " +
                                "AND " + ContactsContract.Data.MIMETYPE + " = 'vnd.android.cursor.item/vnd.com.whatsapp.video.call' " +
                                "AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + contactNumber + "%'",
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME
                );

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

    public  int getContactIdForWhatsAppCall(String contactNumber,Context context)
    {
        Cursor cursor = context.getContentResolver ()
                .query (
                        ContactsContract.Data.CONTENT_URI,
                        new String [] { ContactsContract.Data._ID },
                        ContactsContract.RawContacts.ACCOUNT_TYPE + " = 'com.whatsapp' " +
                                "AND " + ContactsContract.Data.MIMETYPE + " = 'vnd.android.cursor.item/vnd.com.whatsapp.voip.call' " +
                                "AND " + ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + contactNumber + "%'",
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME
                );

        if (cursor.getCount()>0)
        {
            cursor.moveToNext();
            int phoneContactID=  cursor.getInt(cursor.getColumnIndex(ContactsContract.Data._ID));
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
