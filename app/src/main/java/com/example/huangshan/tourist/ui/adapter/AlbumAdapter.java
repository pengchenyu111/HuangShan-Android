package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.huangshan.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolader> {

    private Context mContext;
    private List<String> imgUrls;

    public AlbumAdapter(Context mContext, List<String> imgUrls) {
        this.mContext = mContext;
        this.imgUrls = imgUrls;
    }

    @NonNull
    @Override
    public AlbumViewHolader onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_album, parent, false);
        AlbumViewHolader holader = new AlbumViewHolader(view);
        return holader;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolader holder, int position) {
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(10));
        Glide.with(mContext).load(imgUrls.get(position)).apply(options).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imgUrls.size();
    }

    public static class AlbumViewHolader extends RecyclerView.ViewHolder{
        ImageView imageView;
        public AlbumViewHolader(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.album_adapter_img);
        }
    }
}
