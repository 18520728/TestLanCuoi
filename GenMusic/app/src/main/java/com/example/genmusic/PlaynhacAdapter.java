package com.example.genmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.genmusic.bxhFragment.Baihatuathich;

import java.util.ArrayList;

public class PlaynhacAdapter extends RecyclerView.Adapter<PlaynhacAdapter.ViewHolder> {
    Context context;
    ArrayList<Baihatuathich> mangbaihat;

    public PlaynhacAdapter(Context context, ArrayList<Baihatuathich> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dong_playbaihat,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  PlaynhacAdapter.ViewHolder holder, int position) {
        Baihatuathich baihatuathich=mangbaihat.get(position);
        holder.txtcasi.setText(baihatuathich.getCasi());
        holder.txtindex.setText(position + 1+ "");
        holder.txttenbaihat.setText((baihatuathich.getTenbaihat()));
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtindex,txttenbaihat,txtcasi;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            txtcasi=itemView.findViewById(R.id.textviewplaynhactencasi);
            txttenbaihat=itemView.findViewById(R.id.textviewplaynhactenbaihat);
            txtindex=itemView.findViewById(R.id.textviewplaynhacindex);
        }
    }
}
