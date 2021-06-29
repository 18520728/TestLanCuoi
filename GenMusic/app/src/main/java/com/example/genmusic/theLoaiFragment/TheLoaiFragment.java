package com.example.genmusic.theLoaiFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Dataservice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TheLoaiFragment extends Fragment {

    //A. Khai báo biến
    private Context context;
    private View view;
    private TextView txtXemThem;
    private RecyclerView rcvAlbum, rcvChuDeTamTrang, rcvTheLoai, rcvChuDeQuocGia, rcvChuDe;
    private ChuDeAdapter chuDeAdapter;
    private AlbumHorizonAdapter albumHorizonAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.the_loai_fragment, container, false);

        //B. Ánh xạ view
        context = container.getContext();
        rcvAlbum = view.findViewById(R.id.rcvAlbum);
        rcvChuDeTamTrang = view.findViewById(R.id.rcvChuDeTamTrang);
        rcvTheLoai = view.findViewById(R.id.rcvTheLoai);
        rcvChuDeQuocGia = view.findViewById(R.id.rcvChuDeQuocGia);
        rcvChuDe = view.findViewById(R.id.rcvChuDe);
        txtXemThem = view.findViewById(R.id.txtXemThem);


        //C. Code xử lý

        setOnAlbumList();
        setOnChuDeList();

        //sự kiện click cho button xem thêm
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachAlbumActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setOnAlbumList() {
        //Album
        Dataservice dataservice = APIService.getService();
        Call<List<Album>> callback = dataservice.GetDanhSachAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {

                List<Album> MangAlbum = (List<Album>) response.body();

                albumHorizonAdapter = new AlbumHorizonAdapter(context,MangAlbum);
                //Hiển thị dạng lưới (dạng grid)
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                rcvAlbum.setLayoutManager(linearLayoutManager);

                //Không cho recycleview cuộn
                rcvAlbum.setNestedScrollingEnabled(false);
                rcvAlbum.setFocusable(false);

                //gọi adapter cho recycleview
                rcvAlbum.setAdapter(albumHorizonAdapter);

            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

    private void setOnChuDeList() {
        //ChuDe
        Dataservice dataservice = APIService.getService();
        Call<List<ChuDe>> callbackChuDe = dataservice.GetDanhSachChuDe();
        callbackChuDe.enqueue(new Callback<List<ChuDe>>() {
            @Override
            public void onResponse(Call<List<ChuDe>> call, Response<List<ChuDe>> response) {

                List<ChuDe> MangChuDe = (List<ChuDe>) response.body();
                //Thể loại (tương tự album)
                chuDeAdapter = new ChuDeAdapter();
                //Đổ dữ liệu vào từng recycleView:

                GridLayoutManager gridLayoutManager2 = new GridLayoutManager(context, 2);
                GridLayoutManager gridLayoutManager3 = new GridLayoutManager(context, 2);
                GridLayoutManager gridLayoutManager4 = new GridLayoutManager(context, 2);
                GridLayoutManager gridLayoutManager5 = new GridLayoutManager(context, 2);

                //TamTrang
                rcvChuDeTamTrang.setLayoutManager(gridLayoutManager2);
                rcvChuDeTamTrang.setNestedScrollingEnabled(false);
                rcvChuDeTamTrang.setFocusable(false);
                chuDeAdapter.setData(MangChuDe);
                rcvChuDeTamTrang.setAdapter(chuDeAdapter);

                //TheLoai
                rcvTheLoai.setLayoutManager(gridLayoutManager3);
                rcvTheLoai.setNestedScrollingEnabled(false);
                rcvTheLoai.setFocusable(false);
                chuDeAdapter.setData(MangChuDe);
                rcvTheLoai.setAdapter(chuDeAdapter);

                //ChuDeQuocGia
                rcvChuDeQuocGia.setLayoutManager(gridLayoutManager4);
                rcvChuDeQuocGia.setNestedScrollingEnabled(false);
                rcvChuDeQuocGia.setFocusable(false);
                chuDeAdapter.setData(MangChuDe);
                rcvChuDeQuocGia.setAdapter(chuDeAdapter);

                //ChuDe
                rcvChuDe.setLayoutManager(gridLayoutManager5);
                rcvChuDe.setNestedScrollingEnabled(false);
                rcvChuDe.setFocusable(false);
                chuDeAdapter.setData(MangChuDe);
                rcvChuDe.setAdapter(chuDeAdapter);

            }

            @Override
            public void onFailure(Call<List<ChuDe>> call, Throwable t) {

            }
        });
    }





}
