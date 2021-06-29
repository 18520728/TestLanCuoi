package com.example.genmusic.trangChuFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.genmusic.NewsActivity;
import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.BaihatuathichAdapter;
import com.example.genmusic.bxhFragment.Dataservice;
import com.example.genmusic.theLoaiFragment.Album;
import com.example.genmusic.theLoaiFragment.AlbumAdapter;
import com.example.genmusic.theLoaiFragment.AlbumHorizonAdapter;
import com.example.genmusic.theLoaiFragment.DanhSachTheLoaiActivity;
import com.example.genmusic.theLoaiFragment.TheLoai;
import com.example.genmusic.theLoaiFragment.TheLoaiAdapter;
import com.example.genmusic.theLoaiFragment.TheLoaiHorizonAdapter;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangChuFragment extends Fragment {

    private ViewPager viewPagerOfHome;
    private CircleIndicator circleIndicator;
    private QuangCaoAdapter quangCaoAdapter;
    private Button btnMoNhac;
    private RecyclerView rcvGoiYTheLoai, rcvBaiHatMoi, rcvAlbumMoi;
    private TheLoaiHorizonAdapter theLoaiHorizonAdapter;
    private BaihatuathichAdapter baihatuathichAdapter;
    private AlbumHorizonAdapter albumHorizonAdapter;
    private Dataservice dataservice = APIService.getService();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trang_chu_fragment, container, false);

        viewPagerOfHome = view.findViewById(R.id.vpgSongImage);
        circleIndicator = view.findViewById(R.id.ciSongImage);
        rcvGoiYTheLoai = view.findViewById(R.id.rcvGoiYTheLoai);
        rcvBaiHatMoi = view.findViewById(R.id.rcvBaiHatMoi);
        rcvAlbumMoi = view.findViewById(R.id.rcvAlbumMoi);

        btnMoNhac = view.findViewById(R.id.btnMoNhac);

        //Xử lý slide show bài hát

        setDataSlideShow();
        setDataNews();
        setDataGoiYTheLoai();
        setDataBaiHatMoi();
        setDataAlbumMoi();


        return view;
    }

    private void setDataNews() {

        btnMoNhac.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setDataSlideShow() {


        Call<List<QuangCao>> callback = dataservice.GetDanhSachQuangCao();
        callback.enqueue(new Callback<List<QuangCao>>() {
            @Override
            public void onResponse(Call<List<QuangCao>> call, Response<List<QuangCao>> response) {
                List<QuangCao> mangquangcao = (List<QuangCao>) response.body();
                quangCaoAdapter = new QuangCaoAdapter(getContext(), mangquangcao);
                viewPagerOfHome.setFocusable(true);
                viewPagerOfHome.setAdapter(quangCaoAdapter);
                circleIndicator.setViewPager(viewPagerOfHome);
                quangCaoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
            }

            @Override
            public void onFailure(Call<List<QuangCao>> call, Throwable t) {

            }
        });
    }
    private void setDataGoiYTheLoai() {

        Call<List<TheLoai>> callback = dataservice.GetDanhSachTheLoaiRandom();
        callback.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                List<TheLoai> mangtheloai = (List<TheLoai>) response.body();
                theLoaiHorizonAdapter = new TheLoaiHorizonAdapter(getContext(), mangtheloai);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                rcvGoiYTheLoai.setLayoutManager(linearLayoutManager);
                rcvGoiYTheLoai.setNestedScrollingEnabled(false);
                rcvGoiYTheLoai.setFocusable(false);
                rcvGoiYTheLoai.setAdapter(theLoaiHorizonAdapter);
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {

            }
        });

    }
    private void setDataBaiHatMoi() {

        Call<List<Baihatuathich>> callback = dataservice.GetDanhSachBaiHatMoi();
        callback.enqueue(new Callback<List<Baihatuathich>>() {
            @Override
            public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {
                ArrayList<Baihatuathich> mangbaihatmoi = (ArrayList<Baihatuathich>) response.body();
                baihatuathichAdapter = new BaihatuathichAdapter(getContext(), mangbaihatmoi);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false);
                rcvBaiHatMoi.setLayoutManager(linearLayoutManager);
                rcvBaiHatMoi.setNestedScrollingEnabled(false);
                rcvBaiHatMoi.setFocusable(false);
                rcvBaiHatMoi.setAdapter(baihatuathichAdapter);
            }

            @Override
            public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

            }
        });
    }
    private void setDataAlbumMoi() {
        Call<List<Album>> callback = dataservice.GetDanhSachAlbumMoi();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                List<Album> mangalbummoi = (List<Album>) response.body();
                albumHorizonAdapter = new AlbumHorizonAdapter(getContext(), mangalbummoi);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                rcvAlbumMoi.setLayoutManager(linearLayoutManager);
                rcvAlbumMoi.setNestedScrollingEnabled(false);
                rcvAlbumMoi.setFocusable(false);
                rcvAlbumMoi.setAdapter(albumHorizonAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

}
