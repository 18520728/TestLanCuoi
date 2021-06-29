package com.example.genmusic.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra("action_music", 0);
        Intent intentService = new Intent(context, MusicService.class);
        intentService.putExtra("action_music_service", actionMusic);

        context.startService(intentService);
    }
}
