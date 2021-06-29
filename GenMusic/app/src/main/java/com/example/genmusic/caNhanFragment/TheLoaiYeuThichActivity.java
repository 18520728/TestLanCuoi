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

public class TheLoaiYeuThichActivity extends AppCompatActivity {

    private Toolbar toolbarTheLoaiYeuThich;
    private RecyclerView rcvTheLoaiYeuThich;
    private TheLoaiAdapter theLoaiAdapter;
    private Dataservice dataservice = APIService.getService();

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
}