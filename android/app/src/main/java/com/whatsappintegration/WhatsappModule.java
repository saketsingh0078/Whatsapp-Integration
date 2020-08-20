package com.whatsappintegration;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class WhatsappModule extends ReactContextBaseJavaModule {

    ReactApplicationContext context = getReactApplicationContext();
    public WhatsappModule(@NonNull ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "WhatsAppModule";
    }

    @ReactMethod
    public void NavigatetoWhatsapp(){
        Intent intent = new Intent (context,WhatsappHome.class);
        if(intent.resolveActivity(context.getPackageManager())!=null)
        {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
