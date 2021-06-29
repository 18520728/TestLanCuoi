package com.example.genmusic.caNhanFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.genmusic.BaiHat;
import com.example.genmusic.MainActivity;
import com.example.genmusic.R;
import com.example.genmusic.theLoaiFragment.PlaylistAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaiHatDaTai extends AppCompatActivity {

    private ImageButton imgTroVeCaNhan;

    private RecyclerView rcvBaiHatDaTai;
    private PlaylistAdapter playlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_hat_da_tai);

        imgTroVeCaNhan = findViewById(R.id.imgTroVeCaNhan);
        rcvBaiHatDaTai = findViewById(R.id.rcvBaiHatDaTai);


        //Xử lý trở về
        troVeCaNhan();

        //hiển thị danh sách bài hát
        hienThiBaiHat();
    }

    private void hienThiBaiHat() {
        playlistAdapter = new PlaylistAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvBaiHatDaTai.setLayoutManager(linearLayoutManager);

        rcvBaiHatDaTai.setFocusable(false);
        rcvBaiHatDaTai.setNestedScrollingEnabled(false);

        playlistAdapter.setData(getListData());
        rcvBaiHatDaTai.setAdapter(playlistAdapter);

    }

    private List<BaiHat> getListData() {

        ArrayList<BaiHat> list = new ArrayList<>();
        list.add(new BaiHat(R.drawable.baihat_buocquamuacodon, "Bước qua mùa cô đơn","Vũ"));
        list.add(new BaiHat(R.drawable.baihat_buocquamuacodon, "Bước qua mùa cô đơn","Vũ"));
        list.add(new BaiHat(R.drawable.baihat_buocquamuacodon, "Bước qua mùa cô đơn","Vũ"));
        list.add(new BaiHat(R.drawable.baihat_buocquamuacodon, "Bước qua mùa cô đơn","Vũ"));
        list.add(new BaiHat(R.drawable.baihat_mongphonhoa, "Mộng phồn hoa", "Hoàng Linh"));
        list.add(new BaiHat(R.drawable.baihat_mongphonhoa, "Mộng phồn hoa", "Hoàng Linh"));
        list.add(new BaiHat(R.drawable.baihat_mongphonhoa, "Mộng phồn hoa", "Hoàng Linh"));
        list.add(new BaiHat(R.drawable.baihat_mongphonhoa, "Mộng phồn hoa", "Hoàng Linh"));
        list.add(new BaiHat(R.drawable.baihat_buocquamuacodon, "Bước qua mùa cô đơn","Vũ"));
        list.add(new BaiHat(R.drawable.baihat_buocquamuacodon, "Bước qua mùa cô đơn","Vũ"));
        list.add(new BaiHat(R.drawable.baihat_mongphonhoa, "Mộng phồn hoa", "Hoàng Linh"));
        list.add(new BaiHat(R.drawable.baihat_mongphonhoa, "Mộng phồn hoa", "Hoàng Linh"));


        return list;
    }

    private void troVeCaNhan() {

        imgTroVeCaNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaiHatDaTai.this, MainActivity.class);
                intent.putExtra("current_fragment",3);
                startActivity(intent);
                finish();
            }
        });
    }
}