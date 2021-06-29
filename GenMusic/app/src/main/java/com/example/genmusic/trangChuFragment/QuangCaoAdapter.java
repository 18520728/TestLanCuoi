package com.example.genmusic.trangChuFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.genmusic.PlayNhacActivity;
import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuangCaoAdapter extends PagerAdapter {

    private Context context;
    private List<QuangCao> listQuangCao;

    public QuangCaoAdapter(Context context, List<QuangCao> listQuangCao) {
        this.context = context;
        this.listQuangCao = listQuangCao;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull  ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_slide_song, container, false);
        ImageView imgQuangCao = view.findViewById(R.id.imgHinhQuangCao);

        QuangCao quangCao = listQuangCao.get(position);
        if(quangCao != null)
        {
            Glide.with(context).load(quangCao.getHinhQuangCao()).into(imgQuangCao);
        }

        //sự kiện click mở bài hát
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dataservice dataservice = APIService.getService();
                Call<List<Baihatuathich>> callback = dataservice.GetBaiHatQuangCao(quangCao.getIdBaiHat());
                callback.enqueue(new Callback<List<Baihatuathich>>() {
                    @Override
                    public void onResponse(Call<List<Baihatuathich>> call, Response<List<Baihatuathich>> response) {
                        List<Baihatuathich> baihat = response.body();
                        Intent intent=new Intent(context, PlayNhacActivity.class);
                        intent.putExtra("cakhuc", (Parcelable) baihat.get(0));
                        context.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<List<Baihatuathich>> call, Throwable t) {

                    }
                });
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(listQuangCao != null)
            return listQuangCao.size();
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position,  Object object) {
        container.removeView((View) object);
    }
}
