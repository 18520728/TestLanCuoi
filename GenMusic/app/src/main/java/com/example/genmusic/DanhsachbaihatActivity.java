package com.example.genmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;
import com.example.genmusic.bxhFragment.bxh;
import com.example.genmusic.theLoaiFragment.Album;
import com.example.genmusic.theLoaiFragment.TheLoai;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachbaihatActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerViewdanhsachbaihat;
    FloatingActionButton floatingActionButton;
    ImageView imgdanhsachcakhuc;
    DanhsachbaihatAdapter danhsachbaihatAdapter;
    bxh chart;
    Album album;
    TheLoai theLoai;
    private ImageView imgThemPlaylistYeuThich;
    private Dataservice dataservice= APIService.getService();
    ArrayList<Baihatuathich> mangbaihat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachbaihat);
        StrictMode.ThreadPolicy policy=new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        DataIntent();
        anhxa();
        init();

        if(chart!=null && !chart.getTen().equals("")){
            setValueInView(chart.getTen(),chart.getHinh());
            GetDataChart(chart.getIdChart());
        }
        if(album != null && !album.getTenAlbum().equals(""))
        {
            setValueInView(album.getTenAlbum(), album.getHinhAlbum());
            GetDataAlbum(album.getIdAlbum());
            addAlbumYeuThich(album);
        }
        if(theLoai != null && !theLoai.getTentheloai().equals(""))
        {
            setValueInView(theLoai.getTentheloai(), theLoai.getHinhtheloai());
            GetDataTheLoai(theLoai.getIdtheloai());
            addTheLoaiYeuThich(theLoai);
        }
        
    }

    private void addAlbumYeuThich(Album album) {
        //Kiểm tra album đã có trong albumyeuthich hay chưa
        Call<String> callbackKTAlbum = dataservice.KiemTraAlbumYeuThich(album.getIdAlbum());
        callbackKTAlbum.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua = response.body();
                if(ketqua.equals("1"))
                {
                    imgThemPlaylistYeuThich.setImageResource(R.drawable.ic_loved);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //click nút trái tim
        imgThemPlaylistYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //truyền tendangnhap, idalbum
                Call<String> callbackAlbum = dataservice.InsertOrDeleteAlbumYeuThich(album.getIdAlbum());
                callbackAlbum.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("successful_insert"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Đã thêm " + album.getTenAlbum() + " vào Album yêu thích", Toast.LENGTH_SHORT).show();
                           imgThemPlaylistYeuThich.setImageResource(R.drawable.ic_loved);
                        }
                        else if(kq.equals("failed_insert"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Có lỗi khi thêm Album", Toast.LENGTH_SHORT).show();
                        }
                        else if(kq.equals("successful_delete"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Đã xóa " + album.getTenAlbum() + " khỏi Album yêu thích", Toast.LENGTH_SHORT).show();
                            imgThemPlaylistYeuThich.setImageResource(R.drawable.ic_love_white);
                        }
                        else if(kq.equals("failed_delete"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Có lỗi khi xóa Album", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }
    private void addTheLoaiYeuThich(TheLoai theLoai) {
        //Kiểm tra theloai đã có trong theloaiyeuthich hay chưa
        Call<String> callbackKTTheLoai = dataservice.KiemTraTheLoaiYeuThich(theLoai.getIdtheloai());
        callbackKTTheLoai.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua = response.body();
                if(ketqua.equals("1"))
                {
                    imgThemPlaylistYeuThich.setImageResource(R.drawable.ic_loved);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //click nút trái tim
        imgThemPlaylistYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //truyền tendangnhap, idalbum
                Call<String> callbackTheLoai = dataservice.InsertOrDeleteTheLoaiYeuThich(theLoai.getIdtheloai());
                callbackTheLoai.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("successful_insert"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Đã thêm " + theLoai.getTentheloai() + " vào Playlist yêu thích", Toast.LENGTH_SHORT).show();
                            imgThemPlaylistYeuThich.setImageResource(R.drawable.ic_loved);
                        }
                        else if(kq.equals("failed_insert"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Có lỗi khi thêm Playlist", Toast.LENGTH_SHORT).show();
                        }
                        else if(kq.equals("successful_delete"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Đã xóa " + theLoai.getTentheloai() + " khỏi Playlist yêu thích", Toast.LENGTH_SHORT).show();
                            imgThemPlaylistYeuThich.setImageResource(R.drawable.ic_love_white);
                        }
                        else if(kq.equals("failed_delete"))
                        {
                            Toast.makeText(DanhsachbaihatActivity.this, "Có lỗi khi xóa Playlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void GetDataTheLoai(String idtheloai) {
        Call<List<Baihatuathich>> callback = dataservice.GetDanhSachBaiHatTrongTheLoai(idtheloai);
        callback.enqueue(new Callback<List<Baihatuathich>>() {
            @Override
            public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {

                mangbaihat=(ArrayList<Baihatuathich>)response.body();
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);

                recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this, RecyclerView.VERTICAL, false));
                recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

            }
        });
    }

    private void GetDataAlbum(String idlabum) {

        Call<List<Baihatuathich>> callback = dataservice.GetDanhSachBaiHatTrongAlbum(idlabum);
        callback.enqueue(new Callback<List<Baihatuathich>>() {
            @Override
            public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {
                mangbaihat=(ArrayList<Baihatuathich>)response.body();
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);

                recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this, RecyclerView.VERTICAL, false));
                recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

            }
        });
    }

    private void GetDataChart(String idxephang) {

        Call<List<Baihatuathich>> callback=dataservice.GetDanhsachbaihatuathichtheochart(idxephang);
        callback.enqueue(new Callback<List<Baihatuathich>>() {
            @Override
            public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {

                mangbaihat=(ArrayList<Baihatuathich>)response.body();
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);

                recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this, RecyclerView.VERTICAL, false));
                recyclerViewdanhsachbaihat.setAdapter(danhsachbaihatAdapter);
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

            }
        });
    }

    private void setValueInView(String ten, String hinh) {
        collapsingToolbarLayout.setTitle(ten);

        try {
            URL url=new URL(hinh);
            Bitmap bitmap= BitmapFactory.decodeStream((url.openConnection().getInputStream()));
            BitmapDrawable bitmapDrawable=new BitmapDrawable(getResources(),bitmap);
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                collapsingToolbarLayout.setBackground(bitmapDrawable);
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Picasso.with(this).load(hinh).into(imgdanhsachcakhuc);
    }

    private void DataIntent() {
        Intent intent= getIntent();
        if(intent!=null)
        {
            if(intent.hasExtra("itemchart")){
                chart= (bxh) intent.getSerializableExtra("itemchart");
            }
            if(intent.hasExtra("album"))
                album = (Album) intent.getSerializableExtra("album");
            if(intent.hasExtra("theloai"))
                theLoai = (TheLoai) intent.getSerializableExtra("theloai");
        }
    }


    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        floatingActionButton.setEnabled(false);
    }

    private void anhxa() {
        coordinatorLayout=findViewById(R.id.coordinatorlayout);
        collapsingToolbarLayout=findViewById(R.id.collapsingtoolbar);
        toolbar=findViewById(R.id.toolbardanhsach);
        recyclerViewdanhsachbaihat=findViewById(R.id.recyclerviewdanhsachbaihat);
        imgdanhsachcakhuc=findViewById(R.id.imageviewdanhsachcakhuc);
        floatingActionButton=findViewById(R.id.floatingactionbutton);
        imgThemPlaylistYeuThich = findViewById(R.id.imgThemPlaylistYeuThich);
    }
    private void eventClick(){
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DanhsachbaihatActivity.this,PlayNhacActivity.class);
                intent.putExtra("cacbaihat",mangbaihat);
                startActivity(intent);
            }
        });
    }
}