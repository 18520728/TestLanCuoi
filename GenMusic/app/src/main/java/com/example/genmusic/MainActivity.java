package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MinimizedPlayerFragment.ISendDataListener {

    //-----------------------A. Khai báo biến -------------------------
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    private ImageButton btnUser, btnSetting;
    private EditText edtSearch;
    private Intent intent;

    //SharedPreferences
    public static final String ACTIVITY_STATE ="ACTIVITY_STATE";
    //Hằng final từ Player gửi qua main (chỉ có thể gán giá trị 1 lần bởi player)
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    //Dữ liệu từ player gửi qua
    public static final String MUSIC_PATH = "MUSIC_LINK";
    public static final String MUSIC_PICTURE = "MUSIC_PICTURE_LINK";
    public static final String SONG_ID = "SONG_ID";
    public static final String SONG_NAME = "SONG_NAME";
    public static final String SONG_ARTIST = "SONG_ARTIST";
    public static final String SONG_LIKE = "SONG_LIKE";

    //Biến public từ main gửi cho minimized player
    public static boolean SHOW_MINIMIZED_PLAYER = false;
    public static boolean SHOW_PLAYER = false;
    public static String PATH_TO_FRAG = null;
    public static String PICTURE_TO_FRAG = null;
    public static String ID_TO_FRAG = null;
    public static String NAME_TO_FRAG = null;
    public static String ARTIST_TO_FRAG = null;
    public static boolean ISPLAYING_TO_FRAG = false;

    //SERVICE
    public static ArrayList<Baihatuathich> mangbaihat= new ArrayList<>();
    public static int position = 0;
    public static MusicService musicService;
    public static boolean isServiceConnected = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getMusicService();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isServiceConnected = false;
        }
    };
    FirebaseAuth auth= FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-----------------------b. Ánh xạ view -------------------------
        bottomNavigationView = findViewById(R.id.BottomNav);
        viewPager = findViewById(R.id.ViewPager);

        btnUser = findViewById(R.id.btnUser);
        btnSetting = findViewById(R.id.btnSetting);
        edtSearch = findViewById(R.id.edtSearch);

        //-----------------------C. Code xử lý -------------------------
        //Nhận tên đăng nhập từ màn hình login
        getIntentFromLogin();

        //NAVIGATION BAR------------------------
        //Xử lý ViewPager
        setOnViewPager();

        //Xử lý BottomNavigationBar
        setOnNavBar();

        //Xử lý quay về đúng TheLoaiFragment
        backToExactlyFragment();

        //Đi đến Search Activity
        goToSearchActivity();
        //Đi đến User Setting Activity
        goToUserActivity();
        //Đi đến Setting Activity
        goToSettingActivity();

        //service
        //Intent intentService = new Intent(MainActivity.this, MusicService.class);
        //bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);

        //Dữ liệu bài hát cũ
        loadLastPLayedSong();
    }

    private void loadLastPLayedSong() {
        SharedPreferences preferences = getSharedPreferences(MUSIC_LAST_PLAYED,MODE_PRIVATE);
        String MusicPath = preferences.getString(MUSIC_PATH,null);
        String MusicPicture = preferences.getString(MUSIC_PICTURE, null);
        String SongId = preferences.getString(SONG_ID, null);
        String SongName = preferences.getString(SONG_NAME, null);
        String SongArtist = preferences.getString(SONG_ARTIST, null);
        String SongLike = preferences.getString(SONG_LIKE, null);

        if(MusicPath != null && SongName != null && SongArtist != null && SongId != null && SongLike != null)
        {
            SHOW_MINIMIZED_PLAYER = true;
            SHOW_PLAYER = true;
            Baihatuathich bh = new Baihatuathich(SongId, SongName, MusicPicture,SongArtist, MusicPath, SongLike);
            mangbaihat.add(bh);
            Intent intentService = new Intent(MainActivity.this, MusicService.class);
            //Gửi bài hát sang service
            Bundle bundle = new Bundle();
            bundle.putParcelable("baihat", mangbaihat.get(position));
            intentService.putExtra("LastSongFromMain","dontplaymusic");
            intentService.putExtras(bundle);
            startService(intentService);
            bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.e("activity","MAIN load mangbaihat = " + mangbaihat.size());
        }
        else
        {
            SHOW_MINIMIZED_PLAYER = false;
            SHOW_PLAYER = false;
            MusicPath = null;
            MusicPicture = null;
            SongId = null;
            SongName = null;
            SongArtist = null;
            SongLike = null;
        }
    }

    //Các thao tác khi MainActivity được mở lại
    @Override
    protected void onResume() {
        super.onResume();

    }
    // CÁC HÀM XỬ LÝ -----------------------------------------------------
    private void getIntentFromLogin() {
        Intent intentLogin = getIntent();
        if(intentLogin.hasExtra("login"));
        {
            Dataservice dataservice = APIService.getService();
            Call<String> callback = dataservice.InsertUser(auth.getCurrentUser().getEmail());
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String ketqua = response.body();
                    Log.d("dangnhap","ket qua dang nhap: " + ketqua);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }


    private void setOnNavBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.menuTrangChu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menuTheLoai:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menuXepHang:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menuCaNhan:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    private void setOnViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        //Xử lí sự kiện vuốt ngang màn hình
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position)
                {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menuTrangChu).setChecked(true);
                        break;

                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menuTheLoai).setChecked(true);
                        break;

                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menuXepHang).setChecked(true);
                        break;

                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menuCaNhan).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }



    private void backToExactlyFragment() {
        intent = getIntent();
        int index = intent.getIntExtra("current_fragment", 0);
        viewPager.setCurrentItem(index);

    }

    private void goToSearchActivity() {
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Search.class);

                intent.putExtra("current_fragment",viewPager.getCurrentItem());
                startActivity(intent);

            }
        });

    }

    private void goToUserActivity() {
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, UserSetting.class);
                intent.putExtra("current_fragment",viewPager.getCurrentItem());
                startActivity(intent);
            }
        });

    }
    private void goToSettingActivity() {
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, Setting.class);
                intent.putExtra("current_fragment",viewPager.getCurrentItem());
                startActivity(intent);
            }
        });

    }

    @Override
    public void sendNextSongData(Baihatuathich baihat) {
        Intent intentService = new Intent(MainActivity.this, MusicService.class);

        //Gửi bài hát sang service
        Bundle bundle = new Bundle();
        bundle.putParcelable("baihat", baihat);
        intentService.putExtras(bundle);

        startService(intentService);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}