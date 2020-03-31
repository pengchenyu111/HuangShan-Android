package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huangshan.R;

import java.util.List;

public class ImpressionAdapter extends RecyclerView.Adapter<ImpressionAdapter.ImpressionViewHolder> {

    public static class ImpressionViewHolder extends RecyclerView.ViewHolder {
        ImageView impressionImg;
        TextView impressionText;

        public ImpressionViewHolder(@NonNull View itemView) {
            super(itemView);
            impressionImg = itemView.findViewById(R.id.impression_img);
            impressionText = itemView.findViewById(R.id.impression_text);
        }
    }

    private Context mContext;
    private int[] imgId;
    private String[] desStr;

    public ImpressionAdapter(Context mContext, int[] imgId, String[] desStr) {
        this.mContext = mContext;
        this.imgId = imgId;
        this.desStr = desStr;
    }

    @NonNull
    @Override
    public ImpressionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_impression_item, parent, false);
        ImpressionViewHolder holder = new ImpressionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImpressionViewHolder holder, int position) {
        holder.impressionImg.setImageResource(imgId[position]);
        holder.impressionText.setText(desStr[position]);

        holder.impressionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2020/3/31 影响待写
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgId.length;
    }
}
