package com.example.genmusic.theLoaiFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.DanhsachbaihatActivity;
import com.example.genmusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>{

    //Tạo context (TheLoaiFragment), để lúc sau có thể intent sang 1 activity khác từ fragment này
    private Context context;
    //Tạo list chứa các thể loại
    private List<Album> ListAlbum;

    public AlbumAdapter(Context context, List<Album> listAlbum) {
        this.context = context;
        ListAlbum = listAlbum;
    }
    //Hàm khởi tạo adapter và kết nối với layout xml
    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);

        return new AlbumViewHolder(view);
    }

    //Hàm xử lý với từng item trong recycleview
    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {

        Album album = ListAlbum.get(position);
        if(album == null)
            return;
        else
        {
            holder.txtTenAlbum.setText(album.getTenAlbum());
            holder.txtTenCaSiAlbum.setText(album.getTenCaSiAlbum());
            Picasso.with(context).load(album.getHinhAlbum()).into(holder.imgHinhAlbum);

            //sự kiện click vào mỗi cardview
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("album", ListAlbum.get(position));
                    context.startActivity(intent);
                }
            });
        }

    }
    //Hàm lấy số lượng item
    @Override
    public int getItemCount() {

        if(ListAlbum != null)
        {
            return ListAlbum.size();
        }
        return 0;
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgHinhAlbum;
        private TextView txtTenAlbum, txtTenCaSiAlbum;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            imgHinhAlbum = itemView.findViewById(R.id.imgHinhAlbum);
            txtTenAlbum = itemView.findViewById(R.id.txtTenAlbum);
            txtTenCaSiAlbum = itemView.findViewById(R.id.txtTenCaSiAlbum);
        }
    }
}
