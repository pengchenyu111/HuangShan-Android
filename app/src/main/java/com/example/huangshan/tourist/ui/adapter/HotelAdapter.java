package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.huangshan.R;
import com.example.huangshan.tourist.bean.Hotel;
import com.example.huangshan.tourist.ui.activity.ServeHotelInfoActivity;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private Context mContext;
    private List<Hotel> hotels;

    public HotelAdapter(Context mContext, List<Hotel> hotels) {
        this.mContext = mContext;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_hotel, parent, false);
        HotelViewHolder holder = new HotelViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(10));
        Glide.with(mContext).load(hotels.get(position).getHeadIcon()).apply(options).into(holder.headIcon);
        holder.hotelNameView.setText(hotels.get(position).getHotelName());
        holder.hotelStarView.setNumStars(hotels.get(position).getHotelStar());
        holder.hotelStarView.setRating(hotels.get(position).getHotelStar());

        holder.hotelRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServeHotelInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hotel",hotels.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder{
        LinearLayout hotelRoot;
        ImageView headIcon;
        TextView hotelNameView;
        MaterialRatingBar hotelStarView;
        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelRoot = itemView.findViewById(R.id.hotel_adapter_root);
            headIcon = itemView.findViewById(R.id.hotel_adapter_head);
            hotelNameView = itemView.findViewById(R.id.hotel_adapter_name);
            hotelStarView = itemView.findViewById(R.id.hotel_adapter_star);
        }
    }
}
