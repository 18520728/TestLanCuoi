package com.example.genmusic.bxhFragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.PlayNhacActivity;
import com.example.genmusic.R;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaihatuathichAdapter extends  RecyclerView.Adapter<BaihatuathichAdapter.ViewHolder>{
    @NonNull
    Context context;
    ArrayList<Baihatuathich> baihatuathichArrayList;
    private Dataservice dataservice= APIService.getService();

    public BaihatuathichAdapter(@NonNull Context context, ArrayList<Baihatuathich> baihatuathichArrayList) {
        this.context = context;
        this.baihatuathichArrayList = baihatuathichArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.dongbaihatuathich,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        Baihatuathich baihatuathich=baihatuathichArrayList.get(position);
        holder.txtcasi.setText(baihatuathich.getCasi());
        holder.txtten.setText(baihatuathich.getTenbaihat());
        Picasso.with(context).load(baihatuathich.getHinhbaihat()).into(holder.imghinh);

        //Kiểm tra bài hát đã có trong baihatyeuthich hay chưa
        Call<String> callbackKiemTra = dataservice.KiemTraBaiHatYeuThich(baihatuathich.getIdbaihat());
        callbackKiemTra.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua = response.body();
                if(ketqua.equals("1"))
                {
                    holder.imgluotthich.setImageResource(R.drawable.ic_loved);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        //sự kiện click vào mỗi dòng bài hát làm 2 việc:
        //1. intent qua player để chơi nhạc
        //2. tăng lượt nghe (LuotThich trên database) lên +1

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent
                Intent intent=new Intent(context, PlayNhacActivity.class);
                intent.putExtra("cakhuc",baihatuathichArrayList.get(position));
                context.startActivity(intent);

                //++luotnghe
                Call<String> callbackLuotNghe=dataservice.UpdateLuotThich(baihatuathich.getIdbaihat());
                callbackLuotNghe.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("hiep","ket qua: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

        //Sự kiện click nút trái tim thêm vào hoặc xóa khỏi bài hát yêu thích
        holder.imgluotthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //truyền tendangnhap, idbaihat
                Call<String> callback = dataservice.InsertOrDeleteBaiHatYeuThich(baihatuathich.getIdbaihat());
                baihatuathichArrayList.remove(position);
                callback.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq = response.body();
                        if(kq.equals("successful_insert"))
                        {
                            Toast.makeText(context, "Đã thêm " + baihatuathich.getTenbaihat() + " vào Bài hát yêu thích", Toast.LENGTH_SHORT).show();
                            holder.imgluotthich.setImageResource(R.drawable.ic_loved);
                        }
                        else if(kq.equals("failed_insert"))
                        {
                            Toast.makeText(context, "Có lỗi khi thêm bài hát", Toast.LENGTH_SHORT).show();
                        }
                        else if(kq.equals("successful_delete"))
                        {
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Đã xóa " + baihatuathich.getTenbaihat() + " khỏi Bài hát yêu thích", Toast.LENGTH_SHORT).show();
                            holder.imgluotthich.setImageResource(R.drawable.ic_love);
                        }
                        else if(kq.equals("failed_delete"))
                        {
                            Toast.makeText(context, "Có lỗi khi xóa bài hát", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return baihatuathichArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtten,txtcasi;
        ImageView imghinh,imgluotthich;
        public ViewHolder(@NonNull  View itemView) {

            super(itemView);
            txtten=itemView.findViewById(R.id.textviewtenbaihatuathich);
            txtcasi=itemView.findViewById(R.id.textviewcasibaihatuathich);
            imghinh=itemView.findViewById(R.id.imageViewbaihatuathich);
            imgluotthich=itemView.findViewById(R.id.imageViewluotthich);
        }
    }
}
