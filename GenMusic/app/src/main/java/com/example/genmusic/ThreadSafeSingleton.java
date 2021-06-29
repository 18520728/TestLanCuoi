package com.example.genmusic;

import android.app.Application;
import android.media.MediaPlayer;

public final class ThreadSafeSingleton extends Application {

    static MediaPlayer instance;

    public static MediaPlayer getSingletonInstance(){
        if(instance == null)
        {
            synchronized (MediaPlayer.class)
            {
                if(instance == null)
                {
                    instance = new MediaPlayer();
                }
            }
        }

        return instance;
    }
}
