package com.example.genmusic.caNhanFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.genmusic.MinimizedPlayerFragment;
import com.example.genmusic.R;
import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.BaihatuathichAdapter;
import com.example.genmusic.bxhFragment.Dataservice;
import com.example.genmusic.theLoaiFragment.TheLoai;
import com.example.genmusic.theLoaiFragment.TheLoaiAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.genmusic.MainActivity.isServiceConnected;
import static com.example.genmusic.MainActivity.musicService;

public class TheLoaiYeuThichActivity extends AppCompatActivity implements MinimizedPlayerFragment.ISendDataListener {

    private Toolbar toolbarTheLoaiYeuThich;
    private RecyclerView rcvTheLoaiYeuThich;
    private TheLoaiAdapter theLoaiAdapter;
    private Dataservice dataservice = APIService.getService();

    //service
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_loai_yeu_thich);

        rcvTheLoaiYeuThich = findViewById(R.id.rcvTheLoaiYeuThich);
        toolbarTheLoaiYeuThich = findViewById(R.id.toolbarTheLoaiYeuThich);

        //Cài đặt toolbar
        setToolbar();
        //hiển thị danh sách bài hát
        setDataRecycleView();

    }

    private void setToolbar() {
        setSupportActionBar(toolbarTheLoaiYeuThich);
        getSupportActionBar().setTitle("Playlist yêu thích");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTheLoaiYeuThich.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setDataRecycleView() {

        Call<List<TheLoai>> callbackyeuthich = dataservice.GetDanhSachTheLoaiYeuThich();
        callbackyeuthich.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                List<TheLoai> mangtheloai = (List<TheLoai>) response.body();
                Log.d("hiep", "ten: " + mangtheloai.get(0).getTentheloai());
                theLoaiAdapter = new TheLoaiAdapter(TheLoaiYeuThichActivity.this, mangtheloai);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(TheLoaiYeuThichActivity.this, 2);
                rcvTheLoaiYeuThich.setLayoutManager(gridLayoutManager);
                rcvTheLoaiYeuThich.setFocusable(false);
                rcvTheLoaiYeuThich.setNestedScrollingEnabled(false);
                rcvTheLoaiYeuThich.setAdapter(theLoaiAdapter);
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {

            }
        });

    }

    @Override
    public void sendNextSongData(Baihatuathich baihat) {
        Intent intentService = new Intent(TheLoaiYeuThichActivity.this, MusicService.class);
        //Gửi bài hát sang service
        Bundle bundle = new Bundle();
        bundle.putParcelable("baihat", baihat);
        intentService.putExtras(bundle);

        startService(intentService);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}