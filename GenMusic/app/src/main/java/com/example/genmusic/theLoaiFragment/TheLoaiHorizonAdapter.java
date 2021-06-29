package com.example.genmusic.theLoaiFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.DanhsachbaihatActivity;
import com.example.genmusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TheLoaiHorizonAdapter extends RecyclerView.Adapter<TheLoaiHorizonAdapter.TheLoaiViewHolder>{

    private Context context;
    private List<TheLoai> ListTheLoai;

    public TheLoaiHorizonAdapter(Context context, List<TheLoai> listTheLoai) {
        this.context = context;
        ListTheLoai = listTheLoai;
    }

    @Override
    public TheLoaiViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_the_loai_horizon, parent, false);

        return new TheLoaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TheLoaiHorizonAdapter.TheLoaiViewHolder holder, int position) {

        TheLoai theLoai = ListTheLoai.get(position);
        if(theLoai == null)
        {
            return;
        }
        else
        {
            holder.txtTenTheLoai.setText(theLoai.getTentheloai());
            Picasso.with(context).load(theLoai.getHinhtheloai()).into(holder.imgHinhTheLoai);

            //click
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("theloai", ListTheLoai.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(ListTheLoai != null)
        {
            return ListTheLoai.size();
        }
        return 0;
    }

    public class TheLoaiViewHolder extends RecyclerView.ViewHolder {

        TextView txtTenTheLoai;
        ImageView imgHinhTheLoai;

        public TheLoaiViewHolder( View itemView) {
            super(itemView);

            txtTenTheLoai = itemView.findViewById(R.id.txtTenTheLoai);
            imgHinhTheLoai = itemView.findViewById(R.id.imgHinhTheLoai);

        }
    }
}
