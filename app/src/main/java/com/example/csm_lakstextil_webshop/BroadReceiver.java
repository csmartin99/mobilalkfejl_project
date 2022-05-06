package com.example.csm_lakstextil_webshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        new NotificationHandler(context).sendNoti("Check out our new collection!","We add new stuff to our webshop every Monday. Don't miss the opportunity!");
    }
}