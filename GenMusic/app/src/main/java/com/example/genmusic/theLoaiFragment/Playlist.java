package com.example.genmusic.theLoaiFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.genmusic.BaiHat;
import com.example.genmusic.MainActivity;
import com.example.genmusic.R;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends AppCompatActivity {

    private ImageButton imgBack;
    private ImageView imgYeuThich;

    private RecyclerView rcvBaiHat;
    private PlaylistAdapter playlistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        rcvBaiHat = findViewById(R.id.rcvBaiHat);
        imgBack = findViewById(R.id.imgBack);
        imgYeuThich = findViewById(R.id.imgYeuThich);
        hienthiBaiHat();

        yeuThichPlaylist();

        xulyBack();

    }

    private void xulyBack() {

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Playlist.this, MainActivity.class);
                intent.putExtra("current_fragment",1);
                startActivity(intent);
            }
        });
    }

    boolean isFavorited = false;
    private void yeuThichPlaylist() {
        imgYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFavorited)
                {
                    imgYeuThich.setImageResource(R.drawable.favorite);
                    isFavorited = true;
                }

                else
                {
                    imgYeuThich.setImageResource(R.drawable.favorite_border);
                    isFavorited = false;
                }

            }
        });
    }

    private void hienthiBaiHat() {
        playlistAdapter = new PlaylistAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvBaiHat.setLayoutManager(linearLayoutManager);

        rcvBaiHat.setFocusable(false);
        rcvBaiHat.setNestedScrollingEnabled(false);

        playlistAdapter.setData(getListData());
        rcvBaiHat.setAdapter(playlistAdapter);

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

        return list;
    }
}