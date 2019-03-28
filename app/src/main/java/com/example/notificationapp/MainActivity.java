package com.example.notificationapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID=0;
    private Button notifButton;
    private Button updateButton;
    private Button cancelButton;
    private static final String NOTIFICATION_URL="https://google.com";
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.notificationapp.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_CANCEL_NOTIFICATION =
            "com.example.notificationapp.ACTION_CANCEL_NOTIFICATION";
    private NotificationReceiver receiver = new NotificationReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifButton = findViewById(R.id.notify);
        updateButton = findViewById(R.id.update);
        cancelButton = findViewById(R.id.cancel);
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notifButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        intentFilter.addAction(ACTION_CANCEL_NOTIFICATION);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    private void updateNotification(){
        Bitmap image = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent
                .getActivity(this,NOTIFICATION_ID,notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_URL));
        PendingIntent learnMorePendingIntent = PendingIntent
                .getActivity(this,NOTIFICATION_ID,learnMoreIntent
                        ,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("TITLE")
                        .setContentText("Content")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(R.mipmap.ic_launcher, "Learn More",learnMorePendingIntent)
                        .setContentIntent(notificationPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image)
                            .setBigContentTitle("Notification Updated"));
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }
    private void cancelNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
    }
    private void sendNotification(){
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent
                .getActivity(this,NOTIFICATION_ID,notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NOTIFICATION_URL));
        PendingIntent learnMorePendingIntent = PendingIntent
                .getActivity(this,NOTIFICATION_ID,learnMoreIntent
                        ,PendingIntent.FLAG_ONE_SHOT);

        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent
                .getBroadcast(this,NOTIFICATION_ID,updateIntent
                        ,PendingIntent.FLAG_ONE_SHOT);

        Intent cancelIntent = new Intent(ACTION_CANCEL_NOTIFICATION);
        PendingIntent cancelPendingIntent = PendingIntent
                .getBroadcast(this,NOTIFICATION_ID,cancelIntent
                        ,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifyBuilder =
                    new NotificationCompat.Builder(this)
                        .setContentTitle("TITLE")
                        .setContentText("Content")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .addAction(R.mipmap.ic_launcher, "Learn More",learnMorePendingIntent)
                        .addAction(R.mipmap.ic_launcher, "Update",updatePendingIntent)
                        .addAction(R.mipmap.ic_launcher, "Close", cancelPendingIntent)
                        .setContentIntent(notificationPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }

    private class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
                case ACTION_CANCEL_NOTIFICATION:
                    cancelNotification();
                    break;
            }
        }
    }
}
