package com.example.genmusic.caNhanFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.genmusic.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class CaNhanFragment extends Fragment {

    private Context context;
    private RelativeLayout RLayoutBaiHatYeuThich, RLayoutPlaylistYeuThich, RLayoutAlbumYeuThich;
    private AdView mAdView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_nhan_fragment, container, false);

        RLayoutBaiHatYeuThich = view.findViewById(R.id.RLayoutBaiHatYeuThich);
        RLayoutAlbumYeuThich = view.findViewById(R.id.RLayoutAlbumYeuThich);
        RLayoutPlaylistYeuThich = view.findViewById(R.id.RLayoutPlaylistYeuThich);

        context = container.getContext();

        RLayoutBaiHatYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BaiHatYeuThich.class);
                startActivity(intent);
            }
        });

        RLayoutAlbumYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumYeuThichActivity.class);
                startActivity(intent);
            }
        });

        RLayoutPlaylistYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TheLoaiYeuThichActivity.class);
                startActivity(intent);
            }
        });

        //Quảng cáo
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                mAdView = view.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });
        return view;
    }
}
