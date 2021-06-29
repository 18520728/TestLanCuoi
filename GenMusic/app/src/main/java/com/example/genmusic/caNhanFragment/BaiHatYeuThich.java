package com.example.genmusic.caNhanFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;

import com.example.genmusic.MainActivity;
import com.example.genmusic.MinimizedPlayerFragment;
import com.example.genmusic.R;
import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.BaihatuathichAdapter;
import com.example.genmusic.bxhFragment.Dataservice;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.genmusic.MainActivity.musicService;
import static com.example.genmusic.MainActivity.isServiceConnected;

public class BaiHatYeuThich extends AppCompatActivity implements MinimizedPlayerFragment.ISendDataListener {

    private Toolbar toolbarBaiHatYeuThich;
    private RecyclerView rcvBaiHatYeuThich;
    private BaiHatYeuThichAdapter baiHatYeuThichAdapter;
    private Dataservice dataservice = APIService.getService();
    //lấy tên đăng nhập từ firebase
    private FirebaseAuth auth= FirebaseAuth.getInstance();
    private String tendangnhap;

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
        setContentView(R.layout.activity_bai_hat_yeu_thich);
        rcvBaiHatYeuThich = findViewById(R.id.rcvBaiHatYeuThich);
        toolbarBaiHatYeuThich = findViewById(R.id.toolbarBaiHatYeuThich);

        tendangnhap = auth.getCurrentUser().getEmail();
        //Cài đặt toolbar
        setToolbar();
        //hiển thị danh sách bài hát
        setDataRecycleView();
    }

    private void setToolbar() {
        setSupportActionBar(toolbarBaiHatYeuThich);
        getSupportActionBar().setTitle("Bài hát yêu thích");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarBaiHatYeuThich.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setDataRecycleView() {
        Call<List<Baihatuathich>> callbackyeuthich = dataservice.GetDanhSachBaiHatYeuThich(tendangnhap);
        callbackyeuthich.enqueue(new Callback<List<Baihatuathich>>() {
            @Override
            public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {
                ArrayList<Baihatuathich> mangbaihat = (ArrayList<Baihatuathich>) response.body();

                baiHatYeuThichAdapter = new BaiHatYeuThichAdapter(BaiHatYeuThich.this, mangbaihat);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaiHatYeuThich.this, RecyclerView.VERTICAL, false);
                rcvBaiHatYeuThich.setLayoutManager(linearLayoutManager);
                rcvBaiHatYeuThich.setFocusable(false);
                rcvBaiHatYeuThich.setNestedScrollingEnabled(false);
                rcvBaiHatYeuThich.setAdapter(baiHatYeuThichAdapter);
            }
            @Override
            public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

            }
        });
    }

    @Override
    public void sendNextSongData(Baihatuathich baihat) {
        Intent intentService = new Intent(BaiHatYeuThich.this, MusicService.class);
        //Gửi bài hát sang service
        Bundle bundle = new Bundle();
        bundle.putParcelable("baihat", baihat);
        intentService.putExtras(bundle);

        startService(intentService);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}