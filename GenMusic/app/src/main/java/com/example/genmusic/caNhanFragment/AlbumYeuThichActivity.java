package com.example.genmusic.caNhanFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Dataservice;
import com.example.genmusic.theLoaiFragment.Album;
import com.example.genmusic.theLoaiFragment.AlbumAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumYeuThichActivity extends AppCompatActivity {

    private Toolbar toolbarAlbumYeuThich;
    private RecyclerView rcvAlbumYeuThich;
    private AlbumAdapter albumAdapter;
    private Dataservice dataservice = APIService.getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_yeu_thich);

        rcvAlbumYeuThich = findViewById(R.id.rcvAlbumYeuThich);
        toolbarAlbumYeuThich = findViewById(R.id.toolbarAlbumYeuThich);

        //Cài đặt toolbar
        setToolbar();
        //hiển thị danh sách bài hát
        setDataRecycleView();
    }

    private void setToolbar() {
        setSupportActionBar(toolbarAlbumYeuThich);
        getSupportActionBar().setTitle("Album yêu thích");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarAlbumYeuThich.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setDataRecycleView() {

        Call<List<Album>> callbackyeuthich = dataservice.GetDanhSachAlbumYEUThich();
        callbackyeuthich.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                List<Album> mangalbum = (List<Album>) response.body();
                Log.d("hiep", "ten album: " + mangalbum.get(0).getTenAlbum());
                albumAdapter = new AlbumAdapter(AlbumYeuThichActivity.this, mangalbum);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(AlbumYeuThichActivity.this, 2);
                rcvAlbumYeuThich.setLayoutManager(gridLayoutManager);
                rcvAlbumYeuThich.setFocusable(false);
                rcvAlbumYeuThich.setNestedScrollingEnabled(false);
                rcvAlbumYeuThich.setAdapter(albumAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.d("hiep", "loi: " + t.getMessage());
            }
        });
    }
}