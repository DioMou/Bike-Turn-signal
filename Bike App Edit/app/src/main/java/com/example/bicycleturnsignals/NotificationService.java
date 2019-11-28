package com.example.bicycleturnsignals;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

//import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NotificationService extends NotificationListenerService {

    Context context;

    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();

    }
    @Override

    // This is fired whenever a new notification appears. For google maps navigation this happens about every second
    // Most of the code in this function is just to print all the data to logcat
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Test if the notification is from google maps
        String pack = sbn.getPackageName();
        if (!pack.equals("com.google.android.apps.maps")) {
            return;
        }
        // Some string
        String ticker = "";
        if (sbn.getNotification().tickerText != null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        // Extras is a bundle that contains basically all of the notification data
        Bundle extras = sbn.getNotification().extras;
        if (extras.keySet() != null) {
            // Print all the variables in extras
            Object[] keys = extras.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] instanceof String && extras.get((String) keys[i]) != null) {
                    Log.i((String) keys[i], extras.get((String) keys[i]).toString());
                }
            }
            Log.i("KEYSET", extras.keySet().toString());
        }
        if ( extras.getString("android.subText") != null) {
            Log.i("SUBTEXT", extras.getString("android.subText"));
        }
        //View cv = (View) extras.get("android.contains.customView");
        //cv.getSourceLayoutResId();
        String title = extras.getString("android.title");
        String text = "";
        if (extras.getCharSequence("android.text") != null) {
            text = extras.getCharSequence("android.text").toString();
        }

        // Useless
        Log.i("AX", "getting icon");
        int iconId = extras.getInt(Notification.EXTRA_LARGE_ICON_BIG);
        Log.i("ICON", String.valueOf(iconId));

        // Get the arrow icon
        if (extras.containsKey(Notification.EXTRA_LARGE_ICON)) {
            Bitmap bitmap = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
            // Calculate a hash that uniquely identifies the arrow image
            String hash = String.valueOf(bitmapHash(bitmap));

            // Send that hash to the main activity to show the arrow in the app (You don't need that)
            SharedPreferences prefs = getSharedPreferences("arrowInts", 0);
            if (prefs.getInt(hash, 5) == 5) {
                new ImageSaver(getApplicationContext())
                        .setFileName(hash)
                        .save(bitmap);
                prefs.edit().putInt(hash, Directions.Companion.getUNKNOWN()).apply();
            }

            Intent intent = new Intent();
            intent.setAction("com.example.bicycleturnsignals");
            intent.putExtra("DATAPASSED", hash);
            sendBroadcast(intent);

            Log.i("IMG HASH", String.valueOf(bitmapHash(bitmap)));
        }

        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        if (title != null) {
            Log.i("Title", title);
        }
        Log.i("Text",text);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);

        //LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);


    }

    private int bitmapHash(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] buffer = new int[width * height];
        bitmap.getPixels(buffer, 0, width, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        return Arrays.hashCode(buffer);
    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
