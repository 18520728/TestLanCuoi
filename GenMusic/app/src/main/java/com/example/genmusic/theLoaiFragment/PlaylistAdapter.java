package com.example.genmusic.theLoaiFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.BaiHat;
import com.example.genmusic.R;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>{

    private List<BaiHat> listBaiHat;

    public void setData(List<BaiHat> list)
    {
        this.listBaiHat = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);

        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.PlaylistViewHolder holder, int position) {

        BaiHat baiHat = listBaiHat.get(position);
        if(baiHat == null)
            return;
        else
        {
            holder.imgAnhBaiHat.setImageResource(baiHat.getIDHinh());
            holder.txtTieuDeBaiHat.setText(baiHat.getTen());
            holder.txtCaSi.setText(baiHat.getCaSi());
        }
    }

    @Override
    public int getItemCount() {

        if(listBaiHat != null)
        {
            return listBaiHat.size();
        }
        return 0;
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgAnhBaiHat;
        private TextView txtTieuDeBaiHat;
        private TextView txtCaSi;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnhBaiHat = itemView.findViewById(R.id.imgAnhBaiHat);
            txtTieuDeBaiHat = itemView.findViewById(R.id.txtTieuDeBaiHat);
            txtCaSi = itemView.findViewById(R.id.txtCaSi);
        }
    }

}
