//
package com.example.genmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.bxhFragment.APIService;
import com.example.genmusic.bxhFragment.Baihatuathich;
import com.example.genmusic.bxhFragment.Dataservice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachbaihatAdapter extends  RecyclerView.Adapter<DanhsachbaihatAdapter.ViewHolder>{
    Context context;
    ArrayList<Baihatuathich> mangbaihat;
    private Dataservice dataservice = APIService.getService();

    public DanhsachbaihatAdapter(Context context, ArrayList<Baihatuathich> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dong_danhsachbaihat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  DanhsachbaihatAdapter.ViewHolder holder, int position)
    {
        Baihatuathich baihatuathich=mangbaihat.get(position);
        holder.txtcasi.setText(baihatuathich.getCasi());
        holder.txttenbaihat.setText(baihatuathich.getTenbaihat());
        holder.txtindex.setText(position +1+ "");

        //Kiểm tra bài hát đã có trong baihatyeuthich hay chưa
        Call<String> callbackKiemTra = dataservice.KiemTraBaiHatYeuThich(baihatuathich.getIdbaihat());
        callbackKiemTra.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if(kq.equals("1"))
                {
                    holder.imgluotthich.setImageResource(R.drawable.ic_loved);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //intent sang player chơi nhạc
                Intent intent=new Intent(context,PlayNhacActivity.class);
                intent.putExtra("cakhuc",mangbaihat.get(position));
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
                            Toast.makeText(context, "Đã xóa " + baihatuathich.getTenbaihat() + " vào Bài hát yêu thích", Toast.LENGTH_SHORT).show();
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
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtindex,txttenbaihat,txtcasi;
        ImageView imgluotthich;
        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            txtcasi=itemView.findViewById(R.id.textviewtencasi);
            txtindex=itemView.findViewById(R.id.textviewdanhsachindex);
            txttenbaihat=itemView.findViewById(R.id.textviewtenbaihat);
            imgluotthich=itemView.findViewById(R.id.imageviewluotthichdanhsachbaihat);
        }
    }
}
