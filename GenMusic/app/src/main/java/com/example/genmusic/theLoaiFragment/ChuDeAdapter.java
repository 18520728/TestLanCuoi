package com.example.genmusic.theLoaiFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChuDeAdapter extends RecyclerView.Adapter<ChuDeAdapter.ChuDeViewHolder>{

    //Lưu context
    private Context context;
    //Tạo list chứa các thể loại
    private List<ChuDe> ListChuDe;

    //Hàm đổ dữ liệu vào List
    public void setData(List<ChuDe> list)
    {
        this.ListChuDe = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChuDeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chu_de, parent, false);

        //Lưu context
        context = parent.getContext();
        return new ChuDeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ChuDeAdapter.ChuDeViewHolder holder, int position) {

        ChuDe chuDe = ListChuDe.get(position);
        if(chuDe == null)
            return;
        else
        {
            Picasso.with(context).load(chuDe.getHinhChuDe()).into(holder.imgHinhChuDe);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DanhSachTheLoaiActivity.class);
                    intent.putExtra("chude", ListChuDe.get(position));
                    context.startActivity(intent);

                }
            });
        }

    }

    @Override
    public int getItemCount() {

        if(ListChuDe != null)
        {
            return ListChuDe.size();
        }
        return 0;
    }

    public class ChuDeViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgHinhChuDe;

        public ChuDeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHinhChuDe = itemView.findViewById(R.id.imgHinhChuDe);

        }
    }
}
