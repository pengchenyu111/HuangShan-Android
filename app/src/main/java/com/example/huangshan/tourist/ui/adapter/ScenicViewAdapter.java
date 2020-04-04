package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.huangshan.R;
import com.example.huangshan.tourist.bean.Scenic;
import com.example.huangshan.tourist.bean.ScenicHot;
import com.example.huangshan.tourist.ui.activity.ScenicActivity;

import java.util.List;

public class ScenicViewAdapter extends RecyclerView.Adapter<ScenicViewAdapter.ScenicViewAdapterViewHolder>{

    public static class ScenicViewAdapterViewHolder extends RecyclerView.ViewHolder{

        ImageView scenicImg;
        TextView scenicName;
        TextView scenicHot;

        public ScenicViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            scenicImg = itemView.findViewById(R.id.scenic_view_adapter_img);
            scenicName = itemView.findViewById(R.id.scenic_view_adapter_name);
            scenicHot = itemView.findViewById(R.id.scenic_view_adapter_hot);
        }

    }

    private Context mContext;
    private List<Scenic> scenics;
    private List<ScenicHot> scenicHots;

    public ScenicViewAdapter(Context mContext, List<Scenic> scenics, List<ScenicHot> scenicHots) {
        this.mContext = mContext;
        this.scenics = scenics;
        this.scenicHots = scenicHots;
    }

    @NonNull
    @Override
    public ScenicViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_scenic_view, parent, false);
        ScenicViewAdapterViewHolder holder = new ScenicViewAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScenicViewAdapterViewHolder holder, int position) {
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(10));
        Glide.with(mContext).load(scenics.get(position).getHeadIcon()).apply(options).into(holder.scenicImg);
        holder.scenicName.setText(scenics.get(position).getName());
        holder.scenicHot.setText("实时热度：" + String.valueOf(scenicHots.get(position).getHotNum()) + "人");

        holder.scenicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ScenicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("scenic", scenics.get(position));
                bundle.putInt("scenic_hot",scenicHots.get(position).getHotNum());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scenics.size();
    }
}
