package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AsyncPlayer;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.genmusic.MainActivity.SHOW_PLAYER;
import static com.example.genmusic.Service.MusicService.mediaPlayer;
import static com.example.genmusic.MainActivity.musicService;
import static com.example.genmusic.MainActivity.isServiceConnected;
import static com.example.genmusic.MainActivity.mangbaihat;
import static com.example.genmusic.MainActivity.position;


public class PlayNhacActivity extends AppCompatActivity implements MusicService.ISendActionListener {
    Toolbar toolbarplaynhac;
    TextView txtTimesong,txtTotaltimesong;
    SeekBar sktime;
    ImageButton imgplay,imgrepeat,imgnext,imgpre,imgrandom;
    ViewPager viewPagerplaynhac;
    ImageView imgYeuThichTrongPlayer;
    //lấy tên đăng nhập từ firebase
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private String tendangnhap;

    //ACTION NOTIFICATION
    private static final int ACTION_PAUSE = 1;
    private static final int ACTION_RESUME = 2;
    private static final int ACTION_NEXT = 4;
    private static final int ACTION_PREVIOUS = 5;
    //SERVICE
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getMusicService();
            isServiceConnected = true;
            musicService.registerClient(PlayNhacActivity.this);
            Log.e("service","service connection");
            handleLayoutMusic();
            setPlayPauseButtonStatus();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isServiceConnected = false;
        }
    };
    //lưu dữ liệu bài hát vào sharedpreferences
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_PATH = "MUSIC_LINK";
    public static final String MUSIC_PICTURE = "MUSIC_PICTURE_LINK";
    public static final String SONG_ID = "SONG_ID";
    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ARTIST = "SONG_ARTIST";
    public static final String SONG_LIKE = "SONG_LIKE";



    public static ViewPagerPlaylistnhac adapternhac;
    public Fragment_Dia_nhac fragment_dia_nhac;
    public Fragment_Play_DanhsachbaiHat fragment_play_danhsachbaiHat;
    private Dataservice dataservice = APIService.getService();

    public static boolean repeat=false;
    public static boolean checkrandom=false;
    public static boolean next=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac);

        //Khôi phục lại trạng thái của activity trước đó trong trường hợp:
            //+ mở lại activity này từ minimized player
        if(savedInstanceState != null)
        {
            mangbaihat = savedInstanceState.getParcelableArrayList("mangbaihat");
            position = savedInstanceState.getInt("position");
            repeat = savedInstanceState.getBoolean("repeat");
            checkrandom = savedInstanceState.getBoolean("checkrandom");
            next = savedInstanceState.getBoolean("next");
            Log.e("play","PLAY restart repeat = " + repeat);
        }

        StrictMode.ThreadPolicy policy=new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        tendangnhap = auth.getCurrentUser().getEmail();
        GetDataFromIntent();
        init();
        eventClick();
    }

    //Lưu trạng thái của activity (trong trường hợp bị finish() )
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList("mangbaihat",mangbaihat);
        outState.putInt("position",position);
        outState.putBoolean("repeat",repeat);
        outState.putBoolean("checkrandom",checkrandom);
        outState.putBoolean("next",next);
        Log.e("play","PLAY finish repeat = " + repeat);
    }

    private void handleLayoutMusic()
    {
        UpdateLuotNghe(mangbaihat.get(position).getIdbaihat());
        TimeSong();
        UpdateTime();
    }
    private void init() {
        //ánh xạ
        toolbarplaynhac=findViewById((R.id.toolbarplaynhac));
        txtTimesong=findViewById(R.id.textviewtimesong);
        txtTotaltimesong=findViewById(R.id.textviewtotaltimesong);
        sktime =findViewById(R.id.seekbarsong);
        imgplay=findViewById(R.id.imagebuttonplay);
        imgrepeat=findViewById(R.id.imagebuttonrepeat);
        imgnext=findViewById(R.id.imagebuttonnext);
        imgrandom=findViewById(R.id.imagebuttonsuffle);
        imgpre=findViewById(R.id.imagebuttonpre);
        viewPagerplaynhac=findViewById(R.id.viewpagerplaynhac);
        imgYeuThichTrongPlayer = findViewById(R.id.imgYeuThichTrongPlayer);

        if(checkrandom)
        {
            imgrandom.setImageResource(R.drawable.iconshuffled);
        }
        if(repeat)
        {
            imgrepeat.setImageResource(R.drawable.iconrepeat_one);
        }

        //toolbar
        setSupportActionBar(toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Back về main
        toolbarplaynhac.setNavigationOnClickListener((v) ->
        {
            finish();
        });
        toolbarplaynhac.setTitleTextColor(Color.WHITE);

        //thêm fragment vào viewpager
        fragment_dia_nhac=new Fragment_Dia_nhac();
        fragment_play_danhsachbaiHat=new Fragment_Play_DanhsachbaiHat();
        adapternhac= new ViewPagerPlaylistnhac((getSupportFragmentManager()));
        adapternhac.AddFragment(fragment_play_danhsachbaiHat);
        adapternhac.AddFragment(fragment_dia_nhac);
        viewPagerplaynhac.setAdapter(adapternhac);
        viewPagerplaynhac.setCurrentItem(1);

        fragment_dia_nhac= (Fragment_Dia_nhac) adapternhac.getItem(1);

        //Nếu có bài hát trong mảng thì khởi chạy player
        if(mangbaihat.size()>0)
        {
            getSupportActionBar().setTitle(mangbaihat.get(position).getTenbaihat());

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (adapternhac.getItem(1)!=null){
                        if (mangbaihat.size()>0){
                            fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhbaihat());
                            handler.removeCallbacks(this);

                        }else {
                            handler.postDelayed(this,300);

                        }
                    }
                }
            },500);

            //chạy service để play nhạc
            setOnMusicService();
        }

    }
    private void eventClick() {


        //PLAY-PAUSE
        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer!=null && musicService != null){
                    if(mediaPlayer.isPlaying()){

                        //mediaPlayer.pause();
                        musicService.pauseSong();
                        imgplay.setImageResource(R.drawable.iconplay);
                    }
                    else  {
                        //mediaPlayer.start();
                        musicService.resumeSong();
                        imgplay.setImageResource(R.drawable.iconpause);
                    }
                }
                else
                {
                    //chạy service để play nhạc
                    setOnMusicService();
                }
            }

        });
        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false)
                {
                    if (checkrandom == true)
                    {
                        checkrandom = false;
                        imgrepeat.setImageResource(R.drawable.iconrepeat_one);
                        imgrandom.setImageResource(R.drawable.iconsuffle);
                    }
                    imgrepeat.setImageResource(R.drawable.iconrepeat_one);
                    repeat = true;
                }
                else
                {
                    imgrepeat.setImageResource(R.drawable.iconrepeat_white);
                    repeat = false;
                }
            }
        });
        imgrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkrandom == false) {
                    if (repeat == true) {
                        repeat = false;
                        imgrandom.setImageResource(R.drawable.iconshuffled);
                        imgrepeat.setImageResource(R.drawable.iconrepeat_white);

                    }
                    imgrandom.setImageResource(R.drawable.iconshuffled);
                    checkrandom = true;
                } else {
                    imgrandom.setImageResource(R.drawable.iconsuffle);
                    checkrandom = false;
                }
            }

        });
        sktime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //NEXT
        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
            }
        });

        //PREVIOUS
        imgpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousSong();
            }
        });
    }

    private void GetDataFromIntent() {
        Intent intent=getIntent();
        if(intent!=null)
        {
            if(intent.hasExtra("cakhuc")){
                mangbaihat.clear();
                Baihatuathich baihatuathich=intent.getParcelableExtra("cakhuc");
                mangbaihat.add(baihatuathich);
            }
            if(intent.hasExtra("cacbaihat")){
                mangbaihat.clear();
                ArrayList<Baihatuathich> mangBaiHat=intent.getParcelableArrayListExtra("cacbaihat");
                mangbaihat=mangBaiHat;
                }
            }
        }


    public void UpdateLuotNghe(String idbaihat)
    {
        Log.d("hiep","BAT DAU update luot nghe cho id: " + idbaihat);
        //++luotnghe
        Call<String> callbackLuotNghe=dataservice.UpdateLuotThich(idbaihat);
        callbackLuotNghe.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                Log.d("hiep","update luot nghe id " + idbaihat + " = " + kq);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
    public void TimeSong() {
        Log.e("service","max time: " + mediaPlayer.getDuration());
        Handler handlertime = new Handler();
        handlertime.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null)
                {

                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
                    txtTotaltimesong.setText((simpleDateFormat.format(mediaPlayer.getDuration())));
                    sktime.setMax(mediaPlayer.getDuration());
                }

            }
        },2000);

    }

    public void UpdateTime(){
        Handler handler=new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    sktime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("mm:ss");
                    txtTimesong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this,300);

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next=true;
                            Handler handlerwait = new Handler();

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    continueSong();
                                }
                            }, 1000);
                        }
                    });

                }
            }
        },300);


    }

    private void continueSong()
    {
        //Mở bài hát tiếp theo khi nghe hết bài hát
        Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next==true){
                    if(position<(mangbaihat.size())){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        if(repeat==true){
                            if(position==0){
                                position=mangbaihat.size();
                            }
                            position-=1;
                        }
                        if(checkrandom==true){
                            Random random=new Random();
                            int index=random.nextInt(mangbaihat.size());
                            if(index==position){
                                position=index-1;
                            }
                            position=index;
                        }
                        if(position>mangbaihat.size()-1){
                            position=0;
                        }
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenbaihat());
                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhbaihat());

                        Intent intentService = new Intent(PlayNhacActivity.this, MusicService.class);
                        //Gửi bài hát sang service
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("baihat", mangbaihat.get(position));
                        intentService.putExtras(bundle);
                        startService(intentService);
                        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
                        Handler handlerlayout = new Handler();
                        handlerlayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handleLayoutMusic();
                                //Lưu dữ liệu vào SharedPreferences
                                createSharedPreferences(mangbaihat.get(position));
                            }
                        }, 1000);
                        addToFavoriteSong(mangbaihat.get(position));
                        //setPlayPauseButtonStatus();

                    }
                    next=false;
                    handler1.removeCallbacks(this);
                }else {
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);
    }
    private void setOnMusicService() {
        //SERVICE
        //Nếu không phải mở activity này từ mini player thì chạy mới service
        Intent intentmini = getIntent();
        if(MainActivity.SHOW_PLAYER && intentmini.hasExtra("miniplayer"))
        {
            Intent intentService = new Intent(PlayNhacActivity.this, MusicService.class);
            bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
            SHOW_PLAYER = false;
        }
        else if(!intentmini.hasExtra("miniplayer"))
        {
            Intent intentService = new Intent(PlayNhacActivity.this, MusicService.class);
            //Gửi bài hát sang service
            Bundle bundle = new Bundle();
            bundle.putParcelable("baihat", mangbaihat.get(position));
            intentService.putExtras(bundle);

            startService(intentService);
            bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        else
        {
            handleLayoutMusic();
        }
        Handler handlerlayout = new Handler();
        handlerlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Lưu dữ liệu vào SharedPreferences
                createSharedPreferences(mangbaihat.get(position));
            }
        }, 1000);
        addToFavoriteSong(mangbaihat.get(position));
        setPlayPauseButtonStatus();
    }

    //Xử lí sự kiện nút trái tim thêm vào bài hát yêu thích
    public void addToFavoriteSong(Baihatuathich baihatuathich)
    {

        //Kiểm tra bài hát đã có trong baihatyeuthich hay chưa

        Call<String> callbackKiemTra = dataservice.KiemTraBaiHatYeuThich(tendangnhap, baihatuathich.getIdbaihat());
        callbackKiemTra.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua = response.body();
                if(ketqua.equals("1"))
                {
                    imgYeuThichTrongPlayer.setImageResource(R.drawable.ic_loved);
                }
                if(ketqua.equals("0"))
                {
                    imgYeuThichTrongPlayer.setImageResource(R.drawable.ic_love_white);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //Sự kiện click nút trái tim thêm vào hoặc xóa khỏi bài hát yêu thích
        imgYeuThichTrongPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //truyền tendangnhap, idbaihat
                Call<String> callback = dataservice.InsertOrDeleteBaiHatYeuThich(tendangnhap, baihatuathich.getIdbaihat());
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("successful_insert"))
                        {
                            Toast.makeText(PlayNhacActivity.this, "Đã thêm " + baihatuathich.getTenbaihat() + " vào Bài hát yêu thích", Toast.LENGTH_SHORT).show();
                            imgYeuThichTrongPlayer.setImageResource(R.drawable.ic_loved);
                        }
                        else if(kq.equals("failed_insert"))
                        {
                            Toast.makeText(PlayNhacActivity.this, "Có lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        }
                        else if(kq.equals("successful_delete"))
                        {
                            Toast.makeText(PlayNhacActivity.this, "Đã xóa " + baihatuathich.getTenbaihat() + " khỏi Bài hát yêu thích", Toast.LENGTH_SHORT).show();
                            imgYeuThichTrongPlayer.setImageResource(R.drawable.ic_love_white);
                        }
                        else if(kq.equals("failed_delete"))
                        {
                            Toast.makeText(PlayNhacActivity.this, "Có lỗi khi xóa bài hát", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void createSharedPreferences(Baihatuathich baihat)
    {
        SharedPreferences.Editor editor = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE)
                .edit();
        editor.putString(MUSIC_PATH,baihat.getLinkbaihat());
        editor.putString(MUSIC_PICTURE, baihat.getHinhbaihat());
        editor.putString(SONG_ID, baihat.getIdbaihat());
        editor.putString(SONG_NAME, baihat.getTenbaihat());
        editor.putString(SONG_ARTIST, baihat.getCasi());
        editor.putString(SONG_LIKE, baihat.getLuotthich());
        editor.apply();
    }

    private void setPlayPauseButtonStatus()
    {
        if(musicService != null && mediaPlayer != null)
        {
            if(mediaPlayer.isPlaying())
            {
                imgplay.setImageResource(R.drawable.iconpause);
            }
            else
            {
                imgplay.setImageResource(R.drawable.iconplay);
            }
        }
    }

    //Nhận action điều khiển nhạc từ notification
    @Override
    public void sendAction(int action) {
        switch(action)
        {
            case ACTION_PAUSE:
                Log.e("service","pause tu notification");
                imgplay.setImageResource(R.drawable.iconplay);
                break;

            case ACTION_RESUME:
                imgplay.setImageResource(R.drawable.iconpause);
                break;

            case ACTION_NEXT:
                nextSong();
                break;

            case ACTION_PREVIOUS:
                previousSong();
                break;
        }
    }

    private void nextSong()
    {
        if (mangbaihat.size() > 0) {
            if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (position < (mangbaihat.size())) {
                imgplay.setImageResource(R.drawable.iconpause);
                Log.e("play", "first position = " + position);
                position++;
                Log.e("play", "repeat = " + repeat);
                if (repeat == true)
                {
                        position -= 1;
                }
                Log.e("play", "checkrandom = " + checkrandom);
                if (checkrandom == true) {
                    Random random = new Random();
                    int index = random.nextInt(mangbaihat.size() + 1 - 1);
                    Log.e("play", "index = " + index);
                    Log.e("play", "added position = " + position);
                    if((position - 1) == index)
                    {
                        if((position - 1) == 0)
                            position = mangbaihat.size() - 1;
                        else
                            position = index - 1;
                    }
                    else
                        position = index;
                }
                if (position > mangbaihat.size() - 1) {
                    position = 0;
                }
                Log.e("play", "last position = " + position);
                getSupportActionBar().setTitle(mangbaihat.get(position).getTenbaihat());
                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhbaihat());

                Intent intentService = new Intent(PlayNhacActivity.this, MusicService.class);
                //Gửi bài hát sang service
                Bundle bundle = new Bundle();
                bundle.putParcelable("baihat", mangbaihat.get(position));
                intentService.putExtras(bundle);
                startService(intentService);
                bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);

                Handler handlerlayout = new Handler();
                handlerlayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleLayoutMusic();
                    }
                }, 1000);

                //Lưu dữ liệu vào SharedPreferences để phát nhạc trong minimizedPlayer
                createSharedPreferences(mangbaihat.get(position));
                addToFavoriteSong(mangbaihat.get(position));
                setPlayPauseButtonStatus();
            }

        }
        imgpre.setClickable(false);
        imgnext.setClickable(false);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgpre.setClickable(true);
                imgnext.setClickable(true);
            }
        }, 1000);
    }

    private void previousSong() {
        //PREVIEW
        if (mangbaihat.size() > 0) {
            if (mediaPlayer.isPlaying() || mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (position < (mangbaihat.size())) {
                imgplay.setImageResource(R.drawable.iconpause);
                Log.e("play", "PREVIOUS first position = " + position);
                position--;
                if (position < 0) {
                    position = mangbaihat.size() - 1;
                }
                if (repeat == true)
                {
                    position += 1;
                }
                if (checkrandom == true) {
                    Random random = new Random();
                    int index = random.nextInt(mangbaihat.size() + 1 - 1);
                    Log.e("play", "PREVIOUS index = " + index);
                    Log.e("play", "PREVIOUS minored position = " + position);
                    if ((position + 1) == index)
                    {
                        if((position + 1) == 0)
                            position = mangbaihat.size() - 1;
                        else
                            position = index + 1;
                    }
                    else
                        position = index;
                }
                if (position < 0)
                {
                    position = mangbaihat.size() - 1;
                }
                Log.e("play", "PREVIOUS last position = " + position);
                getSupportActionBar().setTitle(mangbaihat.get(position).getTenbaihat());
                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhbaihat());

                Intent intentService = new Intent(PlayNhacActivity.this, MusicService.class);
                //Gửi bài hát sang service
                Bundle bundle = new Bundle();
                bundle.putParcelable("baihat", mangbaihat.get(position));
                intentService.putExtras(bundle);
                startService(intentService);
                bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
                Handler handlerlayout = new Handler();
                handlerlayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleLayoutMusic();
                    }
                }, 1000);
                //Lưu dữ liệu vào SharedPreferences để phát nhạc trong minimizedPlayer
                createSharedPreferences(mangbaihat.get(position));
                addToFavoriteSong(mangbaihat.get(position));
                setPlayPauseButtonStatus();
            }

        }
        imgpre.setClickable(false);
        imgnext.setClickable(false);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgpre.setClickable(true);
                imgnext.setClickable(true);
            }
        }, 1000);
    }


    @Override
    protected void onResume() {
        Log.e("activity","PlayNhacActivity onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.e("activity","PlayNhacActivity onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("activity","PlayNhacActivity onDestroy");
        super.onDestroy();
    }
}