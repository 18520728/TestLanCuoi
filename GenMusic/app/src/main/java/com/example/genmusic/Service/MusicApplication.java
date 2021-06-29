package com.example.genmusic.Service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MusicApplication extends Application {
    public static final String CHANNEL_ID = "MUSIC_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();
        
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "music channel", NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = getSystemService(NotificationManager.class);
            if(manager != null)
            {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
