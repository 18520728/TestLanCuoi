package com.example.genmusic.theLoaiFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Dataservice;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachTheLoaiActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView rcvDanhSachTheLoai;
    ChuDe chuDe;
    TheLoaiAdapter theLoaiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_the_loai);

        DataIntent();
        init();

        if(chuDe != null && !chuDe.getTenChuDe().equals(""))
        {
            setValueInView(chuDe.getTenChuDe(), chuDe.getHinhChuDe());
            setDataTheLoai(chuDe.getIdChuDe());
        }
    }

    private void setValueInView(String ten, String hinh) {
        collapsingToolbarLayout.setTitle(ten);
        try {
            URL url = new URL(hinh);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                collapsingToolbarLayout.setBackground(bitmapDrawable);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setDataTheLoai(String idchude) {
        Dataservice dataservice = APIService.getService();
        Call<List<TheLoai>> callback = dataservice.GetDanhSachTheLoai(idchude);
        callback.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                List<TheLoai> mangtheloai = (List<TheLoai>) response.body();
                theLoaiAdapter = new TheLoaiAdapter(DanhSachTheLoaiActivity.this, mangtheloai);

                GridLayoutManager gridLayoutManager = new GridLayoutManager(DanhSachTheLoaiActivity.this,2);
                rcvDanhSachTheLoai.setLayoutManager(gridLayoutManager);
                rcvDanhSachTheLoai.setNestedScrollingEnabled(false);
                rcvDanhSachTheLoai.setFocusable(false);
                rcvDanhSachTheLoai.setAdapter(theLoaiAdapter);
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {

            }
        });
    }

    private void DataIntent() {
        Intent intent = getIntent();
        if(intent != null)
        {
            if(intent.hasExtra("chude"))
            {
                chuDe = (ChuDe) intent.getSerializableExtra("chude");
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        //Ánh xạ
        coordinatorLayout = findViewById(R.id.coordinatorlayoutTheLoai);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbarTheLoai);
        toolbar = findViewById(R.id.toolbarTheLoai);
        rcvDanhSachTheLoai = findViewById(R.id.rcvDanhSachTheLoai);

        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //đổi màu nút back
        //toolbar.setNavigationIcon();
        collapsingToolbarLayout.setExpandedTitleColor(R.color.transparent);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


}