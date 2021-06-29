package com.example.genmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_Play_DanhsachbaiHat extends Fragment {
    View view;
    RecyclerView recyclerViewplaynhac;
    PlaynhacAdapter playnhacAdapter;
    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater,@Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.fragmentplaydanhsachbaihat,container,false);
        recyclerViewplaynhac=view.findViewById(R.id.recyclerviewPlaybaihat);
       if(PlayNhacActivity.mangbaihat.size()>0){

           playnhacAdapter=new PlaynhacAdapter(getActivity(),PlayNhacActivity.mangbaihat);
           recyclerViewplaynhac.setLayoutManager(new LinearLayoutManager(getActivity()));
           recyclerViewplaynhac.setAdapter(playnhacAdapter);
       }
       return view;
    }
}
