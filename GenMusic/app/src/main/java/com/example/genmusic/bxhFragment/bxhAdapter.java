package com.example.genmusic.bxhFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.genmusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class bxhAdapter extends ArrayAdapter<bxh> {

    public bxhAdapter(@NonNull Context context, int resource,@NonNull List<bxh> objects) {
        super(context, resource, objects);
    }
    class ViewHolder{
        TextView txttenchart;
        ImageView imgbackground,imgchart;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder =null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView =inflater.inflate(R.layout.dong_bxh,null);
            viewHolder=new ViewHolder();
            viewHolder.txttenchart=convertView.findViewById(R.id.textviewtenchart);
            viewHolder.imgchart=convertView.findViewById(R.id.imageviewchart);
            viewHolder.imgbackground=convertView.findViewById(R.id.imageviewbackgroundchart);
            convertView.setTag(viewHolder);

        }else
        {
            viewHolder =(ViewHolder) convertView.getTag();
        }
        bxh chart=getItem(position);
        Picasso.with(getContext()).load(chart.getHinh()).into(viewHolder.imgbackground);
        Picasso.with(getContext()).load(chart.getIcon()).into(viewHolder.imgchart);
        viewHolder.txttenchart.setText(chart.getTen());
        return convertView;


    }
}
