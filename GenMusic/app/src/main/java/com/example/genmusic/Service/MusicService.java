package com.example.genmusic.Service;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.genmusic.MainActivity;
import com.example.genmusic.MinimizedPlayerFragment;
import com.example.genmusic.R;
import com.example.genmusic.ThreadSafeSingleton;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.genmusic.Service.MusicApplication.CHANNEL_ID;

public class MusicService extends Service {
    public static MediaPlayer mediaPlayer = ThreadSafeSingleton.getSingletonInstance();
    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_RESUME = 2;
    private static final int ACTION_NEXT = 4;
    private static final int ACTION_PREVIOUS = 5;

    //Interface truyền action xuống activity khi thực hiện action trên notification
    private ISendActionListener iSendActionListener;
    public interface ISendActionListener
    {
        void sendAction(int action);
    }
    //đăng ký activity với service này
    public void registerClient(Activity activity)
    {
        iSendActionListener = (ISendActionListener) activity;
    }

    private Baihatuathich mbaihat;

    private MusicBinder musicBinder= new MusicBinder();
    public class MusicBinder extends Binder{
        public MusicService getMusicService()
        {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("service","onBind");
        return musicBinder;
    }

    @Override
    public void onCreate() {
        Log.e("service","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("service","onStartCommand");
        //Nhận dữ liệu từ activity và xử lý
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            Baihatuathich baihat = (Baihatuathich) bundle.get("baihat");
            if(baihat != null)
            {
                if(intent.hasExtra("LastSongFromMain"))
                {
                    mbaihat = baihat;
                    sendNotification(baihat);
                }
                else
                {
                    mbaihat = baihat;
                    playSong(baihat);
                    sendNotification(baihat);
                }
            }
        }
        //lấy action_music từ broadcastReceiver truyền qua
        int actionMusic = -1;
        actionMusic = intent.getIntExtra("action_music_service", -1);
        if(actionMusic != -1)
        {
            handleActionMusic(actionMusic);
        }
        return START_NOT_STICKY;
    }

    private void sendNotification(Baihatuathich baihat) {
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(baihat.getTenbaihat())
                .setContentText(baihat.getCasi())
                .setSmallIcon(R.drawable.ic_baseline_music)
                .setLargeIcon(getBitmapFromURL(baihat.getHinhbaihat()))
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()));
        //if else xử lý 2 trạng thái nút play-pause
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            //Các button điều khiển nhạc
                builder.addAction(R.drawable.icon_previous_black, "Previous", getPendingIntent(this, ACTION_PREVIOUS)) //#0
                .addAction(R.drawable.iconpause_black, "Pause", getPendingIntent(this, ACTION_PAUSE)) //#1
                .addAction(R.drawable.iconnext_black, "Next", getPendingIntent(this,ACTION_NEXT)); //#2
        }
        else
        {
            //Các button điều khiển nhạc
                builder.addAction(R.drawable.icon_previous_black, "Previous", getPendingIntent(this, ACTION_PREVIOUS)) //#0
                        .addAction(R.drawable.iconplay_black, "Pause", getPendingIntent(this, ACTION_RESUME)) //#1
                        .addAction(R.drawable.iconnext_black, "Next", getPendingIntent(this,ACTION_NEXT)); //#2
        }
        Notification notification = builder.build();

        startForeground(1, notification);
    }

    public void playSong(Baihatuathich baihat) {
        try
        {

            if(mediaPlayer == null)
            {
                mediaPlayer=new MediaPlayer();
            }
            else if(mediaPlayer != null)
            {
                mediaPlayer.reset();
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });

            mediaPlayer.setDataSource(baihat.getLinkbaihat());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    private void handleActionMusic(int action)
    {
        switch(action)
        {
            case ACTION_PAUSE:
                pauseSong();
                iSendActionListener.sendAction(ACTION_PAUSE);
                break;

            case ACTION_RESUME:
                resumeSong();
                iSendActionListener.sendAction(ACTION_RESUME);
                break;

            case ACTION_NEXT:
                iSendActionListener.sendAction(ACTION_NEXT);
                break;

            case ACTION_PREVIOUS:
                iSendActionListener.sendAction(ACTION_PREVIOUS);
                break;
        }
    }

    //Các action tương tác trên NOTIFICATION và ACTIVITY tương tác với SERVICE thông qua BoundService
    public void pauseSong()
    {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
            sendNotification(mbaihat);

        }
    }
    public void resumeSong()
    {
        if(MainActivity.SHOW_MINIMIZED_PLAYER)
        {
            playSong(mbaihat);
            MainActivity.SHOW_MINIMIZED_PLAYER = false;
        }
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            sendNotification(mbaihat);
        }

    }
    //Trả về pending intent chứa action từ broadcastReceiver
    private PendingIntent getPendingIntent(Context context, int action)
    {
        Intent intent = new Intent(this, MusicReceiver.class);
        intent.putExtra("action_music",action);

        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("service","onUnBind");

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e("service","onDestroy");
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }



}
