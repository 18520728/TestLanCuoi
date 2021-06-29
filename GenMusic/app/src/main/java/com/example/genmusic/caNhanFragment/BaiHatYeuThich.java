package com.example.genmusic.caNhanFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.genmusic.MainActivity;
import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.BaihatuathichAdapter;
import com.example.genmusic.bxhFragment.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatYeuThich extends AppCompatActivity {

    private Toolbar toolbarBaiHatYeuThich;
    private RecyclerView rcvBaiHatYeuThich;
    private BaihatuathichAdapter baihatuathichAdapter;
    private Dataservice dataservice = APIService.getService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_hat_yeu_thich);
        rcvBaiHatYeuThich = findViewById(R.id.rcvBaiHatYeuThich);
        toolbarBaiHatYeuThich = findViewById(R.id.toolbarBaiHatYeuThich);

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

        Call<List<Baihatuathich>> callbackyeuthich = dataservice.GetDanhSachBaiHatYeuThich();
        callbackyeuthich.enqueue(new Callback<List<Baihatuathich>>() {
            @Override
            public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {
                ArrayList<Baihatuathich> mangbaihat = (ArrayList<Baihatuathich>) response.body();

                baihatuathichAdapter = new BaihatuathichAdapter(BaiHatYeuThich.this, mangbaihat);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BaiHatYeuThich.this, RecyclerView.VERTICAL, false);
                rcvBaiHatYeuThich.setLayoutManager(linearLayoutManager);
                rcvBaiHatYeuThich.setFocusable(false);
                rcvBaiHatYeuThich.setNestedScrollingEnabled(false);
                rcvBaiHatYeuThich.setAdapter(baihatuathichAdapter);
            }
            @Override
            public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

            }
        });
    }
}