package com.example.csm_lakstextil_webshop;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {
    private NotificationManager mNotiMan;
    private Context mCon;
    private static final String C_ID = "webshop_notification_channel";

    public NotificationHandler(Context con) {
        this.mCon = con;
        this.mNotiMan = (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotiChannel();
    }

    private void createNotiChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            return;
        } else {
            NotificationChannel notichannel = new NotificationChannel(C_ID, "Home Textiles WS Notification", NotificationManager.IMPORTANCE_HIGH);
            notichannel.enableVibration(true);
            notichannel.enableLights(true);
            notichannel.setDescription("Check out the latest home textiles");
            notichannel.setLightColor(Color.rgb(11, 189, 26));
            this.mNotiMan.createNotificationChannel(notichannel);
        }
    }

    public void sendNoti(String shortmsg, String longmsg) {
        Intent notiIntent = new Intent(mCon,ProductsActivity.class);
        PendingIntent notiPendingIntent = PendingIntent.getActivity(mCon, 0, notiIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notibuilder = new NotificationCompat.Builder(mCon, C_ID)
                .setSmallIcon(R.drawable.noti_icon)
                .setContentTitle("Home Textiles WS Notification")
                .setContentText(shortmsg)
                .setContentIntent(notiPendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(longmsg));

        this.mNotiMan.notify(0, notibuilder.build());
    }

    public void cancelNoti() {
        this.mNotiMan.cancel(0);
    }
}
