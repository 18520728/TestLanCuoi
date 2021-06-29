package com.example.genmusic.theLoaiFragment;

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
import android.view.View;
import android.widget.ImageView;

import com.example.genmusic.MinimizedPlayerFragment;
import com.example.genmusic.R;
import com.example.genmusic.Service.MusicService;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;
import com.example.genmusic.caNhanFragment.BaiHatYeuThich;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.genmusic.MainActivity.isServiceConnected;
import static com.example.genmusic.MainActivity.musicService;

public class DanhSachAlbumActivity extends AppCompatActivity implements MinimizedPlayerFragment.ISendDataListener {

    RecyclerView rcvAllAlbum;
    Toolbar toolbarAlbum;
    AlbumAdapter albumAdapter;

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
        setContentView(R.layout.activity_danh_sach_album);
        //Ánh xạ
        rcvAllAlbum = findViewById(R.id.rcvAllAlbum);
        toolbarAlbum = findViewById(R.id.toolbarAlbum);

        //Code

        //Cài đặt toolbar
        setOnToolbar();

        //recycleview danh sách album
        setOnAlbumList();
    }
    private void setOnAlbumList() {
        //Album
        Dataservice dataservice = APIService.getService();
        Call<List<Album>> callback = dataservice.GetDanhSachTatCaAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {

                List<Album> MangAlbum = (List<Album>) response.body();

                albumAdapter = new AlbumAdapter(DanhSachAlbumActivity.this,MangAlbum);
                //Hiển thị dạng lưới (dạng grid)
                GridLayoutManager gridLayoutManager = new GridLayoutManager(DanhSachAlbumActivity.this,2);
                rcvAllAlbum.setLayoutManager(gridLayoutManager);

                //Không cho recycleview cuộn
                rcvAllAlbum.setNestedScrollingEnabled(false);
                rcvAllAlbum.setFocusable(false);


                //truyền dữ liệu vào adapter và gọi adapter cho recycleview
                rcvAllAlbum.setAdapter(albumAdapter);

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

    }

    private void setOnToolbar() {
        setSupportActionBar(toolbarAlbum);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất cả Album");
        toolbarAlbum.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void sendNextSongData(Baihatuathich baihat) {
        Intent intentService = new Intent(DanhSachAlbumActivity.this, MusicService.class);
        //Gửi bài hát sang service
        Bundle bundle = new Bundle();
        bundle.putParcelable("baihat", baihat);
        intentService.putExtras(bundle);

        startService(intentService);
        bindService(intentService, serviceConnection, Context.BIND_AUTO_CREATE);
    }
}